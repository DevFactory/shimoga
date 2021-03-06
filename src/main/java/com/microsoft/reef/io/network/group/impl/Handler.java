/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl;

import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import org.apache.reef.wake.EventHandler;
import org.apache.reef.wake.Identifier;

/**
 * Interface of a {@link EventHandler} for handling {@link GroupCommMessage}
 */
public interface Handler extends EventHandler<GroupCommMessage> {
  GroupCommMessage getData(Identifier srcId) throws InterruptedException;
}
