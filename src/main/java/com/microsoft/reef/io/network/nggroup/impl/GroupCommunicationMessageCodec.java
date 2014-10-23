/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl;


import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage.Type;
import org.apache.reef.io.network.impl.StreamingCodec;

import javax.inject.Inject;
import java.io.*;

/**
 * Codec for {@link GroupCommMessage}
 */
public class GroupCommunicationMessageCodec implements StreamingCodec<GroupCommunicationMessage> {

  @Inject
  public GroupCommunicationMessageCodec() {
    // Intentionally Blank
  }

  @Override
  public GroupCommunicationMessage decode(final byte[] data) {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
      try (DataInputStream dais = new DataInputStream(bais)) {
        return decodeFromStream(dais);
      }
    } catch (final IOException e) {
      throw new RuntimeException("IOException", e);
    }
  }

  @Override
  public GroupCommunicationMessage decodeFromStream(final DataInputStream stream) {
    try {
      final String groupName = stream.readUTF();
      final String operName = stream.readUTF();
      final Type msgType = Type.valueOf(stream.readInt());
      final String from = stream.readUTF();
      final int srcVersion = stream.readInt();
      final String to = stream.readUTF();
      final int dstVersion = stream.readInt();
      final byte[][] gcmData = new byte[stream.readInt()][];
      for (int i = 0; i < gcmData.length; i++) {
        gcmData[i] = new byte[stream.readInt()];
        stream.readFully(gcmData[i]);
      }
      return new GroupCommunicationMessage(
          groupName,
          operName,
          msgType,
          from,
          srcVersion,
          to,
          dstVersion,
          gcmData);
    } catch (final IOException e) {
      throw new RuntimeException("IOException", e);
    }
  }

  @Override
  public byte[] encode(final GroupCommunicationMessage msg) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      try (DataOutputStream daos = new DataOutputStream(baos)) {
        encodeToStream(msg, daos);
      }
      return baos.toByteArray();
    } catch (final IOException e) {
      throw new RuntimeException("IOException", e);
    }
  }

  @Override
  public void encodeToStream(final GroupCommunicationMessage msg, final DataOutputStream stream) {
    try {
      stream.writeUTF(msg.getGroupname());
      stream.writeUTF(msg.getOperatorname());
      stream.writeInt(msg.getType().getNumber());
      stream.writeUTF(msg.getSrcid());
      stream.writeInt(msg.getSrcVersion());
      stream.writeUTF(msg.getDestid());
      stream.writeInt(msg.getVersion());
      stream.writeInt(msg.getMsgsCount());
      for (final byte[] b : msg.getData()) {
        stream.writeInt(b.length);
        stream.write(b);
      }
    } catch (final IOException e) {
      throw new RuntimeException("IOException", e);
    }
  }

}
