# jfilter

Functional filtering library for POJOs.

Defines the base abstraction for *generic*, *composable* and *type-safe* filtering operations on POJOs.

## Maturity level

This is a POC: the API should be considered *alpha*, and may be subject to change.

## Changelog

### 0.2.0-SNAPSHOT

* Added some settings and metadata in the POM file.
* Implemented JDBC-level filtering (WHERE clause generation and values binding).

### 0.0.1

First release with a first formulation of the `Filter` concept and basic mechanics.

## Concepts

Conceptually, filtering some *data* can be seen as a predicate function:

```haskell
filter :: a -> Bool
```

This concept can be abstracted further by defining a matching function:

```haskell
match :: a -> b -> Bool
```

Where `a` is the *value of a filter* and `b` is *the type of the values to match against that filter value*.

In a Java context, we can model a filter as a partially applied function:

* Every filter is a statically typed class of objects that implements that `match` function.
* The `a` type is fixed by creating a specific `Filter` derived class, and the partial application of the `match` 
  function is done by passing an `a` value to the constructor.
* The resulting function `b -> Bool` can be applied to a `b` value directly by calling the `#match()` method of
  a `Filter` derived class instance, or by the `Predicate<B>` function obtained via the `#fn()` method of said 
  instance.
  
This way, we can write generic and type-safe filtering operations by *composing filters*.

## Usage

Some examples can be seen in the tests, but the gist is:

* Define a POJO/value object:

```java
public class Person {
  private final String name;
  private final String surname;
  private final int age;
  private final Sex sex;
  
  // ...
}
```

* Define some *filters* for this type, by extending the `Filter` base class:

```java
public class AgeFilter extends Filter<Person> {
  private final int age;

  public AgeFilter(int age) {
    super("age");
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getAge() == age;
  }
}
```

* Use all the filters defined this way to filter `Person` values by using the `Filters` utility class:

```java
List<Person> results = Filters.filter(AgeFilter.ageFilter(21), Arrays.asList(p1, p2));
// Or applying multiple filters at once:
List<Person> results = Filters.filter(
  Arrays.asList(ageFilter(25), sexFilter(Sex.FEMALE)),
  Arrays.asList(p1, p2, p3));
```

## Contribution guidelines

* This component is versioned according to [Semantic Versioning](http://semver.org/),
  using [binary compatibility](https://wiki.eclipse.org/Evolving_Java-based_APIs_2) to classify breaking changes.
* For development, use *SNAPSHOT* versions.
* Whenever a new version has to be released:
    1. Write a new changelog section in this file.
    2. Change the version in the *pom.xml* according to the versioning policy.
    3. Commit.
    3. Tag the git commit using a tag in this form: `vX.Y.Z`.
    4. Push everything: `git push origin/master`.

## API surface

The public API exposed by this library (and significant for the versioning policy) is:

* `me.manuelp.jfilter.Filter` as the base class of every filter.
* `me.manuelp.jfilter.Filters` with its generic filtering functions.