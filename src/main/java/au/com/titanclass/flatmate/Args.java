/*
 * Copyright (c) Titan Class P/L, 2018
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
