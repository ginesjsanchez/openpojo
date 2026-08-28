# GJS OpenPojo

POJO Testing &amp; Identity Management Made Trivial

A Java 21 port of [OpenPojo](https://github.com/oshoukry/openpojo) 0.9.2, by Osman Shoukry.

```xml
<dependency>
  <groupId>org.gjs.java.tools</groupId>
  <artifactId>openpojo</artifactId>
  <version>1.0.0-RC.2</version>
  <scope>test</scope>
</dependency>
```

Requires JDK 21.

The only mandatory dependency is `org.slf4j:slf4j-api`. openpojo ships **no logging
binding**: it uses whichever one your application has configured. If there is none,
SLF4J warns once and discards the messages.

The remaining integrations are optional and detected at runtime, so openpojo does not
drag them onto your classpath: JUnit 5 for assertions (without it, failures are plain
`AssertionError`, which every test framework reports), and ASM to generate subclasses
of abstract types.

#### Testing Example
```java
public class PojoTest {
  // Configured for expectation, so we know when a class gets added or removed.
  private static final int EXPECTED_CLASS_COUNT = 1;

  // The package to test
  private static final String POJO_PACKAGE = "com.openpojo.sample";

  @Test
  public void ensureExpectedPojoCount() {
    List <PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE,
                                                                   new FilterPackageInfo());
    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
  }

  @Test
  public void testPojoStructureAndBehavior() {
    Validator validator = ValidatorBuilder.create()
                            // Add Rules to validate structure for POJO_PACKAGE
                            // See com.openpojo.validation.rule.impl for more ...
                            .with(new GetterMustExistRule())
                            .with(new SetterMustExistRule())
                            // Add Testers to validate behaviour for POJO_PACKAGE
                            // See com.openpojo.validation.test.impl for more ...
                            .with(new SetterTester())
                            .with(new GetterTester())
                            .build();

    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
  }
}
```

#### Identity Management Example
```java
public class Person {
  @BusinessKey(caseSensitive = false)  //Configure your field(s)
  private String lastName;

  @Override
  public boolean equals(Object obj) {
    return BusinessIdentity.areEqual(this, obj);
  }

  @Override
  public int hashCode() {
    return BusinessIdentity.getHashCode(this);
  }

  @Override
  public String toString() {
      return BusinessIdentity.toString(this);
  }
}
```

More examples and tutorials in the [original project's wiki](https://github.com/oshoukry/openpojo/wiki).
