package me.manuelp.jfilter;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Filters {
  public static <T> List<T> filter(Filter<T> filter, List<T> values) {
    return sequence(values).filter(filter.fn()).toList();
  }

  public static <T> List<T> filter(List<Filter<T>> filters, List<T> values) {
    List<T> results = sequence(values).toList();
    for (Filter<T> f : filters)
      results = filter(f, results);
    return results;
  }
}
