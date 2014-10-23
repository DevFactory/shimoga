/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.bgd.utils;

import com.microsoft.reef.examples.nggroup.bgd.MasterTask;
import org.apache.reef.driver.task.TaskConfiguration;
import org.apache.reef.driver.task.TaskConfigurationOptions;
import org.apache.reef.tang.Configuration;
import org.apache.reef.tang.Injector;
import org.apache.reef.tang.JavaConfigurationBuilder;
import org.apache.reef.tang.Tang;
import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.exceptions.BindException;
import org.apache.reef.tang.exceptions.InjectionException;
import org.apache.reef.tang.formats.AvroConfigurationSerializer;
import org.apache.reef.tang.formats.ConfigurationSerializer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SubConfiguration {

  private static final Logger LOG = Logger.getLogger(SubConfiguration.class.getName());

  @SafeVarargs
  public static Configuration from(
      final Configuration baseConf, final Class<? extends Name<?>>... classes) {

    final Injector injector = Tang.Factory.getTang().newInjector(baseConf);
    final JavaConfigurationBuilder confBuilder = Tang.Factory.getTang().newConfigurationBuilder();

    for (final Class<? extends Name<?>> clazz : classes) {
      try {
        confBuilder.bindNamedParameter(clazz,
            injector.getNamedInstance((Class<? extends Name<Object>>) clazz).toString());
      } catch (final BindException | InjectionException ex) {
        final String msg = "Exception while creating subconfiguration";
        LOG.log(Level.WARNING, msg, ex);
        throw new RuntimeException(msg, ex);
      }
    }

    return confBuilder.build();
  }

  public static void main(final String[] args) throws BindException, InjectionException {

    final Configuration conf = TaskConfiguration.CONF
        .set(TaskConfiguration.IDENTIFIER, "TASK")
        .set(TaskConfiguration.TASK, MasterTask.class)
        .build();

    final ConfigurationSerializer confSerizalizer = new AvroConfigurationSerializer();
    final Configuration subConf = SubConfiguration.from(conf, TaskConfigurationOptions.Identifier.class);
    System.out.println("Base conf:\n" + confSerizalizer.toString(conf));
    System.out.println("Sub conf:\n" + confSerizalizer.toString(subConf));
  }
}
