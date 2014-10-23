/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators.basic;

import com.microsoft.reef.io.network.group.impl.GroupCommNetworkHandler;
import com.microsoft.reef.io.network.group.impl.operators.basic.config.GroupParameters;
import com.microsoft.reef.io.network.group.operators.AllGather;
import com.microsoft.reef.io.network.group.operators.Broadcast;
import com.microsoft.reef.io.network.group.operators.Gather;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.ListCodec;
import org.apache.reef.io.network.util.Utils;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.ComparableIdentifier;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@link AllGather}
 */
public class AllGatherOp<T> extends SenderReceiverBase implements AllGather<T> {

  final Gather.Sender<T> gatherSender;
  final Gather.Receiver<T> gatherReceiver;
  final Broadcast.Sender<List<T>> broadcastSender;
  final Broadcast.Receiver<List<T>> broadcastReceiver;

  @Inject
  public AllGatherOp(
      final NetworkService<GroupCommMessage> netService,
      final GroupCommNetworkHandler multiHandler,
      final @Parameter(GroupParameters.AllGather.DataCodec.class) Codec<T> codec,
      final @Parameter(GroupParameters.AllGather.SelfId.class) String self,
      final @Parameter(GroupParameters.AllGather.ParentId.class) String parent,
      final @Parameter(GroupParameters.AllGather.ChildIds.class) String children,
      final @Parameter(GroupParameters.IDFactory.class) IdentifierFactory idFac) {

    this(
        new GatherOp.Sender<>(netService, multiHandler, codec, self, parent, children, idFac),
        new GatherOp.Receiver<>(netService, multiHandler, codec, self, parent, children, idFac),
        new BroadcastOp.Sender<>(netService, multiHandler, new ListCodec<>(codec), self, parent, children, idFac),
        new BroadcastOp.Receiver<>(netService, multiHandler, new ListCodec<>(codec), self, parent, children, idFac),
        idFac.getNewInstance(self),
        parent.equals(GroupParameters.defaultValue) ? null : idFac.getNewInstance(parent),
        children.equals(GroupParameters.defaultValue) ? null : Utils.parseListCmp(children, idFac));
  }

  public AllGatherOp(
      final Gather.Sender<T> gatherSender,
      final Gather.Receiver<T> gatherReceiver,
      final Broadcast.Sender<List<T>> broadcastSender,
      final Broadcast.Receiver<List<T>> broadcastReceiver,
      final Identifier self,
      final Identifier parent,
      final List<ComparableIdentifier> children) {

    super(self, parent, children);

    this.gatherSender = gatherSender;
    this.gatherReceiver = gatherReceiver;
    this.broadcastSender = broadcastSender;
    this.broadcastReceiver = broadcastReceiver;
  }

  @Override
  public List<T> apply(final T element) throws NetworkException, InterruptedException {
    return apply(element, getChildren());
  }

  @Override
  public List<T> apply(final T element, final List<? extends Identifier> order)
      throws NetworkException, InterruptedException {

    final List<T> result;

    if (getParent() == null) {
      result = this.gatherReceiver.receive(order);
      this.broadcastSender.send(result);
    } else {
      this.gatherSender.send(element);
      result = this.broadcastReceiver.receive();
    }

    return result;
  }
}
