# flatmate

## Overview

flatmate runs one or more specified JAR files in the same JVM, each with different class loaders.

This is done to reduce the memory usage of JVM-based applications that are deployed to the same machine.

Pro-tip: To further reduce memory usage, consider using [OpenJ9](https://www.eclipse.org/openj9/) and its class data sharing feature, particularly if the specified JARs have similar dependencies.

## Usage

```bash
java -jar flatmate.jar \
  --app foo.jar -Dsome.property=1 -- "first arg" "second arg" -- \
  --app bar.jar -Dother.property=2 -- "first arg" "second arg"
```

