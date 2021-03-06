/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.task;

import com.microsoft.reef.io.network.nggroup.impl.GroupCommunicationMessage;
import com.microsoft.reef.io.network.nggroup.impl.task.CommGroupNetworkHandlerImpl;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.wake.EventHandler;

/**
 * The EventHandler that receives the GroupCommunicationMsg
 * pertaining to a specific Communication Group
 */
@DefaultImplementation(value = CommGroupNetworkHandlerImpl.class)
public interface CommGroupNetworkHandler extends EventHandler<GroupCommunicationMessage> {

  void register(Class<? extends Name<String>> operName, EventHandler<GroupCommunicationMessage> handler);

  void addTopologyElement(Class<? extends Name<String>> operName);

  GroupCommunicationMessage waitForTopologyUpdate(Class<? extends Name<String>> operName);

  byte[] waitForTopologyChanges(Class<? extends Name<String>> operName);
}
