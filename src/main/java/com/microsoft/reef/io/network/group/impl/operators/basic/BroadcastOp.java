/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators.basic;

import com.microsoft.reef.io.network.group.impl.GroupCommNetworkHandler;
import com.microsoft.reef.io.network.group.impl.operators.ReceiverHelper;
import com.microsoft.reef.io.network.group.impl.operators.ReceiverHelperImpl;
import com.microsoft.reef.io.network.group.impl.operators.SenderHelper;
import com.microsoft.reef.io.network.group.impl.operators.SenderHelperImpl;
import com.microsoft.reef.io.network.group.impl.operators.basic.config.GroupParameters;
import com.microsoft.reef.io.network.group.operators.Broadcast;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.StringCodec;
import org.apache.reef.io.network.util.Utils;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.ComparableIdentifier;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link Broadcast}
 */
public class BroadcastOp implements Broadcast {

  private static Logger LOG = Logger.getLogger(BroadcastOp.class.getName());

  public static class Sender<T> extends SenderBase<T> implements Broadcast.Sender<T> {
    @Inject
    public Sender(
        final NetworkService<GroupCommMessage> netService,
        final GroupCommNetworkHandler multiHandler,
        final @Parameter(GroupParameters.BroadCast.DataCodec.class) Codec<T> codec,
        final @Parameter(GroupParameters.BroadCast.SenderParams.SelfId.class) String self,
        final @Parameter(GroupParameters.BroadCast.SenderParams.ParentId.class) String parent,
        final @Parameter(GroupParameters.BroadCast.SenderParams.ChildIds.class) String children,
        final @Parameter(GroupParameters.IDFactory.class) IdentifierFactory idFac) {
      super(new SenderHelperImpl<>(netService, codec),
          new ReceiverHelperImpl<>(netService, new StringCodec(), multiHandler),
          idFac.getNewInstance(self),
          parent.equals(GroupParameters.defaultValue) ? null : idFac.getNewInstance(parent),
          Utils.parseListCmp(children, idFac));
    }

    public Sender(
        final SenderHelper<T> dataSender,
        final ReceiverHelper<String> ackReceiver,
        final Identifier self, final Identifier parent,
        final List<ComparableIdentifier> children) {
      super(dataSender, ackReceiver, self, parent, children);
    }

    @Override
    public void send(final T element) throws NetworkException, InterruptedException {

      for (final Identifier child : getChildren()) {
        this.dataSender.send(getSelf(), child, element, Type.Broadcast);
      }

      final List<String> result =
          this.ackReceiver.receive(getChildren(), getSelf(), Type.Broadcast);

      LOG.log(Level.FINEST, "{0} received: {1} from {2}",
          new Object[]{getSelf(), result, getChildren()});
    }
  }

  public static class Receiver<T> extends ReceiverBase<T> implements Broadcast.Receiver<T> {
    @Inject
    public Receiver(
        final NetworkService<GroupCommMessage> netService,
        final GroupCommNetworkHandler multiHandler,
        final @Parameter(GroupParameters.BroadCast.DataCodec.class) Codec<T> codec,
        final @Parameter(GroupParameters.BroadCast.ReceiverParams.SelfId.class) String self,
        final @Parameter(GroupParameters.BroadCast.ReceiverParams.ParentId.class) String parent,
        final @Parameter(GroupParameters.BroadCast.ReceiverParams.ChildIds.class) String children,
        final @Parameter(GroupParameters.IDFactory.class) IdentifierFactory idFac) {
      super(new ReceiverHelperImpl<>(netService, codec, multiHandler),
          new SenderHelperImpl<>(netService, new StringCodec()),
          idFac.getNewInstance(self),
          idFac.getNewInstance(parent),
          children.equals(GroupParameters.defaultValue) ?
              null : Utils.parseListCmp(children, idFac));
    }

    public Receiver(
        final ReceiverHelper<T> dataReceiver,
        final SenderHelper<String> ackSender,
        final Identifier self, final Identifier parent,
        final List<ComparableIdentifier> children) {
      super(dataReceiver, ackSender, self, parent, children);
    }

    @Override
    public T receive() throws NetworkException, InterruptedException {
      final T result = this.dataReceiver.receive(getParent(), getSelf(), Type.Broadcast);
      LOG.log(Level.FINEST, "{0} received: {1} from {2}",
          new Object[]{getSelf(), result, getParent()});
      this.ackSender.send(getSelf(), getParent(), "ACK", Type.Broadcast);
      return result;
    }
  }
}
