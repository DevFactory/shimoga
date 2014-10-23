/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.task;

import com.microsoft.reef.io.network.nggroup.impl.GroupCommunicationMessage;
import com.microsoft.reef.io.network.nggroup.impl.task.GroupCommNetworkHandlerImpl;
import org.apache.reef.annotations.audience.TaskSide;
import org.apache.reef.io.network.Message;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.wake.EventHandler;

/**
 * The global EventHandler that receives the GroupCommunicationMsg
 * and routes it to the relevant communication group
 */
@TaskSide
@DefaultImplementation(value = GroupCommNetworkHandlerImpl.class)
public interface GroupCommNetworkHandler extends EventHandler<Message<GroupCommunicationMessage>> {

  void register(Class<? extends Name<String>> groupName, EventHandler<GroupCommunicationMessage> commGroupNetworkHandler);
}
