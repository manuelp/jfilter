# siftj

Functional filtering library for POJOs and SQL queries.

Defines the base abstraction for *generic*, *composable* and *type-safe* filtering operations. This operations can be
used to filter POJOs/value objects, or to manage JDBC filters (WHERE clauses and parameters binding).

## Maturity level

This is a POC: the API should be considered *alpha*, and may be subject to change.

Right now, we have a small number of primitives and functions that rely on [TotallyLazy](http://totallylazy.com/) for
the most generic ones (callables, predicates, etc).

## Changelog

### 0.4.0-SNAPSHOT

* Switched to [Functional Java](http://www.functionaljava.org/).
* `Filter<T>` is now a simple `F<T, Boolean>`.
* `Filters#compose()` has been renamed to `Filters#and()`.

### 0.3.0

* `Filter<T>` is now both a `Predicate<T>` and a `Callable1<T, Boolean>`.
* Implemented an AND composition of `Filter<T>` in `Filters.compose(Filter<T>... filters)`.
* Simplified `Filters` functions implementation by using the new `#compose()` expression.

### 0.2.0

* Added some settings and metadata in the POM file.
* Implemented JDBC-level filtering (WHERE clause generation and values binding).

### 0.0.1

First release with a first formulation of the `Filter` concept and basic mechanics.

## Concepts

### Value objects

Conceptually, filtering some *data* can be seen as a predicate function:

```haskell
filter :: a -> Bool
```

This concept can be abstracted further by defining a matching function:

```haskell
matches :: a -> b -> Bool
```

Where `a` is the *value of a filter* and `b` is *the type of the values to match against that filter value*.

In a Java context, we can model a filter as a partially applied function:

* Every filter is a statically typed class of objects that implements that `matches` function.
* The `a` type is fixed by creating a specific `Filter` derived class, and the partial application of the `matches` 
  function is done by passing an `a` value to the constructor.
* The resulting function `b -> Bool` can be applied to a `b` value directly by calling the `Predicate#matches()` method of
  a `Filter` derived class instance.
  
Actually, using TotallyLazy a `Filter<T>` is a `Predicate<T>` and a `Callable1<T, Boolean>`.
  
This way, we can write generic and type-safe filtering operations by *composing filters*.

### JDBC filters

A filter in a JDBC context can be viewed as a couple of related operations:

* WHERE clause string generation
* Parameters binding

*siftj* defined an interface that specifies this contract: `SqlFilter`. Being an interface, there are 
a couple of considerations to make:

1. Every object can be a `SqlFilter`, even a `Filter` implementation. In the last case, both POJOs and JDBC filtering 
   can be implemented in the same class (this is not to say that it's the right thing to do or the suggested way to use 
   this interface).
2. There is no prescription on how a `SqlFilter` type has to be instantiated: we cannot possibly predict all the use
   cases (JOINs w/ multiple table references, columns renames, etc). So **you have a lot of flexibility 
   (and responsibility) to correctly *design* a viable clean and maintainable querying strategy**.

## Usage

Some examples can be seen in the tests, but the gist is as follows.

### Value objects

Define a POJO/value object:

```java
public class Person {
  private final String name;
  private final String surname;
  private final int age;
  private final Sex sex;
  
  // ...
}
```

Define some *filters* for this type, by extending the `Filter` base class:

```java
public class AgeFilter extends Filter<Person> {
  private final int age;

  public AgeFilter(int age) {
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean matches(Person p) {
    return p.getAge() == age;
  }
}
```

Use all the filters defined this way to filter `Person` values by using the `Filters` utility class. We can apply a 
single filter to a list of compatible values:

```java
List<Person> results = Filters.filter(AgeFilter.ageFilter(21), Arrays.asList(p1, p2));
```

We can also apply a *list of filters* to a list of compatible values:

```java
List<Person> results = Filters.filter(
  Arrays.asList(ageFilter(25), sexFilter(Sex.FEMALE)),
  Arrays.asList(p1, p2, p3));
```

We can even *chain filters together in AND*:

```java
Filter<Person> compFilter = Filters.compose(ageFilter(25), sexFilter(Sex.FEMALE));
```

### JDBC filtering

Write a `SqlFilter` class. For example:

```java
public class SqlNameFilter implements SqlFilter {
  private String tableRef;
  private final String name;

  public SqlNameFilter(String tableRef, String name) {
    this.tableRef = tableRef;
    this.name = name;
  }

  @Override
  public String whereClause() {
    return tableRef + "." + name + "=?";
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setString(index, name);
  }
}
```

Then you can use this filter to obtain a WHERE clause:

```java
SqlFilter f = new SqlNameFilter("p", "name");
String c = f.whereClause();
// c = "p.name=?"
```

And to bind the parameter in a full JDBC query:

```java
f.bindParameters(statement, 4);
// statement.setString(4, "name");
```

A more complex example:

```java
public class SqlPotentialFriendFilter implements SqlFilter {
  private final Range range;
  private final Sex sex;
  private String tableRef;

  public SqlPotentialFriendFilter(Range range, Sex sex, String tableRef) {
    NotNull.check(range, tableRef);
    this.range = range;
    this.sex = sex;
    this.tableRef = tableRef;
  }

  @Override
  public String whereClause() {
    return String.format("(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef,
      tableRef);
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setInt(index, range.getFrom());
    statement.setInt(index + 1, range.getTo());
    statement.setString(index + 2, sex.name());
  }
}
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

* `me.manuelp.siftj.Filter` as the base class of every filter.
* `me.manuelp.siftj.Filters` with its generic filtering functions.
* `me.manuelp.siftj.sql.SqlFilter` as the contract of a SQL filter.