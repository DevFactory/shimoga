/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.services.network.group;

import com.microsoft.reef.io.network.group.impl.ExceptionHandler;
import com.microsoft.reef.io.network.group.impl.GCMCodec;
import com.microsoft.reef.io.network.group.impl.operators.faulty.*;
import com.microsoft.reef.io.network.group.operators.Reduce.ReduceFunction;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.impl.MessagingTransportFactory;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.StringIdentifierFactory;
import org.apache.reef.wake.EventHandler;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;
import org.apache.reef.wake.remote.Codec;
import org.apache.reef.wake.remote.NetUtils;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckBroadcastMemoryLeakSender {
  private static final Logger LOG = Logger.getLogger(CheckBroadcastMemoryLeakSender.class.getName());
  private static final IdentifierFactory idFac = new StringIdentifierFactory();
  private static final String nameServiceAddr = NetUtils.getLocalAddress();
  private static final int nameServicePort = 5678;


  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Thread.sleep(1000);
    Identifier senderId = idFac.getNewInstance("SENDER");
    Identifier receiverId = idFac.getNewInstance("RECEIVER");
    Codec<byte[]> baCodec = new Codec<byte[]>() {

      @Override
      public byte[] decode(byte[] arr) {
        return arr;
      }

      @Override
      public byte[] encode(byte[] arr) {
        return arr;
      }
    };
    EventHandler<Exception> excHandler = new ExceptionHandler();
    final Set<String> rcvIds = Collections.singleton(receiverId.toString());
    final BroadcastHandler sndHandler = new BroadcastHandler(rcvIds, idFac);
    final ReduceHandler rcvHandler = new ReduceHandler(rcvIds, idFac);
    final BroadRedHandler sndRcvHandler = new BroadRedHandler(sndHandler, rcvHandler);
    final NetworkService<GroupCommMessage> senderService = new NetworkService<>(idFac, 0, nameServiceAddr, nameServicePort, new GCMCodec(), new MessagingTransportFactory(), sndRcvHandler, excHandler);
    senderService.registerId(senderId);
    final BroadcastOp.Sender<byte[]> brdCstSender = new BroadcastOp.Sender<>(senderService, sndHandler, baCodec, senderId.toString(), null, rcvIds, idFac);
    ReduceFunction<byte[]> redFunc = new ReduceFunction<byte[]>() {

      @Override
      public byte[] apply(Iterable<byte[]> elements) {
        return elements.iterator().next();
      }
    };
    final ReduceOp.Receiver<byte[]> reduceReceiver = new ReduceOp.Receiver<>(senderService, rcvHandler, baCodec, redFunc, senderId.toString(), null, rcvIds, idFac);

    final int iterations = 100;
    Random r = new Random(1337);
    byte[] rBytes = new byte[1 << 26];
    r.nextBytes(rBytes);
    for (int i = 0; i < iterations; i++) {
      try {
        brdCstSender.send(rBytes);
        reduceReceiver.reduce();
        LOG.log(Level.FINEST, "Sent " + (i + 1));
//        Thread.sleep(6000);
      } catch (NetworkFault | NetworkException | InterruptedException e) {
        e.printStackTrace();
      }
    }
    senderService.unregisterId(senderId);
    senderService.close();
  }

}
