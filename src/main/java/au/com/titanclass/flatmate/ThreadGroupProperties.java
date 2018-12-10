/*
 * Copyright (c) Titan Class P/L, 2018
 */

package au.com.titanclass.flatmate;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

class ThreadGroupProperties extends Properties {
  private final ConcurrentHashMap<ThreadGroup, Properties> byThreadGroup;

  ThreadGroupProperties() {
    this.byThreadGroup = new ConcurrentHashMap<>();
  }

  void register(final Properties props) {
    final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

    if (threadGroup != null) {
      byThreadGroup.put(threadGroup, props);
    }
  }

  @Override
  public synchronized Enumeration<Object> elements() {
    return get().elements();
  }

  @Override
  public synchronized boolean contains(final Object value) {
    return get().contains(value);
  }

  @Override
  public synchronized boolean containsKey(final Object key) {
    return get().containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return get().containsValue(value);
  }

  @Override
  public Set<Map.Entry<Object, Object>> entrySet() {
    return get().entrySet();
  }

  @Override
  public synchronized void forEach(final BiConsumer<? super Object, ? super Object> action) {
    get().forEach(action);
  }

  @Override
  public synchronized Object get(final Object key) {
    return get().get(key);
  }

  @Override
  public synchronized Object getOrDefault(final Object key, final Object defaultValue) {
    return get().getOrDefault(key, defaultValue);
  }

  @Override
  public String getProperty(final String key) {
    return get().getProperty(key);
  }

  @Override
  public String getProperty(final String key, final String defaultValue) {
    return get().getProperty(key, defaultValue);
  }

  @Override
  public synchronized boolean isEmpty() {
    return get().isEmpty();
  }

  @Override
  public synchronized Enumeration<Object> keys() {
    return get().keys();
  }

  @Override
  public Set<Object> keySet() {
    return get().keySet();
  }

  @Override
  public void list(final PrintStream out) {
    get().list(out);
  }

  @Override
  public void list(final PrintWriter out) {
    get().list(out);
  }

  @Override
  public Enumeration<?> propertyNames() {
    return get().propertyNames();
  }

  @Override
  public synchronized int size() {
    return get().size();
  }

  @Override
  public Set<String> stringPropertyNames() {
    return get().stringPropertyNames();
  }

  @Override
  public Collection<Object> values() {
    return get().values();
  }

  private Properties get() {
    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

    while (true) {
      if (threadGroup == null) {
        return System.getProperties();
      } else {
        final Properties props = byThreadGroup.get(threadGroup);

        if (props == null) {
          threadGroup = threadGroup.getParent();
        } else {
          return props;
        }
      }
    }
  }
}
