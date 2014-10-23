/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.task;

import com.microsoft.reef.io.network.nggroup.api.task.CommunicationGroupClient;
import com.microsoft.reef.io.network.nggroup.api.task.CommunicationGroupServiceClient;
import com.microsoft.reef.io.network.nggroup.api.task.GroupCommClient;
import com.microsoft.reef.io.network.nggroup.api.task.GroupCommNetworkHandler;
import com.microsoft.reef.io.network.nggroup.impl.config.parameters.SerializedGroupConfigs;
import com.microsoft.reef.io.network.proto.ReefNetworkGroupCommProtos.GroupCommMessage;
import org.apache.reef.driver.task.TaskConfigurationOptions;
import org.apache.reef.io.network.impl.NetworkService;
import org.apache.reef.tang.Configuration;
import org.apache.reef.tang.Injector;
import org.apache.reef.tang.Tang;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.Parameter;
import org.apache.reef.tang.exceptions.BindException;
import org.apache.reef.tang.exceptions.InjectionException;
import org.apache.reef.tang.formats.ConfigurationSerializer;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 */
public class GroupCommClientImpl implements GroupCommClient {
  private static final Logger LOG = Logger.getLogger(GroupCommClientImpl.class.getName());

  private final Map<Class<? extends Name<String>>, CommunicationGroupServiceClient> communicationGroups;

  @Inject
  public GroupCommClientImpl(@Parameter(SerializedGroupConfigs.class) final Set<String> groupConfigs,
                             @Parameter(TaskConfigurationOptions.Identifier.class) final String taskId,
                             final GroupCommNetworkHandler groupCommNetworkHandler,
                             final NetworkService<GroupCommMessage> netService,
                             final ConfigurationSerializer configSerializer) {
    this.communicationGroups = new HashMap<>();
    LOG.finest("GroupCommHandler-" + groupCommNetworkHandler.toString());
    for (final String groupConfigStr : groupConfigs) {
      try {
        final Configuration groupConfig = configSerializer.fromString(groupConfigStr);

        final Injector injector = Tang.Factory.getTang().newInjector(groupConfig);
        injector.bindVolatileParameter(TaskConfigurationOptions.Identifier.class, taskId);
        injector.bindVolatileInstance(GroupCommNetworkHandler.class, groupCommNetworkHandler);
        injector.bindVolatileInstance(NetworkService.class, netService);

        final CommunicationGroupServiceClient commGroupClient = injector.getInstance(CommunicationGroupServiceClient.class);

        this.communicationGroups.put(commGroupClient.getName(), commGroupClient);

      } catch (BindException | IOException e) {
        throw new RuntimeException("Unable to deserialize operator config", e);
      } catch (final InjectionException e) {
        throw new RuntimeException("Unable to deserialize operator config", e);
      }
    }

  }

  @Override
  public CommunicationGroupClient getCommunicationGroup(final Class<? extends Name<String>> groupName) {
    return communicationGroups.get(groupName);
  }
}
