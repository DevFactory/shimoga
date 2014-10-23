/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import com.microsoft.reef.io.network.nggroup.api.GroupChanges;
import com.microsoft.reef.io.network.nggroup.api.task.CommunicationGroupClient;

/**
 *
 */
public class SynchronizationUtils {
  public static boolean chkAndUpdate(final CommunicationGroupClient communicationGroupClient) {
    long t1 = System.currentTimeMillis();
    final GroupChanges changes = communicationGroupClient.getTopologyChanges();
    long t2 = System.currentTimeMillis();
    System.out.println("Time to get TopologyChanges = " + (t2 - t1) / 1000.0 + " sec");
    if (changes.exist()) {
      System.out.println("There exist topology changes. Asking to update Topology");
      t1 = System.currentTimeMillis();
      communicationGroupClient.updateTopology();
      t2 = System.currentTimeMillis();
      System.out.println("Time to get TopologyUpdated = " + (t2 - t1) / 1000.0 + " sec");
      return true;
    } else {
      System.out.println("No changes in topology exist. So not updating topology");
      return false;
    }
  }
}
