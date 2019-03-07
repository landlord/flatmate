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

import org.junit.*;

import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.*;

public class ArgsTests {
  @Test
  public void testEmpty() {
    assertFalse(Args.parse(new String[] {}).isPresent());
  }

  @Test
  public void testSingleJar() {
    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(Paths.get("test.jar"), new ArrayList<>(), new ArrayList<>())))),
        Args.parse(new String[] {"test.jar"}));

    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(Paths.get("test.jar"), new ArrayList<>(), new ArrayList<>())))),
        Args.parse(new String[] {"test.jar", "--"}));

    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(Paths.get("test.jar"), new ArrayList<>(), new ArrayList<>())))),
        Args.parse(new String[] {"test.jar", "--", "--"}));
  }

  @Test
  public void testSingleJarWithProps() {
    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(
                        Paths.get("test.jar"),
                        Arrays.asList(
                            new AbstractMap.SimpleEntry<>("key1", "1234"),
                            new AbstractMap.SimpleEntry<>("key2", "1234")),
                        new ArrayList<>())))),
        Args.parse(new String[] {"test.jar", "-Dkey1=1234", "-Dkey2=1234"}));

    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(
                        Paths.get("test.jar"),
                        Arrays.asList(
                            new AbstractMap.SimpleEntry<>("key1", "1234"),
                            new AbstractMap.SimpleEntry<>("key2", "1234")),
                        new ArrayList<>())))),
        Args.parse(new String[] {"test.jar", "-Dkey1=1234", "-Dkey2=1234", "--"}));
  }

  @Test
  public void testSingleJarWithArgs() {
    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(
                        Paths.get("test.jar"),
                        Arrays.asList(),
                        Arrays.asList("argOne", "argTwo"))))),
        Args.parse(new String[] {"test.jar", "--", "argOne", "argTwo"}));

    assertEquals(
        Optional.of(
            new ArrayList<>(
                Arrays.asList(
                    new JarApp(
                        Paths.get("test.jar"),
                        Arrays.asList(
                            new AbstractMap.SimpleEntry<>("key1", "1234"),
                            new AbstractMap.SimpleEntry<>("key2", "1234")),
                        Arrays.asList("argOne", "argTwo"))))),
        Args.parse(
            new String[] {
              "test.jar", "-Dkey1=1234", "-Dkey2=1234", "--", "argOne", "argTwo", "--"
            }));
  }

  @Test
  public void testMultipleJars() {
    assertEquals(
        Optional.of(
            new ArrayList<JarApp>(
                Arrays.asList(
                    new JarApp(Paths.get("test.jar"), Arrays.asList(), new ArrayList<>()),
                    new JarApp(Paths.get("test2.jar"), Arrays.asList(), new ArrayList<>())))),
        Args.parse(new String[] {"test.jar", "--", "--", "test2.jar", "--", "--"}));
  }
}
