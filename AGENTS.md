# Repository Guidelines

## Project Structure & Module Organization

This repository contains a small Java library for injecting Spring beans into static fields. Core library code lives in `src/main/java/me/byz/spring`, with annotations and the Spring processor kept in the same package. The root Gradle build (`build.gradle.kts`) publishes the library artifact `me.byz:spring-static-autowire`. The `spring-static-autowire-test` directory is a separate Maven/Spring Boot sample app used to exercise the published library behavior. Gradle version catalogs are in `gradle/libs.versions.toml`; generated build output belongs under `build/` and should not be committed.

## Build, Test, and Development Commands

- `./gradlew build` compiles the Java 8 library, creates classes, Javadoc, sources, and publication artifacts.
- `./gradlew test` runs Gradle tests if test sources are added later.
- `./gradlew publishToMavenLocal` installs the library into the local Maven cache for sample-app validation.
- `cd spring-static-autowire-test && mvn spring-boot:run` runs the Spring Boot demo after a compatible local artifact is available.
- `./gradlew javadoc` checks generated API documentation.

## Coding Style & Naming Conventions

Use Java 8-compatible code and the existing package prefix `me.byz.spring`. Keep public annotations and processor classes small, explicit, and documented when behavior is not obvious. Use 4-space indentation, braces on the same line, and descriptive camelCase names for methods, fields, and annotation attributes. Types use PascalCase. Prefer native Java and Spring APIs already present in the project over new dependencies.

## Testing Guidelines

There is currently no dedicated `src/test/java` suite in the root library. For behavior changes, add focused tests where practical, or validate with the Maven sample app. Name tests after the behavior under test, for example `StaticAutowireProcessorTest`. When touching bean resolution or strict-mode behavior, include cases for missing beans, multiple beans, `beanName`, and assignment filtering.

## Commit & Pull Request Guidelines

Recent history includes conventional-style commits such as `feat(core annotations): ...` and `deploy(github publish): ...`. Use `type(scope): description` in English, for example `fix(processor): handle named bean lookup`. Pull requests should include a concise summary, validation commands run, linked issues when applicable, and notes for release or publishing changes. Include screenshots only for documentation or workflow UI changes.

## Security & Configuration Tips

Do not commit signing keys, Maven Central credentials, GitHub tokens, or local environment files. Keep release credentials in CI secrets or local environment variables, and avoid printing secrets in logs, comments, or examples.
