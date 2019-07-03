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

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JndiReadinessCheckTests {
  @Test
  public void test() throws Exception {

    final String testJndiName = "test-jndi-name";
    final JndiReadinessCheck jndiReadinessCheck = new JndiReadinessCheck(testJndiName);

    assertFalse(jndiReadinessCheck.isReady());

    new FlatmateContext().bind(testJndiName, new Object());

    jndiReadinessCheck.waitUntilReady();

    assertTrue(jndiReadinessCheck.isReady());
  }
}
