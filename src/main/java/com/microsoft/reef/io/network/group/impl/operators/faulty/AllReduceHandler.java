/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators.faulty;

import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupMessageBody;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.Message;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.EventHandler;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllReduceHandler implements EventHandler<Message<GroupCommMessage>> {

  private static final Logger LOG = Logger.getLogger(AllReduceHandler.class.getName());

  private final ConcurrentHashMap<Identifier,
      BlockingQueue<GroupCommMessage>> id2que = new ConcurrentHashMap<>();

  @NamedParameter(doc = "List of Identifiers on which the handler should listen")
  public static class IDs implements Name<Set<String>> {
  }

  private final IdentifierFactory idFac;

  @Inject
  public AllReduceHandler(
      final @Parameter(IDs.class) Set<String> ids,
      final @Parameter(AllReduceConfig.IdFactory.class) IdentifierFactory idFac) {
    this.idFac = idFac;
    for (final String id : ids) {
      final Identifier compId = idFac.getNewInstance(id);
      this.id2que.put(compId, new LinkedBlockingQueue<GroupCommMessage>());
      LOG.log(Level.FINEST, "Listen from: {0}", compId);
    }
  }

  @Override
  public void onNext(final Message<GroupCommMessage> value) {
    final GroupCommMessage oneVal = value.getData().iterator().next();
    final Identifier srcId = this.idFac.getNewInstance(oneVal.getSrcid());
    try {
      LOG.log(Level.FINEST, "Message {0} from: {1}", new Object[]{oneVal, srcId});
      this.id2que.get(srcId).put(oneVal);
    } catch (final InterruptedException e) {
      final String msg = "Could not put " + oneVal + " into the queue of " + srcId;
      LOG.log(Level.WARNING, msg, e);
      throw new RuntimeException(msg, e);
    }
  }

  public <T> T get(final Identifier id,
                   final Codec<T> codec) throws InterruptedException, NetworkException {

    LOG.log(Level.FINEST, "Get from {0}", id);

    if (!this.id2que.containsKey(id)) {
      final RuntimeException ex = new RuntimeException("Can't receive from a non-child");
      LOG.log(Level.WARNING, "Can't receive from a non-child", ex);
      throw ex;
    }

    final GroupCommMessage gcm = this.id2que.get(id).take();
    if (gcm.getType() == Type.SourceDead) {
      LOG.log(Level.FINEST, "Got src dead msg from driver. Terminating wait and returning null");
      return null;
    }

    T retVal = null;
    for (final GroupMessageBody body : gcm.getMsgsList()) {
      retVal = codec.decode(body.getData().toByteArray());
    }

    LOG.log(Level.FINEST, "\t\tReturning {0}", retVal);

    return retVal;
  }
}
