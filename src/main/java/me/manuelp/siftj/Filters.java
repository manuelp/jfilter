package me.manuelp.siftj;

import static fj.data.List.iterableList;

import fj.F2;
import java.util.List;

/**
 * Defines some generic filtering functions on {@link Filter}s.
 */
public class Filters {
  /**
   * Generic type-safe filtering higher-order function to apply a single {@link Filter} to a list of values.
   *
   * @param filter {@link Filter} to apply to every single value
   * @param values List of values
   * @param <T> Type of values to be filtered
   * @return Filtered list of the original values that satisfies the given filter predicate
   */
  public static <T> List<T> filter(Filter<T> filter, List<T> values) {
    return iterableList(values).filter(filter).toJavaList();
  }

  /**
   * Creates an AND combinator for two {@link Filter}s on the same type.
   *
   * @param <T> Type parameter of {@link Filter}s to combine
   * @return AND combinator
   */
  public static <T> F2<Filter<T>, Filter<T>, Filter<T>> andCombinator() {
    return new F2<Filter<T>, Filter<T>, Filter<T>>() {
      @Override
      public Filter<T> f(final Filter<T> x, final Filter<T> y) {
        return new Filter<T>() {
          @Override
          public Boolean f(T v) {
            return x.f(v) && y.f(v);
          }
        };
      }
    };
  }

  /**
   * Compose in AND multiple filters on the same type in a single filter.
   *
   * @param filters Filters to combine (in AND).
   * @param <T> Type of values that filters can match against.
   * @return Filter obtained by composing in AND all input filters
   */
  public static <T> Filter<T> and(final List<Filter<T>> filters) {
    return new Filter<T>() {
      @Override
      public Boolean f(T t) {
        return iterableList(filters).foldLeft(Filters.<T> andCombinator(), NonFilter.<T> nonFilter(true)).f(t);
      }
    };
  }

  /**
   * Creates an OR combinator for two {@link Filter}s on the same type.
   *
   * @param <T> Type parameter of {@link Filter}s to combine
   * @return OR combinator
   */
  public static <T> F2<Filter<T>, Filter<T>, Filter<T>> orCombinator() {
    return new F2<Filter<T>, Filter<T>, Filter<T>>() {
      @Override
      public Filter<T> f(final Filter<T> x, final Filter<T> y) {
        return new Filter<T>() {
          @Override
          public Boolean f(T v) {
            return x.f(v) || y.f(v);
          }
        };
      }
    };
  }

  /**
   * Compose in OR multiple filters on the same type in a single filter.
   *
   * @param filters Filters to combine (in OR).
   * @param <T> Type of values that filters can match against.
   * @return Filter obtained by composing in OR all input filters
   */
  public static <T> Filter<T> or(final List<Filter<T>> filters) {
    if (filters.isEmpty())
      return NonFilter.<T> nonFilter(true);

    return new Filter<T>() {
      @Override
      public Boolean f(T t) {
        return iterableList(filters).foldLeft(Filters.<T> orCombinator(), NonFilter.<T> nonFilter(false)).f(t);
      }
    };
  }

  /**
   * Generic type-safe filtering functions to apply a list of {@link Filter filters} in AND to a list of values.
   *
   * @param filters List of {@link Filter filters} combined with {@link #andCombinator()} to apply to every value
   * @param values List of values
   * @param <T> Type of the values to filter
   * @return List of values that satisfy <em>all</em> given filter predicates
   */
  public static <T> List<T> filter(List<Filter<T>> filters, List<T> values) {
    return filter(and(filters), values);
  }
}
