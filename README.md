# Flatmate [![build-badge][]][build]

[build]:                 https://circleci.com/gh/titanclass/flatmate
[build-badge]:           https://circleci.com/gh/titanclass/flatmate.svg?style=shield

## Overview

"Flatmate" runs one or more specified JAR files in the same JVM, each with different class loaders. This is done to reduce the memory usage of JVM-based applications that are deployed to the same machine.

In essence, Flatmate delivers similar benefits to common [OSGI](https://www.osgi.org/) usage around saving memory. Flatmate is also similar in the goals of [Landlord](https://github.com/landlord/landlord) around the sharing of memory. However, unlike OSGi, both Flatmate and Landlord share an important goal of being able to take two or more JVM applications and host them as one without change. Unlike Landlord and OSGi, Flatmate does not provide any dynamic loading/unloading behavior. Since developing Landlord, we concluded that the JVM needs to provide process-style isolation to better support it and OSGi; perhaps akin to [Isolates](https://en.wikipedia.org/wiki/Application_Isolation_API).

Flatmate is free from third-party dependencies and written in pure Java with the aim of limiting its memory footprint.

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

Flatmate includes a readiness feature that can wait to start applications based on the binding of a TCP socket. The following will only start the program contained in `bar.jar` once `foo.jar` binds to port 9870:

```bash
java -jar ~/work/farmco/roommate/target/flatmate-1.0.0-SNAPSHOT.jar \
  ~/work/farmco/lora-device-provisioner/backend/iox-sss/target/lora-device-provisioner-iox-sss-0.1.0-SNAPSHOT.jar -Dstreambed.http-server.bind.port=9870 -- -- \
  ~/work/farmco/lora-device-provisioner/backend/iox-sss/target/lora-device-provisioner-iox-sss-0.1.0-SNAPSHOT.jar -ready tcp://localhost:9870 -Dstreambed.http-server.bind.port=9871
```

### Building a JAR

```bash
sbt package
```
## Changelog

### 0.2.0 - 2019-03-18

* Add a readiness check feature to sequence application startup

### 0.1.1 - 2019-12-11

* Fix a bug causing stack overflow on startup

### 0.1.0 - 2018-12-10

* Initial release

## Contribution policy

Contributions via GitHub pull requests are gladly accepted from their original author. Along with
any pull requests, please state that the contribution is your original work and that you license
the work to the project under the project's open source license. Whether or not you state this
explicitly, by submitting any copyrighted material via pull request, email, or other means you
agree to license the material under the project's open source license and warrant that you have the
legal authority to do so.

## License

This code is open source software licensed under the
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0) license.

(c)opyright 2018, Titan Class P/L
