/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.io.network.nggroup.impl.utils;

import com.microsoft.reef.io.network.nggroup.impl.driver.MsgKey;

import java.util.*;

/**
 * Map from K to Set<V>
 */
public class SetMap<K, V> {
  private final Map<K, Set<V>> map = new HashMap<>();

  public boolean containsKey(final K key) {
    return map.containsKey(key);
  }

  public boolean contains(final K key, final V value) {
    if (!containsKey(key)) {
      return false;
    }
    return map.get(key).contains(value);
  }

  public Set<V> get(final K key) {
    if (map.containsKey(key)) {
      return map.get(key);
    } else {
      return Collections.emptySet();
    }
  }

  public void add(final K key, final V value) {
    final Set<V> values;
    if (!map.containsKey(key)) {
      values = new HashSet<>();
      map.put(key, values);
    } else {
      values = map.get(key);
    }
    values.add(value);
  }

  public boolean remove(final K key, final V value) {
    if (!map.containsKey(key)) {
      return false;
    }
    final Set<V> set = map.get(key);
    final boolean retVal = set.remove(value);
    if (set.isEmpty()) {
      map.remove(key);
    }
    return retVal;
  }

  /**
   * @param key
   * @return
   */
  public int count(final K key) {
    if (!containsKey(key)) {
      return 0;
    } else {
      return map.get(key).size();
    }
  }

  /**
   * @param key
   */
  public Set<V> remove(final MsgKey key) {
    return map.remove(key);
  }

  public Set<K> keySet() {
    return map.keySet();
  }
}
