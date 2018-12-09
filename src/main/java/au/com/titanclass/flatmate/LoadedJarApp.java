/*
 * Copyright (c) Titan Class P/L, 2018
 */

package au.com.titanclass.flatmate;

import java.util.Objects;

class LoadedJarApp {
  final int id;
  final JarApp jarApp;
  final ClassLoader classLoader;
  final String mainClass;

  LoadedJarApp(
      final int id, final JarApp jarApp, final ClassLoader classLoader, final String mainClass) {
    this.id = id;
    this.jarApp = jarApp;
    this.classLoader = classLoader;
    this.mainClass = mainClass;
  }

  @Override
  public String toString() {
    return "LoadedJarApp{"
        + "id="
        + id
        + ", jarApp="
        + jarApp
        + ", classLoader="
        + classLoader
        + ", mainClass='"
        + mainClass
        + '\''
        + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final LoadedJarApp that = (LoadedJarApp) o;
    return id == that.id
        && Objects.equals(jarApp, that.jarApp)
        && Objects.equals(classLoader, that.classLoader)
        && Objects.equals(mainClass, that.mainClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, jarApp, classLoader, mainClass);
  }
}
