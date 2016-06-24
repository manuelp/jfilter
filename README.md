# siftj

Functional filtering library for POJOs and SQL queries.

Defines the base abstraction for *generic*, *composable* and *type-safe* filtering operations. This operations can be
used to filter POJOs/value objects, or to manage JDBC filters (WHERE clauses and parameters binding).

## Maturity level

This library has been used in production for some time, however the API should stille be considered *beta*, and may be subject to change.

Right now, we have a small number of primitives and functions that rely on [Functional Java](http://www.functionaljava.org/) for the most generic ones (callables, predicates, etc).

## Changelog

### 2.0.2

#### Fixes
* Added identity `SqlFilter`: `IdentitySqlFilter`.
* Fixed `SqlFilters` combinators (and, or) to work with both empty and one element input filters lists.
* Added constat value `Filter`: `NonFilter`.
* Fixed `Filters` combinators (and, or) to work with both empty and one element input filters lists.

### 2.0.1

* Made combinators in `SqlFilters` parametric on `SqlFilter` implementations.

### 2.0.0

* Moved to [Functional Java](http://www.functionaljava.org/) from [TotallyLazy](http://totallylazy.com/), 
  all the library revolved around functional constructs so this is a pretty breaking change.
* Added predicate `WhereClause#isEmpty()`.
* Exposed as public API `SqlFilters#bindParamsId()`.
* Constructors of `WhereClause` and `ParamIndex` is now private, the static factory methods must be used instead.
* Updated and wrote additional Javadoc comments.

### 1.0.0

* Added `Filters#or()` composing operator.
* Refactored `SqlFilters` to expose generic `#and()` and `#or()` operators as `SqlFilter` combinators.
* Renamed method: `SqlFilter#bindParameter()` -> `SqlFilter#bindParameters()`.

### 0.4.1

* Renamed `SqlFiltering` to `SqlFilters`.

### 0.4.0

* `Filters#compose()` has been renamed to `Filters#and()`.
* Created `WhereClause`, `BindParamsF`, `ParamIndex` types and used in `SqlFilter`.
* Refactoring of `SqlFilter#bindParams()`, which now just return a function 
  `Pair<ParamIndex, PreparedStatement> -> Pair<ParamIndex, PreparedStatement>` that can be *composed* easily.
* Created `SqlFiltering` with a couple of utility functions to compose `SqlFilter`s.

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

Conceptually, filtering some *data* can be seen as a predicate function on a value of some generic type:

```haskell
filter :: a -> Bool
```

However, defining filters this way means that every possible predicate from an implementation perspective is a totally different function. For example, if we have a `Person` type and we want to filter those values by age, matching if a person age is 19 or 20 has to be done with two different ad-hoc functions.

Clearly we can do better and use *parametric predicates*, obtaining a predicate by *partial application* (and even better with currying):

```haskell
filterByAge :: Int -> Person -> Bool

filterByAgeRange :: Int -> Int -> Person -> Bool
```

In a Java context, we can model a filter as a partially applied function:

* Every filter is a statically typed class of objects that implements a filter function: it takes a value of a certain type and returns a boolean.
* A filter can be made parametric by requiring parameters at construction time via its constructor. This way, in some sense we are partially applying a filtering function (using the constructor parameters).
* When we have a `Filter` instance (which is just a [function](http://www.functionaljava.org/javadoc/4.4/functionaljava/fj/F.html)), we can apply it to match values of the required type. Actually, `Filter<T>` is indeed a `F<T, Boolean>`.

This way, we can write generic and type-safe filtering operations by *composing filters*. Both AND and OR combinators are implemented.

### JDBC filters

A filter in a JDBC context can be viewed as a pair of related operations:

* WHERE clause string generation
* Parameters binding

*siftj* defined an interface that specifies this contract: `SqlFilter`. Being an interface, there are 
a couple of considerations to make:

1. Every object can be a `SqlFilter`, even a `Filter` implementation. In the latter, both POJOs and JDBC
   filtering can be implemented in the same class (this is not to say that it's the right thing to do or the suggested
   way to use this interface).
2. There is no prescription on how a `SqlFilter` type has to be instantiated: we cannot possibly predict all the use
   cases (JOINs w/ multiple table references, columns renames, etc). So **you have a lot of flexibility 
   (and responsibility) to correctly *design* a viable, clean and maintainable querying strategy**.

## Usage

Some examples can be seen in the tests, but the gist is as follows.

### Value objects

Define a POJO/value object:

```java
public class Person {
  // [...]

  public String getName() { /* ... */ }
  public String getSurname() { /* ... */ }
  public int getAge() { /* ... */ }
  public Sex getSex() { /* ... */ }
}
```

Define some *filters* for this type, by extending the `Filter` base class:

```java
public class AgeFilter implements Filter<Person> {
  private final int age;

  private AgeFilter(int age) {
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public Boolean f(Person p) {
    return p.getAge() == age;
  }
}
```

Use all the filters defined this way to filter `Person` values by using the `Filters` utility class. We can apply a 
single filter to a list of compatible values:

```java
List<Person> results = Filters.filter(ageFilter(21), Arrays.asList(p1, p2));
```

We can also apply a *list of filters* to a list of compatible values:

```java
List<Person> results = Filters.filter(
  Arrays.asList(ageFilter(25), sexFilter(Sex.FEMALE)),
  Arrays.asList(p1, p2, p3));
```

We can even *combine filters together using the AND operator*:

```java
Filter<Person> compFilter = Filters.and(ageFilter(25), sexFilter(Sex.FEMALE));
```

We can compose filters also with the *OR operator*:

```java
Filter<Person> compFilter = Filters.or(ageFilter(25), sexFilter(Sex.FEMALE));
```

Since these combinators operates on `Filter`s (in other words they are functions `[Filter] -> Filter`), they can be freely combined to compose complex filters combinations:

```java
Filters.or(Filters.and(ageFilter(25), sexFilter(Sex.FEMALE)),
           Filters.and(ageFilter(21), sexFilter(Sex.MALE)),
           ageFilter(31));
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
  public WhereClause whereClause() {
    return WhereClause.whereClause(tableRef + "." + name + "=?");
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        ParamIndex index = p._1();
        PreparedStatement statement = p._2();
        statement.setString(index.get(), name);
        return p(index.succ(), statement);
      }
    };
  }
}
```

Then you can use this filter to obtain a WHERE clause:

```java
SqlFilter f = new SqlNameFilter("p", "name");
WhereClause c = f.whereClause();
// c.getClause() = "p.name=?"
```

And to bind the parameters in a full JDBC query:

```java
f.bindParameters().f(P.p(ParamIndex.paramIndex(4), statement));
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
  public WhereClause whereClause() {
    return WhereClause.whereClause(String
        .format("(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef, tableRef));
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        ParamIndex index = p._1();
        PreparedStatement statement = p._2();
        statement.setInt(index.get(), range.getFrom());
        statement.setInt(index.add(1).get(), range.getTo());
        statement.setString(index.add(2).get(), sex.name());
        return p(index.add(3), statement);
      }
    };
  }
}
```

You can also *compose* `SqlFilter` instances with the AND or OR operators:

```java
SqlFilter composedFilter = SqlFilters.or(new SqlNameFilter("p", "name"),
                                         new SqlNameFilter("p", "nomai"))
```

A more complex example:

```java
SqlFilter cf = SqlFilters.or(new SqlNameFilter("p", "Jerry"),
                             SqlFilters.and(new SqlNameFilter("p", "Mary"),
							                ageFilter(28)));
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
* `me.manuelp.siftj.SqlFilters` with its generic `SqlFilter` composing functions.
* `me.manuelp.siftj.sql.*` with the contract of a `SqlFilter` with its supporting value types.
