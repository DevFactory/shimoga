/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators;

import com.microsoft.reef.io.network.group.impl.GroupCommNetworkHandler;
import com.microsoft.reef.io.network.group.impl.Handler;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupMessageBody;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.ListCodec;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.remote.Codec;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link ReceiverHelper} using point-to-point
 * communication provided by the {@link NetworkService}
 *
 * @param <T>
 */
public class ReceiverHelperImpl<T> implements ReceiverHelper<T> {

  NetworkService<GroupCommMessage> netService;
  Codec<T> codec;
  GroupCommNetworkHandler multiHandler;

  @NamedParameter(doc = "codec for the network service", short_name = "nscodec")
  public static class ReceiverCodec implements Name<Codec<?>> {
    //intentionally blank
  }

  @Inject
  public ReceiverHelperImpl(
      NetworkService<GroupCommMessage> netService,
      @Parameter(ReceiverCodec.class) Codec<T> codec,
      GroupCommNetworkHandler multiHandler) {
    super();
    this.netService = netService;
    this.codec = codec;
    this.multiHandler = multiHandler;
  }

  @Override
  public T receive(Identifier from, Identifier to, Type msgType)
      throws InterruptedException {
    return receiveList(from, to, msgType).get(0);
  }

  @Override
  public List<T> receiveList(Identifier from, Identifier to, Type msgType)
      throws InterruptedException {
    Handler handler = multiHandler.getHandler(msgType);
    GroupCommMessage data = handler.getData(from);
    List<T> retVal = new ArrayList<>();
    for (GroupMessageBody body : data.getMsgsList()) {
      retVal.add(codec.decode(body.getData().toByteArray()));
    }
    return retVal;
  }

  @Override
  public List<List<T>> receiveListOfList(Identifier from, Identifier to, Type msgType)
      throws InterruptedException {
    Handler handler = multiHandler.getHandler(msgType);
    GroupCommMessage data = handler.getData(from);
    ListCodec<T> lstCodec = new ListCodec<>(codec);
    List<List<T>> retVal = new ArrayList<>();
    for (GroupMessageBody body : data.getMsgsList()) {
      retVal.add(lstCodec.decode(body.getData().toByteArray()));
    }
    return retVal;
  }

  @Override
  public List<T> receive(List<? extends Identifier> from, Identifier to, Type msgType)
      throws InterruptedException {
    List<T> result = new ArrayList<>();
    for (Identifier id : from)
      result.add(receive(id, to, msgType));
    return result;
  }

}
