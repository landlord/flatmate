/*
 * Copyright 2018 Titan Class Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.com.titanclass.flatmate;

import javax.naming.*;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

/** A simple JNDI context backed by a <code>ConcurrentHashMap</code> */
public class FlatmateContext implements Context {

  private static ConcurrentHashMap<String, Object> things = new ConcurrentHashMap<>();

  @Override
  public Object lookup(Name name) {
    return lookup(name.get(0));
  }

  @Override
  public Object lookup(String name) {
    if (name == null) throw new IllegalArgumentException("name must not be null");
    return things.get(name);
  }

  @Override
  public void bind(Name name, Object obj) {
    bind(name.get(0), obj);
  }

  @Override
  public void bind(String name, Object obj) {
    if (name == null) throw new IllegalArgumentException("name must not be null");
    if (obj == null) throw new IllegalArgumentException("obj must not be null");
    things.put(name, obj);
  }

  @Override
  public void rebind(Name name, Object obj) {}

  @Override
  public void rebind(String name, Object obj) {}

  @Override
  public void unbind(Name name) {}

  @Override
  public void unbind(String name) {}

  @Override
  public void rename(Name oldName, Name newName) {}

  @Override
  public void rename(String oldName, String newName) {}

  @Override
  public NamingEnumeration<NameClassPair> list(Name name) {
    return null;
  }

  @Override
  public NamingEnumeration<NameClassPair> list(String name) {
    return null;
  }

  @Override
  public NamingEnumeration<Binding> listBindings(Name name) {
    return null;
  }

  @Override
  public NamingEnumeration<Binding> listBindings(String name) {
    return null;
  }

  @Override
  public void destroySubcontext(Name name) {}

  @Override
  public void destroySubcontext(String name) {}

  @Override
  public Context createSubcontext(Name name) {
    return null;
  }

  @Override
  public Context createSubcontext(String name) {
    return null;
  }

  @Override
  public Object lookupLink(Name name) {
    return null;
  }

  @Override
  public Object lookupLink(String name) {
    return null;
  }

  @Override
  public NameParser getNameParser(Name name) {
    return null;
  }

  @Override
  public NameParser getNameParser(String name) {
    return null;
  }

  @Override
  public Name composeName(Name name, Name prefix) {
    return null;
  }

  @Override
  public String composeName(String name, String prefix) {
    return null;
  }

  @Override
  public Object addToEnvironment(String propName, Object propVal) {
    return null;
  }

  @Override
  public Object removeFromEnvironment(String propName) {
    return null;
  }

  @Override
  public Hashtable<?, ?> getEnvironment() {
    return null;
  }

  @Override
  public void close() {}

  @Override
  public String getNameInNamespace() {
    return null;
  }
}
