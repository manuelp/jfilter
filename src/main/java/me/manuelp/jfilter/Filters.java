package me.manuelp.jfilter;

import java.util.Collections;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Filters {
  public static <V, T> List<T> filter(Filter<V, T> filter, List<T> values) {
    return sequence(values).filter(filter.fn()).toList();
  }

  public static <T> List<T> filter(List<Filter<?, T>> filters, List<T> values) {
    return Collections.emptyList();
  }
}
