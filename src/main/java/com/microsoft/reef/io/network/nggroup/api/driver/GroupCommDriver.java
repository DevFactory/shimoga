/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.api.driver;

import com.microsoft.reef.io.network.nggroup.impl.driver.GroupCommDriverImpl;
import org.apache.reef.annotations.Provided;
import org.apache.reef.annotations.audience.DriverSide;
import org.apache.reef.driver.context.ActiveContext;
import org.apache.reef.tang.Configuration;
import org.apache.reef.tang.annotations.DefaultImplementation;
import org.apache.reef.tang.annotations.Name;

/**
 * The driver side interface of Group Communication
 * which is the entry point for the service
 */
@DriverSide
@Provided
@DefaultImplementation(value = GroupCommDriverImpl.class)
public interface GroupCommDriver {

  /**
   * Create a new communication group with the specified name
   * and the minimum number of tasks needed in this group before
   * communication can start
   *
   * @param groupName
   * @param numberOfTasks
   * @return
   */
  CommunicationGroupDriver newCommunicationGroup(Class<? extends Name<String>> groupName, int numberOfTasks);

  /**
   * Tests whether the activeContext is a context configured
   * using the Group Communication Service
   *
   * @param activeContext
   * @return
   */
  boolean isConfigured(ActiveContext activeContext);

  /**
   * @return Configuration needed for a Context that should have
   * Group Communication Service enabled
   */
  Configuration getContextConfiguration();

  /**
   * @return Configuration needed to enable
   * Group Communication as a Service
   */
  Configuration getServiceConfiguration();

  /**
   * @return Configuration needed for a Task that should have
   * Group Communication Service enabled
   */
  Configuration getTaskConfiguration(Configuration partialTaskConf);

}
