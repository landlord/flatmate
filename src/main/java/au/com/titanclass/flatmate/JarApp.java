package au.com.titanclass.flatmate;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

class JarApp {
  final Path path;
  final Map<String, String> properties;
  final String[] args;

  JarApp(final Path path, final Map<String, String> properties, final String[] args) {
    this.path = path;
    this.properties = properties;
    this.args = args;
  }

  @Override
  public String toString() {
    return "JarApp{" + "path=" + path + ", properties=" + properties + ", args=" + args + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JarApp jarApp = (JarApp) o;
    return Objects.equals(path, jarApp.path)
        && Objects.equals(properties, jarApp.properties)
        && Objects.equals(args, jarApp.args);
  }

  @Override
  public int hashCode() {

    return Objects.hash(path, properties, args);
  }
}
