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
    Mode mode = Mode.JAR;
    final List<JarApp> jarApps = new ArrayList<>();
    JarApp jarApp = null;

    for (final String arg : args) {
      switch (mode) {
        case JAR:
          jarApp = new JarApp(Paths.get(arg), new ArrayList<>(), new ArrayList<>());
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
          } else {
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
