/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators;

import com.google.protobuf.ByteString;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupMessageBody;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.Connection;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.ListCodec;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link SenderHelper} using point-to-point
 * communication provided by the {@link NetworkService}
 */
public class SenderHelperImpl<T> implements SenderHelper<T> {

  final NetworkService<GroupCommMessage> netService;
  final Codec<T> codec;

  @NamedParameter(doc = "codec for the network service", short_name = "nscodec")
  public static class SenderCodec implements Name<Codec<?>> {
  }

  @Inject
  public SenderHelperImpl(final NetworkService<GroupCommMessage> netService,
                          final @Parameter(SenderCodec.class) Codec<T> codec) {
    super();
    this.netService = netService;
    this.codec = codec;
  }

  @Override
  public void send(final Identifier from, final Identifier to,
                   final T element, final Type msgType) throws NetworkException {
    send(from, to, Arrays.asList(element), msgType);
  }

  @Override
  public void send(final Identifier from, final Identifier to,
                   final List<T> elements, final Type msgType) throws NetworkException {
    final GroupCommMessage.Builder GCMBuilder = GroupCommMessage.newBuilder();
    GCMBuilder.setType(msgType);
    GCMBuilder.setSrcid(from.toString());
    GCMBuilder.setDestid(to.toString());
    final GroupMessageBody.Builder bodyBuilder = GroupMessageBody.newBuilder();
    for (final T element : elements) {
      bodyBuilder.setData(ByteString.copyFrom(this.codec.encode(element)));
      GCMBuilder.addMsgs(bodyBuilder.build());
    }
    GroupCommMessage msg = GCMBuilder.build();
    netServiceSend(to, msg);
  }

  @Override
  public void sendListOfList(final Identifier from, final Identifier to,
                             final List<List<T>> elements, final Type msgType)
      throws NetworkException {
    final GroupCommMessage.Builder GCMBuilder = GroupCommMessage.newBuilder();
    GCMBuilder.setType(msgType);
    GCMBuilder.setSrcid(from.toString());
    GCMBuilder.setDestid(to.toString());
    final GroupMessageBody.Builder bodyBuilder = GroupMessageBody.newBuilder();
    final ListCodec<T> lstCodec = new ListCodec<>(this.codec);
    for (final List<T> subElems : elements) {
      bodyBuilder.setData(ByteString.copyFrom(lstCodec.encode(subElems)));
      GCMBuilder.addMsgs(bodyBuilder.build());
    }
    GroupCommMessage msg = GCMBuilder.build();
    netServiceSend(to, msg);
  }

  private void netServiceSend(Identifier to, GroupCommMessage msg) throws NetworkException {
    final Connection<GroupCommMessage> conn = this.netService.newConnection(to);
    conn.open();
    conn.write(msg);
    //conn.close();
  }

  @Override
  public void send(final Identifier from, List<? extends Identifier> to, final List<T> elements,
                   final List<Integer> counts, final Type msgType) throws NetworkException {
    int offset = 0;
    final Iterator<? extends Identifier> toIter = to.iterator();
    for (final int cnt : counts) {
      final Identifier dstId = toIter.next();
      final List<T> subElems = elements.subList(offset, offset + cnt);
      send(from, dstId, subElems, msgType);
      // LOG.log(Level.FINEST, "{0} Sending elements({1}, {2}): {3} to {4}",
      //         new Object[] { from, offset, cnt, subElems, dstId });
      offset += cnt;
    }
  }
}
