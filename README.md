# Spring Static Autowire

Inject Spring beans into **static** fields of your utility classes in a safe and declarative way.

> Have you ever written a `Utils` class only to realise that you need a Spring managed bean inside it?  
> Static helpers are convenient, but Spring's dependency injection works on instance fields / constructor parameters only.  
> This tiny library fills that gap.

---

## Features

* Annotate any Spring bean (including the `@SpringBootApplication` class) with `@AutowireStatic(...)` to tell the framework **which utility class** and **which static fields** should be wired.
* Supports wiring **all mutable static fields** or a **white-list** of fields via `@AssignBean`.
* Optional `strict` mode – fail the application startup immediately instead of logging a warning if a bean cannot be wired.
* NEW: ability to disambiguate beans by **name** in addition to **type** (see the `beanName` attribute of `@AssignBean`).
* Zero runtime overhead after startup – the field is set once and that's it.

---

## Quick Start

### 1. Add dependency
#### gradle

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("me.byz:spring-static-autowire:<latest-version>")
}
```
#### maven
```xml
<dependency>
    <groupId>me.byz</groupId>
    <artifactId>spring-static-autowire</artifactId>
    <version>latest</version>
</dependency>
```

### 2. Enable the processor

Add `@EnableStaticInjection` to any `@Configuration` / `@SpringBootApplication` class.

```java
@SpringBootApplication
@EnableStaticInjection
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### 3. Describe the wiring

```java
@AutowireStatic(value = FooUtils.class, assignments = {
        @AssignBean(value = "fooService", beanType = FooService.class),
        @AssignBean(value = "barService", beanName = "specialBarService")
})
@Component
public class WiringDescriptor {
}
```

The processor will look for **static, non-final** fields inside `FooUtils` named `fooService` and `barService` and inject the corresponding Spring beans.

If the `assignments` array is **omitted**, _all_ suitable fields of the target utility class will be injected.

---

## Real-world Example

See the [`spring-static-autowire-test`](./spring-static-autowire-test) module for a fully working Spring Boot demo application.
Run it with `mvn spring-boot:run` and observe the logs.

---

## Limitations & Caveats

* **Static references live for the whole JVM lifecycle** – remember to avoid memory leaks, especially when running in servlet containers.
* Wiring happens **after all singletons are instantiated** (`SmartInitializingSingleton`), therefore accessing the static field **during bean construction** will still return `null`.
* If multiple beans of the same type exist and neither `@Primary` nor `beanName` are specified, Spring will throw a `NoUniqueBeanDefinitionException`.

---

## Future Work _(maybe)_

* Support for Bean `Qualifier`s and/or `@Profile`s.
* Optional reset mechanism on application shutdown (clear static fields).
* KSP/KAPT annotation processor that could generate wiring metadata at compile-time.

---

## 中文简介

`spring-static-autowire` 可以把 **Spring Bean** 注入到你的工具类的 **static 字段** 中。

1. 在任意 Spring Bean 上声明 `@AutowireStatic` 指明要注入的工具类／字段。
2. 使用 `@EnableStaticInjection` 启用功能。
3. 如需精确控制字段与 Bean 的对应关系，用 `@AssignBean` 指定字段名、类型，甚至是 Bean 名。

详细使用方式请参考英文部分与 `spring-static-autowire-test` 示例项目。

---

## License

MIT 