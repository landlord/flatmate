package au.com.titanclass.flatmate;

import java.io.IOException;
import java.io.InputStream;
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
  }

  public static void main(final String[] args) {
    final ClassLoader rootClassLoader = App.class.getClassLoader();

    final List<LoadedJarApp> entries = classLoadersWithMainClass(rootClassLoader, jarApps);

    for (final LoadedJarApp entry : entries) {
      final Thread jarThread =
          new Thread(
              () -> {
                try {
                  final Class<?> clazz = entry.classLoader.loadClass(entry.mainClass);
                  final Method mainMethod = clazz.getMethod("main", String[].class);
                  mainMethod.invoke(null, (Object) entry.jarApp.args);

                  // @TODO mechanism to rethrow exceptions below
                } catch (final IllegalAccessException e) {
                  System.exit(140);
                } catch (final NoSuchMethodException e) {
                  System.exit(141);
                } catch (final InvocationTargetException e) {
                  System.exit(142);
                } catch (final ClassNotFoundException e) {
                  System.exit(143);
                }
              });

      jarThread.setName(threadName(entry));
      jarThread.setContextClassLoader(entry.classLoader);
      jarThread.start();
    }
  }

  private static String threadName(LoadedJarApp jarApp) {
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
