# Changelog

All major changes are referenced in this file

[Added|Changed|Deprecated|Removed|Fixed|Security]


## Initial
This project is an update to Java 21 from version 0.9.2-SNAPSHOT as it was on 04/12/2025.
 This project was born out of a need to validate all POJOs (Plain Old Java Object) are behaving correctly.


## 1.0.0

A modernization pass over the whole library: dependency contract, logging, generics,
formatting, documentation and build.

### The dependency contract

openpojo is consumed as a test library, so it should impose as little as possible on the
application that uses it. The tree a consumer now sees has exactly **one** compile-scope
entry of our own:

```
org.gjs.java.tools:openpojo
+- org.slf4j:slf4j-api                 compile      <- the only mandatory dependency
+- org.junit.jupiter:junit-jupiter-api provided, optional
+- org.ow2.asm:asm                     provided, optional
```

[Changed]
- **Logging is now plain SLF4J.** openpojo logs through `org.slf4j.Logger` and the
  application supplies the implementation. `logback-classic` moves to `test` scope,
  overriding the `compile` scope `gjs-java-base-parent` injects, so openpojo no longer
  forces a binding on anyone.
  - Behaviour change: with no logging library at all, openpojo used to fall back on
    `java.util.logging` and write to the console. With no binding, SLF4J now warns once
    and discards the messages (NOP), which is what any library does.
  - The 21 log messages move from the `java.text.MessageFormat` style (`{0}`, `{1}`) to
    the positional SLF4J one (`{}`). None of them repeated an index or had them out of
    order.
  - In `DefaultPojoClassLookupService` the `Throwable` now goes last with no placeholder
    of its own, which is how SLF4J records the stack trace.
- **TestNG is no longer part of the openpojo API.** `TestNGAssertAffirmation` is gone and
  TestNG drops out of the `AffirmationFactory` chain, which is now JUnit 5 and, failing
  that, `JavaAssertionAffirmation`.
  - Verified that TestNG users lose nothing: TestNG's own `org.testng.Assert.fail` throws
    a plain `java.lang.AssertionError`, and what openpojo throws now is
    `org.opentest4j.AssertionFailedError`, which **extends** `AssertionError`. TestNG
    reports it as a failure just the same.
  - `testng` drops from `provided` + `optional` to `test`. It is still needed, but only
    for the `ATestNGClassEndsWithTest` fixture, because `TestClassMustBeProperlyNamedRule`
    recognises test classes by their annotations and needs the real one. The rule itself
    resolves annotations by name, so openpojo still recognises TestNG test classes
    without depending on TestNG.
- Optional integrations (SLF4J, JUnit 5, ASM) are all `provided` + `optional`. JUnit is
  no longer forced onto the consumer's classpath.
- Versions already managed by `gjs-java-base-parent` and `gjs-parent` (commons-io,
  commons-lang3, junit, mockito, slf4j, surefire, jacoco) are no longer redeclared here;
  several of them were silently pinning older versions.

[Removed]
- The whole `com.openpojo.log` package (`Logger`, `LoggerFactory`, `SLF4JLogger`,
  `Log4JLogger`, `JavaLogger`, `MessageFormatter`): a home-grown facade auto-detecting
  three backends, from a time when SLF4J was not yet universal. `Log4JLogger` compiled
  against the Log4j 1.x API, dead since 2015. The six uses of `MessageFormatter`
  unrelated to logging (exception and assertion messages) become string concatenation,
  which was already the style of the rest of the code.
  - `reload4j` and `slf4j-reload4j` go with it, and on the test side the capture
    infrastructure built on `org.apache.log4j.Appender` and `java.util.logging.Handler`
    (`LogHelper`, `EventLogger`, `MockAppender*`, `LogEvent`) plus the tests of the three
    backends. `SpyAppender` is rewritten on the logback `ListAppender`.
- `commons-io` and `commons-lang3` are no longer mandatory dependencies.
  `JavaClassPathClassLoader` was their only consumer: `FilenameUtils.separatorsToUnix`
  becomes a plain separator replace and `StringUtils.isEmpty` a private method.
  `commons-lang3` stays at `test` scope, where it is still used.
- `mockito-core` and `mockito-junit-jupiter`: not referenced anywhere, and declared with
  `compile` scope.
- `log4j-core` 2.x: never referenced from the code.
- `coveralls-maven-plugin`, plus the Travis and Coveralls badges in the README: dead
  infrastructure pointing at the original repository.
- The commented-out JBoss `<repositories>` block.

### Package discovery

[Removed]
- `JavaClassPathClassLoader` and the `...packageloader.env` package. It scanned
  `java.class.path` by hand to complement `ClassLoader.getResources`, but it was broken:
  it treated the whole property value as a single path and kept whatever followed the
  last `:`, which on Windows is the drive separator. In practice it scanned **whichever
  jar happened to be last on the classpath** (here, `lombok.jar`), so `Package.isValid()`
  could depend on the order Maven assembled the classpath in.
  - Before removing it, `ClassLoader.getResources` was verified to cover the same ground:
    of the **404 packages** held by the 27 jars on the test classpath, it sees **404**.
    The 3 it cannot enumerate (`ch.qos.logback.classic.servlet` and two more) fail in
    `Class.forName` because of absent optional dependencies, not in discovery; the manual
    scanner used the same `Helper` and failed identically.
  - `Package` now rests on `getResources` alone, which also follows the real loading
    context rather than the declared classpath (fat jars, module path, custom loaders).
  - The end-to-end coverage that lived in `JavaClassPathClassLoaderTest` (finding classes
    and sub-packages inside a jar, and scanning the root package) moves to `PackageTest`,
    where it matters more now that there is no fallback mechanism.

### Generics and language level

[Changed]
- **The generator SPI no longer uses raw types.** `src/main` goes from **86 `rawtypes`
  warnings to 0**. `BaseCollectionRandomGenerator` and `BaseMapRandomGenerator` (and
  their ~40 concrete generators) now declare `Collection<Object>` and
  `Map<Object, Object>`, which is the honest type: these are freshly created collections
  the library itself fills with arbitrary objects.
  - **Binary compatibility is intact**, verified with `javap`: the descriptors are still
    `(Ljava/lang/Class;)Ljava/util/Collection;` and `(Ljava/lang/Class;)Ljava/util/Map;`,
    the very same ones the raw version produced. Generics live only in the `Signature`
    attribute.
  - The unchecked conversions are concentrated in two places,
    `CollectionHelper.asCollection` and `MapHelper.asMap`, instead of being spread
    around. They are needed because a few types cannot take `Object` as their element:
    `EnumSet` pins its own, `DelayQueue` bounds it to `Delayed`, `EnumMap` bounds the key
    to the enum, and the instances coming from `InstanceFactory` arrive as `Object`.
  - `CollectionHelper.addElement` is gone; it was a `@SuppressWarnings` patch only ever
    called from the helper itself, and `buildCollections` can now add directly.
  - `Resolver` goes from three `@SuppressWarnings` to one, on the `instanceof` dispatch,
    which is where the lack of checking is real.
  - Overall, `@SuppressWarnings("unchecked")` in `src/main` drops from **18 to 13**.
  - Outside the SPI: `TypeVariableResolver` uses `TypeVariable<?>`,
    `SerializableComparableObject implements Comparable<Object>`, and
    `DefaultRandomGeneratorService.getAppropriateRandomGenerator` takes
    `Map<Class<?>, ? extends RandomGenerator>`, which removes three casts and one
    suppression.
  - The ~93 `rawtypes` warnings left in `src/test` are deliberate: the fixtures that
    exercise generic resolution need raw types (see `AClassWithExhaustiveCollection`
    under Tests).
- `com.openpojo.reflection.java.Java` is no longer an `interface` used as a constant
  holder; it is a `final` class with a private constructor. It was already listed in
  `StructuralTest.NON_INSTANTIABLES`, so the change lines the type up with what its own
  structural test already expected. Nobody used it as a type, only `Java.CONSTANT`.
- `serialVersionUID` added to the four serializable classes that lacked it
  (`ASMNotLoadedException`, `SomeRole`, `SomeRoleUnresolved`,
  `RandomReturnInvocationHandler`), using the default value `serialver` computes so that
  already serialized forms stay compatible. The library itself ships
  `SerializableMustHaveSerialVersionUIDRule`.
- Diamond operator in 81 constructions across `src/main`. The
  `new ThreadLocal<Set<Type>>() { ... }` in `GeneratedRandomValues` is left alone, being
  an anonymous class.
- `MessageFormatter` keeps its `StringBuffer`:
  `MessageFormat.format(Object[], StringBuffer, FieldPosition)` is the only overload
  taking a destination, and it still demands `StringBuffer` in Java 21.
- Compiler warnings turned on: `gjs.compiler.verbose=true`, `gjs.compiler.lint=all`. The
  parent switches both off by default, so the warnings that guided this work were
  invisible.

### Documentation

[Changed]
- **Javadoc complete, with `doclint=all`: from 485 warnings to 0.** Class descriptions
  added to the 160 types that only had `@author oshoukry`, and the ~300 members with no
  comment at all are now documented: methods, fields, constants and type parameters. The
  build now demands both that the javadoc be *correct* (syntax, HTML, references,
  `@param`/`@return` matching the signature) and *complete*, so it cannot silently decay.
  - In the generator family the description documents what the name does not give away:
    **which concrete implementation each one returns** (`QueueRandomGenerator` yields a
    `ConcurrentLinkedQueue`, `DequeRandomGenerator` a `LinkedList`,
    `SortedMapRandomGenerator` a `TreeMap`, and so on).
  - 58 explicit, documented no-argument constructors added to the classes that only had
    the implicit one. Not linter appeasement: these are the classes users instantiate
    (`new GetterMustExistRule()`, `new SetterTester()`, `new FilterEnum()`). The original
    access is preserved, public on concrete classes and protected on abstract ones, so
    nothing changes for callers.
  - `SomeEnum` is the one place where the documentation is unavoidably repetitive: its 64
    constants are interchangeable by design (picked with `values()[random]`) and carry no
    meaning of their own. The type documentation says exactly that.
  - Note on the count: the build reported "100 warnings" because **that is javadoc's
    default cap**, not the number of problems. Raised with `-Xmaxwarns`, the real figure
    was **485**.
- All javadoc, implementation comments and test assertion messages are in English,
  matching the original project.

### Formatting and build

[Added]
- `config/eclipse-formatter.xml` with the project style: **tabs and 120 columns**, which
  is what the project's Eclipse already produced and what `code.line.max.length` in
  `gjs-parent` says. The `formatter.file` property points at it, so
  `mvn formatter:format` works with no arguments; the parent leaves `${formatter.file}`
  empty and expects it on the command line.
  - The profile leaves comments unformatted (so licence headers and the inherited javadoc
    are not reflowed), respects existing line breaks that already fit, and honours the
    `// @formatter:off` markers.
- `.gitattributes`, so line endings stop depending on each machine's `core.autocrlf`, and
  `.editorconfig`.
- `Automatic-Module-Name: com.openpojo` in the jar manifest.

[Changed]
- **Formatting unified across all 741 files.** The tree mixed tabs and spaces (45/218 in
  `src/main`, 124/375 in `src/test`) because reformatting was being done by hand.
  - Verified that the change is **whitespace only**: comparing all 741 files with every
    whitespace character stripped gives zero token differences.
- `spotless-maven-plugin` is declared to pin the import group order (`java` / third party
  / `com.openpojo` / static), which spotless's default `importOrder` would flatten into a
  single alphabetical list. Like `formatter:format` it is run by hand
  (`mvn spotless:apply`); the execution inherited from the parent is set to
  `<phase>none</phase>` so neither tool rewrites sources on every build. It touched 128
  files, all of them only on `import` lines, including several commented-out
  `//import org.junit....` leftovers from JUnit 4.
- The `openpojo-release` profile is gone: it duplicated by hand what the `attach-sources`
  and `attach-javadoc` profiles of `gjs-java-base-parent` already do.
  `maven-release-plugin` now invokes those directly (`-Pattach-sources,attach-javadoc`)
  instead of the old `-Psonatype-oss-release,openpojo-release`, where the first did not
  exist anywhere in the parent chain and the second was redundant.

### Tests

[Changed]
- **openpojo's own tests no longer assert with TestNG.** 43 calls to `org.testng.Assert`
  across 10 files move to JUnit 5 `Assertions`. They were JUnit tests asserting with
  TestNG, inherited inconsistency.
  - The argument order was deliberately **not** touched, and that fixes the messages:
    they were written as `(expected, actual)`, JUnit's order, while TestNG reads them as
    `(actual, expected)`, so until now a failing assertion reported the two values the
    wrong way round.
  - `issues/issue81` used `org.testng.Assert.class` merely as "some class that lives in a
    jar" so it could scan its package; it now uses the JUnit one.

[Fixed]
- `LoggingTesterTest` and `issues/issue112/IssueTest` had **every** assertion commented
  out: they set up the log capture and checked nothing. Rewritten on logback, with the
  assertions restored and live.
- `CollectionHelperTest` was in the same state: it checked neither that
  `buildCollections` returns the same instance untouched when the type is `null`, nor
  that it returns `null` when the collection is.
- `ServiceRegistrarTest` listed `org.springframework.security.crypto.password.PasswordEncoder`
  among the types the registry is expected to hold, but openpojo registers nothing for it
  and there is no reference to Spring anywhere in `src/main`. The setup swallowed the
  resulting `ClassNotFoundException` and dropped the entry, so the count assertion
  happened to balance, which meant the test depended on Spring Security being **absent**:
  had it ever reached the test classpath, the expected count would have grown by one and
  the test would have failed pointing at a problem that does not exist. Stale entry
  removed.
- `JARPackageLoaderTest` compared against a fixed class count from another library
  (reload4j), and its verification loop walked the list it had found rather than the one
  it expected, making it a tautology. It now asserts by containment against a jar on the
  test classpath.
- `AClassWithExhaustiveCollection`: the raw types (`Collection`, `Collection<Collection>`)
  in the fixture are restored. They are deliberate, separating the "undefined" case from
  the "unbounded" one, and genericizing them broke
  `CollectionRandomGeneratorTest.exhaustiveTest` and
  `GenericCollectionMultiThreadedTest.shouldCreateGenericCollection`.

### Fixed in the parent, not here

[Fixed]
- The profiles that attach the sources and javadoc jars never ran in **any** gjs Java
  module: the `pluginManagement` of `gjs-java-base-parent` injected the `javadoc:fix` and
  `javadoc:test-fix` goals into the `attach-javadocs` execution. Those goals exist up to
  maven-javadoc-plugin 3.11.2 and were removed in 3.12.0, the version `gjs-parent` pins,
  so every child declaring the plugin failed with "Could not find goal 'fix'". Fixed in
  the parent; openpojo now produces all three jars.
