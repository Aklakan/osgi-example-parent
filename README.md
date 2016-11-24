# osgi-example-parent

A small working embedded OSGI example based on maven and apache felix.

The example comprises:
* An 'api' module, which provides a GreeterService
* Two implementation modules, with different implementations of the GreeterService.
* An 'application' operating one the greeter service. The app embeds an OSGI framework and dynamically loads, invokes and unloads the greeter services.

## Build
* Run `mvn clean install` to build the implementation bundles.
* Run the app's main class using `osgi-example-app` as the working directory.

## Lessons learnt in order to geth things working

* The implementations need to declare the scope of the api dependency as provided:
```xml
<dependency>
    <groupId>org.aksw.osgi-example</groupId>
    <artifactId>osgi-example-api</artifactId>
    <scope>provided</scope>
</dependency>
```
* In the app, it is necessary to set the api's version string for the FRAMEWORK_SYSTEMPACKAGES_EXTRA property - otherwise the service reference will always be null:
```java
config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, String.join(",",
	"org.aksw.osgi_example.api;version=\"1.0.0\""
));
```

