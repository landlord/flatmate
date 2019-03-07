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
