package me.manuelp.siftj;

import fj.F;

/**
 * Generic predicate <code>T -> Boolean</code>.
 *
 * @param <T> Type of the values to which this filter (predicate) can be applied
 */
public interface Filter<T> extends F<T, Boolean> {
}
