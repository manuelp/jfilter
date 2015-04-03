package me.manuelp.jfilter;

import com.googlecode.totallylazy.Predicates;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

/**
 * Defines some generic filtering functions.
 */
public class Filters {
  /**
   * Generic type-safe filtering functions to apply a single {@link Filter} to a
   * list of values.
   *
   * @param filter {@link Filter} value to apply
   * @param values List of values
   * @param <T> Type of values to be filtered
   * @return Filtered list of the original values that
   *         {@link Filter#matches(Object) matches} the given filter
   */
  public static <T> List<T> filter(Filter<T> filter, List<T> values) {
    return sequence(values).filter(filter).toList();
  }

  /**
   * Generic type-safe filtering functions to apply a list of {@link Filter
   * filters} to a list of values.
   * 
   * @param filters List of {@link Filter filters} to apply
   * @param values List of values
   * @param <T> Type of the values to filter
   * @return List of values that {@link Filter#matches(Object) matches}
   *         <em>all</em> the given filters
   */
  public static <T> List<T> filter(List<Filter<T>> filters, List<T> values) {
    List<T> results = sequence(values).toList();
    for (Filter<T> f : filters)
      results = filter(f, results);
    return results;
  }

  /**
   * Compose in AND multiple filters on the same type in a single filter.
   * 
   * @param filters Filters to combine (in AND).
   * @param <T> Type of values that filters can match against.
   * @return Filter obtained by composing in AND all input filters
   */
  public static <T> Filter<T> compose(final Filter<T>... filters) {
    return new Filter<T>() {
      @Override
      public boolean matches(T t) {
        return Predicates.and(filters).matches(t);
      }
    };
  }
}
