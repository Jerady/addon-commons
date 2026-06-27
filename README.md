# Add-on commons

A tiny framework for building **extensible applications** whose add-ons are delivered as external
jars. It is a thin convenience layer over the JDK
[`java.util.ServiceLoader`](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
SPI mechanism — see Oracle's guide on
[Creating Extensible Applications](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html).

You define a contract, third parties (or you) implement it in separate jars, and `addon-commons`
discovers and loads those implementations at runtime — either from the application classpath or from
a configurable folder of drop-in jars.

## Features

- Discover add-ons declared via the standard `META-INF/services` SPI contract.
- Load add-ons from **external jars** dropped into a configurable folder, or fall back to the
  current **classpath**.
- Type-safe lookup constrained to your own marker-derived interfaces.
- Zero runtime dependencies. Java 8+.

## Requirements

- Java 8 or newer (artifact is compiled for source/target 1.8).

## Installation

Coordinates: `de.jensd:addon-commons:16.0.0`

> **Note:** the artifact was historically published to Bintray, which has been shut down. Until a
> new public repository is in place, build and install it into your local Maven repository:
>
> ```bash
> ./gradlew publishToMavenLocal
> ```

Then depend on it:

```groovy
// Gradle
repositories { mavenLocal() }
dependencies { implementation 'de.jensd:addon-commons:16.0.0' }
```

```xml
<!-- Maven -->
<dependency>
    <groupId>de.jensd</groupId>
    <artifactId>addon-commons</artifactId>
    <version>16.0.0</version>
</dependency>
```

## Core concepts

The framework is three small types in `de.jensd.addon`:

| Type | Role |
|------|------|
| `AddOn` | Empty **marker interface**. Every pluggable contract must extend it so the registry can be type-constrained to `<TAddOn extends AddOn>`. |
| `AddOnRegistry` | Single-method lookup interface: `List<TAddOn> getAddOns(Class<TAddOn>)`. |
| `AddOnRegistryServiceLoader` | The implementation. Scans a folder for jars (or falls back to the classpath) and runs `ServiceLoader` to discover implementations. |

## Quick start

### 1. Define your add-on contract

Extend the `AddOn` marker interface so the registry can find it:

```java
import de.jensd.addon.AddOn;

public interface StringConverter extends AddOn {
    String convert(String value);
    String name();
}
```

### 2. Implement it

```java
public class DarkSideConverter implements StringConverter {
    @Override
    public String convert(String name) {
        return "Anakin Skywalker".equals(name) ? "Darth Vader" : "Unknown Sith Lord";
    }

    @Override
    public String name() {
        return "DarkSideConverter";
    }
}
```

### 3. Register the implementation (the SPI contract)

Create a provider-configuration file named after the **fully-qualified interface name** and list your
implementation FQNs, one per line:

```
src/main/resources/META-INF/services/com.example.StringConverter
```

```
com.example.DarkSideConverter
com.example.LightSideConverter
```

### 4. Look up add-ons at runtime

```java
import de.jensd.addon.AddOnRegistry;
import de.jensd.addon.AddOnRegistryServiceLoader;

AddOnRegistry registry = new AddOnRegistryServiceLoader();
List<StringConverter> converters = registry.getAddOns(StringConverter.class);

for (StringConverter c : converters) {
    System.out.println(c.name() + ": " + c.convert("Anakin Skywalker"));
}
```

## How add-ons are discovered

`AddOnRegistryServiceLoader` looks up implementations in one of two ways:

1. **External jars** — it scans a configurable folder for jar files, builds a `URLClassLoader` from
   them, and runs `ServiceLoader.load(...)` against it. Each jar must carry its own
   `META-INF/services/<interface-FQN>` provider file. This is how you ship add-ons as drop-in jars.
2. **Classpath fallback** — when no jars are present in the folder, discovery falls back to the
   current classpath, picking up any providers already on it.

## Configuration

Two runtime knobs are exposed as system properties (constants on `AddOnRegistryServiceLoader`):

| System property | Default | Meaning |
|-----------------|---------|---------|
| `de.jensd.addon.lookupPath` | `./addon/` | Folder scanned for add-on jars. |
| `de.jensd.addon.fileExtension` | `.jar` | File extension recognised as an add-on package. |

```bash
java -Dde.jensd.addon.lookupPath=/opt/myapp/plugins -jar myapp.jar
```

## Building & testing

```bash
./gradlew build              # compile + test + assemble jars (main, sources, javadoc)
./gradlew test               # run all tests
./gradlew test --tests AddonsRegistryTest                  # single test class
./gradlew publishToMavenLocal                              # install to local Maven repo
```

The Gradle wrapper is pinned to 7.3.3, which supports JDK 8–17 only. On a newer JDK the build fails
with `Unsupported class file major version …`. Pin the toolchain via a local (git-ignored)
`gradle.properties`:

```properties
org.gradle.java.home=/path/to/jdk-17
```

## Demo

A complete, runnable example lives in a companion project:
[**addon-commons-demo**](https://github.com/Jerady/addon-commons-demo). The `demo.converter` package
under this repo's `src/test` is the in-source reference implementation.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
