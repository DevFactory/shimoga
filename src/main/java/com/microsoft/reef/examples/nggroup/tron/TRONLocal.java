/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron;

import com.microsoft.reef.examples.nggroup.tron.utils.Timer;
import org.apache.reef.client.LauncherStatus;
import org.apache.reef.runtime.local.client.LocalRuntimeConfiguration;
import org.apache.reef.tang.Configuration;

/**
 * Runs BGD on the local runtime.
 */
public class TRONLocal {
  private static final String NUM_LOCAL_THREADS = "20";
  private static final int TIMEOUT = 10 * Timer.MINUTES;

  public static void main(final String[] args) throws Exception {

    final TRONClient bgdClient = TRONClient.fromCommandLine(args);

    final Configuration runtimeConfiguration = LocalRuntimeConfiguration.CONF
        .set(LocalRuntimeConfiguration.NUMBER_OF_THREADS, NUM_LOCAL_THREADS)
        .build();

    final String jobName = System.getProperty("user.name") + "-" + "ResourceAwareTRONLocal";

    final LauncherStatus status = bgdClient.run(runtimeConfiguration, jobName, TIMEOUT);

    System.out.println("Result: " + status);
  }
}
