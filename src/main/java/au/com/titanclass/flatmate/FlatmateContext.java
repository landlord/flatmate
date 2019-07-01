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

public class FlatmateContext implements Context {

  private static Hashtable<String, Object> things = new Hashtable<>();

  @Override
  public Object lookup(Name name) throws NamingException {
    return lookup(name.get(0));
  }

  @Override
  public Object lookup(String name) throws NamingException {
    System.out.println("Lookup " + name + " in FlatmateContext");
    return things.get(name);
  }

  @Override
  public void bind(Name name, Object obj) throws NamingException {
    bind(name.get(0), obj);
  }

  @Override
  public void bind(String name, Object obj) throws NamingException {
    System.out.println("Binding " + name + " in FlatmateContext");
    things.put(name, obj);
  }

  @Override
  public void rebind(Name name, Object obj) throws NamingException {}

  @Override
  public void rebind(String name, Object obj) throws NamingException {}

  @Override
  public void unbind(Name name) throws NamingException {}

  @Override
  public void unbind(String name) throws NamingException {}

  @Override
  public void rename(Name oldName, Name newName) throws NamingException {}

  @Override
  public void rename(String oldName, String newName) throws NamingException {}

  @Override
  public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
    return null;
  }

  @Override
  public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
    return null;
  }

  @Override
  public void destroySubcontext(Name name) throws NamingException {}

  @Override
  public void destroySubcontext(String name) throws NamingException {}

  @Override
  public Context createSubcontext(Name name) throws NamingException {
    return null;
  }

  @Override
  public Context createSubcontext(String name) throws NamingException {
    return null;
  }

  @Override
  public Object lookupLink(Name name) throws NamingException {
    return null;
  }

  @Override
  public Object lookupLink(String name) throws NamingException {
    return null;
  }

  @Override
  public NameParser getNameParser(Name name) throws NamingException {
    return null;
  }

  @Override
  public NameParser getNameParser(String name) throws NamingException {
    return null;
  }

  @Override
  public Name composeName(Name name, Name prefix) throws NamingException {
    return null;
  }

  @Override
  public String composeName(String name, String prefix) throws NamingException {
    return null;
  }

  @Override
  public Object addToEnvironment(String propName, Object propVal) throws NamingException {
    return null;
  }

  @Override
  public Object removeFromEnvironment(String propName) throws NamingException {
    return null;
  }

  @Override
  public Hashtable<?, ?> getEnvironment() throws NamingException {
    return null;
  }

  @Override
  public void close() throws NamingException {}

  @Override
  public String getNameInNamespace() throws NamingException {
    return null;
  }
}
