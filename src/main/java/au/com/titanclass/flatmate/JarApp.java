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

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class JarApp {
  final Path path;
  final List<Map.Entry<String, String>> properties;
  final List<String> args;
  final List<ReadinessCheck> readinessChecks;

  JarApp(
      final Path path,
      final List<Map.Entry<String, String>> properties,
      final List<String> args,
      final List<ReadinessCheck> readinessChecks) {
    this.path = path;
    this.properties = properties;
    this.args = args;
    this.readinessChecks = readinessChecks;
  }

  @Override
  public String toString() {
    return "JarApp{"
        + "path="
        + path
        + ", properties="
        + properties
        + ", args="
        + args
        + ", readinessChecks="
        + readinessChecks
        + '}';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final JarApp jarApp = (JarApp) o;
    return Objects.equals(path, jarApp.path)
        && Objects.equals(properties, jarApp.properties)
        && Objects.equals(args, jarApp.args)
        && Objects.equals(readinessChecks, jarApp.readinessChecks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path, properties, args, readinessChecks);
  }
}
