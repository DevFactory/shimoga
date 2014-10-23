/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.operators;

import com.microsoft.reef.io.network.group.operators.AbstractGroupCommOperator;
import com.microsoft.reef.io.network.nggroup.impl.GroupCommunicationMessage;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.Connection;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.io.network.util.StringIdentifierFactory;
import org.apache.reef.wake.Identifier;
import org.apache.reef.wake.IdentifierFactory;

import java.util.Arrays;
import java.util.logging.Logger;

public class Sender extends AbstractGroupCommOperator {

  private static final Logger LOG = Logger.getLogger(Sender.class.getName());

  private final NetworkService<GroupCommunicationMessage> netService;
  private final IdentifierFactory idFac = new StringIdentifierFactory();

  public Sender(final NetworkService<GroupCommunicationMessage> netService) {
    this.netService = netService;
  }

  public void send(final GroupCommunicationMessage msg) throws NetworkException {
    LOG.entering("Sender", "send", msg);
    final String dest = msg.getDestid();
    send(msg, dest);
    LOG.exiting("Sender", "send", msg);
  }

  public void send(final GroupCommunicationMessage msg, final String dest) throws NetworkException {
    LOG.entering("Sender", "send", new Object[]{msg, dest});
    final Identifier destId = idFac.getNewInstance(dest);
    final Connection<GroupCommunicationMessage> link = netService.newConnection(destId);
    link.open();
    link.write(msg);
    LOG.exiting("Sender", "send", Arrays.toString(new Object[]{msg, dest}));
  }
}
