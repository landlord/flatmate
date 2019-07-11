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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JndiReadinessCheck implements ReadinessCheck {

  private Context ctx;
  private String name;

  /**
   * A ReadinessCheck that waits until a named value is available in the <code>FlatmateContext
   * </code> JNDI context
   *
   * @param name The name of the object to lookup
   */
  JndiReadinessCheck(String name) {

    this.name = name;
    Properties props = new Properties();

    props.put(Context.INITIAL_CONTEXT_FACTORY, "au.com.titanclass.flatmate.FlatmateContextFactory");

    try {
      ctx = new InitialContext(props);
    } catch (NamingException e) {
      System.err.println("Naming exception " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public boolean isReady() throws Exception {
    return ctx.lookup(name) != null;
  }
}
