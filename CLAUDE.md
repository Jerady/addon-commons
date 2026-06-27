# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

`addon-commons` is a tiny framework (group `de.jensd`, current `16.0.0`) for building extensible
applications whose add-ons are delivered as external jars. It is a thin convenience layer over the
JDK `java.util.ServiceLoader` SPI mechanism. The published artifact is the main source set only; the
`demo.converter` package lives under `src/test` purely as a usage example.

## Build & Test

```bash
./gradlew build              # compile + test + assemble jars (main, sources, javadoc)
./gradlew test               # run all tests
./gradlew test --tests AddonsRegistryTest               # single test class
./gradlew test --tests 'AddonsRegistryTest.testConverter'  # single test method
./gradlew publishToMavenLocal   # publish to local Maven repo
```

Java source/target is 1.8. Tests use JUnit 5 (Jupiter). Gradle wrapper is 7.3.3.

The `build.gradle` `test` task enables `useJUnitPlatform()` — this is **required** for Jupiter
tests to run. Without it Gradle defaults to the JUnit 4 engine, silently discovers zero tests, and
still reports `BUILD SUCCESSFUL` (a green build that ran nothing). If a future change drops this
line, all tests stop executing without any failure being reported.

### JDK requirement

Gradle 7.3.3 supports JDK 8–17 only. Running on a newer JDK (e.g. 21) fails the build with
`Unsupported class file major version 65`. To pin the toolchain, create a local (git-ignored)
`gradle.properties` with `org.gradle.java.home` pointing at a JDK 17, e.g.:

```properties
org.gradle.java.home=/Library/Java/JavaVirtualMachines/liberica-jdk-17-full.jdk/Contents/Home
```

The path is machine-specific (`/usr/libexec/java_home -V` lists installed JDKs on macOS).
Alternatively, run with `JAVA_HOME` set to a JDK 17 for a single invocation:
`JAVA_HOME=<jdk17> ./gradlew test`.

## Architecture

Three production types in `src/main/java/de/jensd/addon`:

- **`AddOn`** — empty marker interface. Every pluggable contract (e.g. the test's `StringConverter`)
  must extend it so the registry can be type-constrained to `<TAddOn extends AddOn>`.
- **`AddOnRegistry`** — single-method lookup interface: `getAddOns(Class<TAddOn>)`.
- **`AddOnRegistryServiceLoader`** — the implementation. It does two distinct things:
  1. Scans a configurable folder for jar files, builds a `URLClassLoader` from them, and runs
     `ServiceLoader.load(addOnClass, classLoader)` to discover implementations declared in each jar's
     `META-INF/services/<fully-qualified-interface-name>`.
  2. Falls back to the current classpath when no jars are present (this is how the tests find the
     in-source demo converters via `src/test/resources/META-INF/services/...`).

### Configuration (system properties)

The lookup folder and jar extension are overridable at runtime — see the constants on
`AddOnRegistryServiceLoader`:

- `de.jensd.addon.lookupPath` (default `./addon/`)
- `de.jensd.addon.fileExtension` (default `.jar`)

Tests set `lookupPath` to `build/resources/test` to exercise jar discovery.

## How add-ons are wired (the SPI contract)

To make an implementation discoverable, two things are required:
1. The implementation implements an interface that extends `AddOn`.
2. A provider-configuration file at `META-INF/services/<interface-FQN>` lists the implementation
   FQNs, one per line.

The `demo.converter` package under `src/test` is the canonical example: `StringConverter extends AddOn`,
with `DarkSideConverter` / `LightSideConverter` / `CastConverter` registered in
`src/test/resources/META-INF/services/de.jensd.addon.demo.converter.StringConverter`.

## Notes

- Discovery progress is reported via `System.out.println` inside `AddOnRegistryServiceLoader` — there is
  no logging framework (log4j was intentionally removed).
- A companion external demo project lives at https://github.com/Jerady/addon-commons-demo.
