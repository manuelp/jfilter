package me.manuelp.jfilter;

import fj.F;

/**
 * Models a function <code>a -> t -> Bool</code> where <code>a</code> is the
 * type of a specific filter values and <code>t</code> is the type of values
 * that can be filtered by this specific filter type.
 * <p>
 * The <code>a</code> type is implicitly fixed by the extensions of this base
 * class, and the value can be specified via its constructor.
 * </p>
 *
 * @param <T> Type of the values to which this filter can be applied
 */
public interface Filter<T> extends F<T, Boolean> {
}
