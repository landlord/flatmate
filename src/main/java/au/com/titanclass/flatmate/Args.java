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

import java.nio.file.Paths;
import java.util.*;

/**
 * Parses the provided arguments.
 *
 * <p>Examples:
 *
 * <p>--app myapp.jar -- argone argtwo --
 */
class Args {
  private enum Mode {
    JAR,
    PROPS,
    ARGS
  }

  static Optional<List<JarApp>> parse(final String[] args) {
    final Iterator<String> argsIterator = Arrays.stream(args).iterator();

    Mode mode = Mode.JAR;
    final List<JarApp> jarApps = new ArrayList<>();
    JarApp jarApp = null;

    while (argsIterator.hasNext()) {
      String arg = argsIterator.next();
      switch (mode) {
        case JAR:
          jarApp =
              new JarApp(Paths.get(arg), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
          mode = Mode.PROPS;
          break;

        case PROPS:
          if (arg.equals("--")) {
            mode = Mode.ARGS;
          } else if (arg.startsWith("-D")) {
            final int equalsIndex = arg.indexOf('=');
            final String key;
            final String value;

            if (equalsIndex != -1) {
              key = arg.substring(2, equalsIndex);
              value = arg.substring(equalsIndex + 1);
            } else {
              key = arg.substring(2);
              value = "";
            }

            jarApp.properties.add(new AbstractMap.SimpleEntry<>(key, value));
          } else if (arg.startsWith("-ready") && argsIterator.hasNext()) {
            final String tcpScheme = "tcp://";
            final String jndiNamePrefix = "jndi:";
            final String readinessCheck = argsIterator.next();

            if (readinessCheck.startsWith(tcpScheme)) {
              System.out.println("Using tcpScheme: " + arg);
              final String[] readinessCheckParts =
                  readinessCheck.substring(tcpScheme.length()).split(":", 2);

              if (readinessCheckParts.length != 2
                  || readinessCheckParts[0].isEmpty()
                  || !readinessCheckParts[1].matches("^[0-9]+$")) {
                return Optional.empty();
              }

              jarApp.readinessChecks.add(
                  new TcpReadinessCheck(
                      readinessCheckParts[0], Integer.parseInt(readinessCheckParts[1])));
            } else if (readinessCheck.startsWith(jndiNamePrefix)) {
              System.out.println("Using jndiNamePrefix: " + readinessCheck);
              final String jndiName = readinessCheck.substring(jndiNamePrefix.length());
              jarApp.readinessChecks.add(new JndiReadinessCheck(jndiName));
            } else {
              System.out.println("Unknown readiness check type " + readinessCheck);
              return Optional.empty();
            }

          } else {
            System.out.println("Unknown arg type " + arg);

            return Optional.empty();
          }

          break;

        case ARGS:
          if (arg.equals("--")) {
            jarApps.add(jarApp);
            jarApp = null;
            mode = Mode.JAR;
          } else {
            jarApp.args.add(arg);
          }

          break;
      }
    }

    if (jarApp != null) {
      jarApps.add(jarApp);
    }

    return jarApps.isEmpty() ? Optional.empty() : Optional.of(jarApps);
  }
}
