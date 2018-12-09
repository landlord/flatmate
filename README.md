*** EXPERIMENTAL ***

# flatmate

## Overview

flatmate runs one or more specified JAR files in the same JVM, each with different class loaders.

This is done to reduce the memory usage of JVM-based applications that are deployed to the same machine.

Pro-tip: To further reduce memory usage, consider using [OpenJ9](https://www.eclipse.org/openj9/) and its class data sharing feature, particularly if the specified JARs have similar dependencies.

## Usage

```bash
java -jar flatmate.jar \
  foo.jar -Dsome.property=1 -- "first arg" "second arg" -- \
  bar.jar -Dother.property=2 -- "first arg" "second arg"
```

### Example

This starts the same application twice, with a different set of properties to change the port:

```bash
java -jar ~/work/farmco/roommate/target/flatmate-1.0.0-SNAPSHOT.jar \
  ~/work/farmco/lora-device-provisioner/backend/iox-sss/target/lora-device-provisioner-iox-sss-0.1.0-SNAPSHOT.jar -Dstreambed.http-server.bind.port=9870 -- -- \
  ~/work/farmco/lora-device-provisioner/backend/iox-sss/target/lora-device-provisioner-iox-sss-0.1.0-SNAPSHOT.jar -Dstreambed.http-server.bind.port=9871
```

which yields:

```
[INFO] [12/08/2018 18:58:22.720] [ForkJoinPool.commonPool-worker-2] [DeviceProvisionerServer(akka://deviceprovisioner)] Server listening on /127.0.0.1:9870
[INFO] [12/08/2018 18:58:22.720] [ForkJoinPool.commonPool-worker-1] [DeviceProvisionerServer(akka://deviceprovisioner)] Server listening on /127.0.0.1:9871
```

### Building a JAR

```bash
sbt package
```

## Releasing

(TODO)

(c)opyright 2018, Titan Class P/L
