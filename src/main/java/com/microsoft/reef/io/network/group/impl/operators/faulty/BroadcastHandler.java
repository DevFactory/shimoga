/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators.faulty;

import com.microsoft.reef.io.network.group.impl.operators.faulty.BroadRedHandler.IDs;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupMessageBody;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.Message;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.EventHandler;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BroadcastHandler implements EventHandler<Message<GroupCommMessage>> {

  private static final Logger LOG = Logger.getLogger(BroadcastHandler.class.getName());

  private final ConcurrentHashMap<Identifier, BlockingQueue<GroupCommMessage>> id2dataQue = new ConcurrentHashMap<>();
  private final BlockingQueue<GroupCommMessage> ctrlQue = new LinkedBlockingQueue<>();

  private final IdentifierFactory idFac;

  @Inject
  public BroadcastHandler(
      final @Parameter(IDs.class) Set<String> ids,
      final @Parameter(BroadReduceConfig.IdFactory.class) IdentifierFactory idFac) {
    this.idFac = idFac;
    for (final String id : ids) {
      final Identifier compId = idFac.getNewInstance(id);
      addChild(compId);
      LOG.log(Level.FINEST, "Listen from: {0}", compId);
    }
  }

  public synchronized void addChild(final Identifier compId) {
    LOG.log(Level.FINEST, "Adding {0} as one of the senders to which I can listen from", compId);
    this.id2dataQue.put(compId, new LinkedBlockingQueue<GroupCommMessage>());
  }

  public synchronized void removeChild(final Identifier compId) {
    LOG.log(Level.FINEST, "Removing {0} as one of the senders to which I can listen from", compId);
    this.id2dataQue.remove(compId);
  }

  @Override
  public void onNext(final Message<GroupCommMessage> value) {

    final GroupCommMessage oneVal = value.getData().iterator().next();
    final Identifier srcId = this.idFac.getNewInstance(oneVal.getSrcid());

    try {
      LOG.log(Level.FINEST, "Message {0} from: {1}", new Object[]{oneVal.getType(), srcId});
      switch (oneVal.getType()) {
        case SourceAdd:
          this.ctrlQue.put(oneVal);
          break;
        case SourceDead:
          this.ctrlQue.put(oneVal);
          // FALL THROUGH:
        default:
          if (this.id2dataQue.containsKey(srcId)) {
            this.id2dataQue.get(srcId).add(oneVal);
          } else {
            LOG.log(Level.FINEST, "Ignoring message from {0}", srcId);
          }
      }
    } catch (final InterruptedException e) {
      final String msg = "Could not put " + oneVal + " into the queue of " + srcId;
      LOG.log(Level.WARNING, msg, e);
      throw new RuntimeException(msg, e);
    }
  }

  public void sync(final Map<Identifier, Integer> isIdAlive
                   /* Set<Identifier> born, Set<Identifier> dead */) {

    LOG.log(Level.FINEST, "Synching any control messages");
    while (!this.ctrlQue.isEmpty()) {
      final GroupCommMessage gcm = this.ctrlQue.poll();
      final Identifier id = this.idFac.getNewInstance(gcm.getSrcid());
      final int status = isIdAlive.containsKey(id) ? isIdAlive.get(id) : 0;
      isIdAlive.put(id, status + 1);
    }
    LOG.log(Level.FINEST, "Id to life status: {0}", isIdAlive);

    for (final Identifier identifier : isIdAlive.keySet()) {
      final int status = isIdAlive.get(identifier);
      if (status < 0) {
        LOG.log(Level.FINEST, "{0} is dead({1}). Removing from handler",
            new Object[]{identifier, status});
        removeChild(identifier);
      } else if (status > 0) {
        LOG.log(Level.FINEST, "{0} is alive({1}). Adding to handler",
            new Object[]{identifier, status});
        addChild(identifier);
      } else {
        //status == 0
        //if(handler can receive from this id)
        //  means that (srcDead + srcAdd)*
        //  TODO: This might put some src dead 
        //  msgs into the queue. We need to remove
        //  those additional srcDeads
        //else
        //  means that (srcAdd + srcDead)*
        //  srcDead msgs will be ignored
        //  So no problem here
      }
    }
  }

  /**
   * @param child
   * @return
   * @throws InterruptedException
   * @throws NetworkException
   */
  public <T> T get(final Identifier id,
                   final Codec<T> codec) throws InterruptedException, NetworkException {

    LOG.log(Level.INFO, "Get from {0}", id);

    if (!this.id2dataQue.containsKey(id)) {
      LOG.log(Level.WARNING, "Can't receive from a non-child");
      throw new RuntimeException("Can't receive from a non-child");
    }

    final GroupCommMessage gcm = this.id2dataQue.get(id).take();
    if (gcm.getType() == Type.SourceDead) {
      LOG.log(Level.FINEST, "Got src dead msg from driver. Terminating wait and returning null");
      return null;
    }

    T retVal = null;
    for (final GroupMessageBody body : gcm.getMsgsList()) {
      retVal = codec.decode(body.getData().toByteArray());
    }
    LOG.log(Level.FINE, "Returning {0}", retVal);
    return retVal;
  }
}
