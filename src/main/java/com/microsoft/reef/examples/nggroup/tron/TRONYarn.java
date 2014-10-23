/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import com.microsoft.reef.examples.nggroup.tron.utils.Timer;
import org.apache.reef.client.LauncherStatus;
import org.apache.reef.runtime.yarn.client.YarnClientConfiguration;
import org.apache.reef.tang.Configuration;

/**
 * Runs BGD on the YARN runtime.
 */
public class TRONYarn {

  private static final int TIMEOUT = 4 * Timer.HOURS;

  public static void main(final String[] args) throws Exception {

    final TRONClient bgdClient = TRONClient.fromCommandLine(args);

    final Configuration runtimeConfiguration = YarnClientConfiguration.CONF
        .set(YarnClientConfiguration.JVM_HEAP_SLACK, "0.1")
        .build();

    final String jobName = System.getProperty("user.name") + "-" + "BR-ResourceAwareBGD-YARN";

    final LauncherStatus status = bgdClient.run(runtimeConfiguration, jobName, TIMEOUT);

    System.out.println("Result: " + status);
  }
}
