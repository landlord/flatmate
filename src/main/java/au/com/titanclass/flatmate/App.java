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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

public class App {
  private static final int EXIT_USAGE = 1;
  private static final int EXIT_ILLEGAL_ACCESS = 140;
  private static final int EXIT_NO_SUCH_METHOD = 141;
  private static final int EXIT_INVOCATION_TARGET = 142;
  private static final int EXIT_CLASS_NOT_FOUND = 143;
  private static final int EXIT_READINESS_CHECK_FAILED = 144;

  public static void main(final String[] args) {
    final Optional<List<JarApp>> maybeJarApps = Args.parse(args);

    if (!maybeJarApps.isPresent()) {
      System.out.println("usage: [<jar> [-ready <check>]... [-Dkey=value]... -- [<arg>]... --]...");
      System.exit(EXIT_USAGE);
      return;
    }

    final List<JarApp> jarApps = maybeJarApps.get();

    final ClassLoader rootClassLoader = App.class.getClassLoader();

    final List<LoadedJarApp> entries = classLoadersWithMainClass(rootClassLoader, jarApps);

    final Properties systemProperties = System.getProperties();

    final Map<Object, Object> systemPropertyMap =
        systemProperties
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final ThreadGroupProperties threadGroupProperties = new ThreadGroupProperties(systemProperties);

    System.setProperties(threadGroupProperties);

    for (final LoadedJarApp entry : entries) {
      for (final ReadinessCheck readinessCheck : entry.jarApp.readinessChecks) {
        try {
          readinessCheck.waitUntilReady();
        } catch (Exception e) {
          e.printStackTrace();

          System.exit(EXIT_READINESS_CHECK_FAILED);
        }
      }

      final ThreadGroup jarThreadGroup = new ThreadGroup(threadGroupName(entry));
      final Thread jarThread =
          new Thread(
              jarThreadGroup,
              () -> {
                final Properties props = new Properties();
                props.putAll(systemPropertyMap);

                for (Map.Entry<String, String> propertyEntry : entry.jarApp.properties) {
                  props.put(propertyEntry.getKey(), propertyEntry.getValue());
                }

                threadGroupProperties.register(props);

                try {
                  final Class<?> clazz = entry.classLoader.loadClass(entry.mainClass);
                  final Method mainMethod = clazz.getMethod("main", String[].class);
                  final String[] mainArgs = entry.jarApp.args.toArray(new String[] {});
                  mainMethod.invoke(null, (Object) mainArgs);
                } catch (final IllegalAccessException e) {
                  System.exit(EXIT_ILLEGAL_ACCESS);
                } catch (final NoSuchMethodException e) {
                  System.exit(EXIT_NO_SUCH_METHOD);
                } catch (final InvocationTargetException e) {
                  System.exit(EXIT_INVOCATION_TARGET);
                } catch (final ClassNotFoundException e) {
                  System.exit(EXIT_CLASS_NOT_FOUND);
                }

                // other exceptions simply cause the "main" thread to die.
                // we don't want to exit as the JVM doesn't exit
                // in that case either (assuming other threads running)
              });

      jarThread.setName(jarThreadGroup.getName() + "-main");
      jarThread.setContextClassLoader(entry.classLoader);
      jarThread.start();
    }
  }

  private static String threadGroupName(final LoadedJarApp jarApp) {
    return "flatmate-"
        + jarApp.id
        + "-"
        + jarApp.jarApp.path.getFileName().toString().replaceAll("[^A-Za-z0-9]", "-").toLowerCase();
  }

  private static List<LoadedJarApp> classLoadersWithMainClass(
      final ClassLoader rootClassLoader, final List<JarApp> jarApps) {
    final List<LoadedJarApp> entries = new ArrayList<>();

    for (final JarApp jarApp : jarApps) {
      try {
        final Manifest manifest = new JarFile(jarApp.path.toString()).getManifest();

        final String mainClassName = manifest.getMainAttributes().getValue("Main-Class");

        final ClassLoader jarClassLoader =
            URLClassLoader.newInstance(new URL[] {jarApp.path.toUri().toURL()}, rootClassLoader);

        if (mainClassName == null) {
          throw new IllegalStateException("Cannot find Main-Class for jar: " + jarApp.path);
        }

        entries.add(new LoadedJarApp(entries.size(), jarApp, jarClassLoader, mainClassName));
      } catch (IOException e) {
        throw new IllegalStateException("Cannot load manifest for jar: " + jarApp.path, e);
      }
    }

    return entries;
  }
}
