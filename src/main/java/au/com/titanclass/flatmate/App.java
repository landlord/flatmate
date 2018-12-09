package au.com.titanclass.flatmate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class App {

  private static final List<JarApp> jarApps = new ArrayList<>();
  // @TODO parse args instead of hardcoded static block below
  static {
    jarApps.add(
        new JarApp(
            Paths.get(
                "/home/longshorej/work/farmco/lora-device-provisioner/backend/iox-sss/target/lora-device-provisioner-iox-sss-0.1.0-SNAPSHOT.jar"),
            new HashMap<>(),
            new String[] {}));

    jarApps.add(
        new JarApp(
            Paths.get(
                "/home/longshorej/work/farmco/testone/target/scala-2.12/testone-assembly-0.1.0-SNAPSHOT.jar"),
            new HashMap<>(),
            new String[] {}));

    jarApps.add(
        new JarApp(
            Paths.get(
                "/home/longshorej/work/farmco/testtwo/target/scala-2.12/testtwo-assembly-0.1.0-SNAPSHOT.jar"),
            new HashMap<>(),
            new String[] {}));
  }

  private static final int EXIT_ILLEGAL_ACCESS = 140;
  private static final int EXIT_NO_SUCH_METHOD = 141;
  private static final int EXIT_INVOCATION_TARGET = 142;
  private static final int EXIT_CLASS_NOT_FOUND = 143;

  public static void main(final String[] args) {
    final ClassLoader rootClassLoader = App.class.getClassLoader();

    final List<LoadedJarApp> entries = classLoadersWithMainClass(rootClassLoader, jarApps);

    Properties systemProperties = System.getProperties();

    ThreadGroupProperties threadGroupProperties = new ThreadGroupProperties(systemProperties);

    System.setProperties(threadGroupProperties);

    for (final LoadedJarApp entry : entries) {
      final ThreadGroup jarThreadGroup = new ThreadGroup(threadGroupName(entry));
      final Thread jarThread =
          new Thread(
              jarThreadGroup,
              () -> {
                final Properties props = new Properties(systemProperties);

                for (Map.Entry<String, String> propertyEntry : entry.jarApp.properties.entrySet()) {
                  props.put(propertyEntry.getKey(), propertyEntry.getValue());
                }

                threadGroupProperties.register(props);

                try {
                  final Class<?> clazz = entry.classLoader.loadClass(entry.mainClass);
                  final Method mainMethod = clazz.getMethod("main", String[].class);
                  mainMethod.invoke(null, (Object) entry.jarApp.args);
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

  private static String threadGroupName(LoadedJarApp jarApp) {
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
