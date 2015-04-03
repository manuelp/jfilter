package me.manuelp.jfilter;

import fj.F;
import fj.data.Java;

import java.util.List;

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
   * @return Filtered list of the original values that {@link Filter#f(T)
   *         matches} the given filter
   */
  public static <T> List<T> filter(Filter<T> filter, List<T> values) {
    fj.data.List<T> results = fj.data.List.iterableList(values).filter(filter);
    return Java.<T> List_ArrayList().f(results);
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
      public Boolean f(final T v) {
        return fj.data.List.iterableList(filters).forall(
          new F<Filter<T>, Boolean>() {
            @Override
            public Boolean f(Filter<T> f) {
              return f.f(v);
            }
          });
      }
    };
  }

  /**
   * Generic type-safe filtering functions to apply a list of {@link Filter
   * filters} to a list of values.
   * 
   * @param filters List of {@link Filter filters} to apply
   * @param values List of values
   * @param <T> Type of the values to filter
   * @return List of values that {@link Filter#f(T) matches} <em>all</em> the
   *         given filters
   */
  public static <T> List<T> filter(List<Filter<T>> filters, List<T> values) {
    return filter(and(filters), values);
  }
}
