/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.group.impl.operators.faulty;

import org.apache.reef.wake.Identifier;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SyncHelper {

  private static final Logger LOG = Logger.getLogger(SyncHelper.class.getName());

  static void update(final Set<Identifier> childIdentifiers,
                     final Map<Identifier, Integer> childStatus,
                     final Identifier self) {

    // TODO: Currently does not care about parents
    // Assumes that all ctr msgs are about children
    // There is currently only one case where ctrl
    // msgs are sent about parents thats when the
    // task is complete or control task finishes
    // It works now because we do not depend on sync
    // functionality for that

    for (final Identifier childIdentifier : childStatus.keySet()) {
      final int status = childStatus.get(childIdentifier);
      if (status < 0) {
        LOG.log(Level.FINEST, "RedReceiver: Removing {0} from children of {1}",
            new Object[]{childIdentifier, self});
        childIdentifiers.remove(childIdentifier);
      } else if (status > 0) {
        LOG.log(Level.FINEST, "RedReceiver: Adding {0} from children of {1}",
            new Object[]{childIdentifier, self});
        LOG.log(Level.FINEST, "RedReceiver: Adding " + childIdentifier + " to children of " + self);
        childIdentifiers.add(childIdentifier);
      } else {
        //No change. Need not worry
      }
    }
  }
}
