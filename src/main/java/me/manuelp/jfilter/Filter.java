package me.manuelp.jfilter;

import com.googlecode.totallylazy.Predicate;
import me.manuelp.jfilter.validations.NotNull;

/**
 * Models a function <code>a -> b -> Bool</code> where <code>a</code> is the
 * type of a specific filter values and <code>b</code> is the type of values
 * that can be filtered by this specific filter type.
 * <p>
 * The <code>a</code> type is implicitly fixed by the extensions of this base
 * class, and the value can be specified via its constructor.
 * </p>
 *
 * @param <T> Type of the values to which this filter can be applied
 */
public abstract class Filter<T> {
  private final String code;

  protected Filter(String code) {
    NotNull.check(code);
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  /**
   * Approximation of a function <code>A -> T -> Bool</code>, where
   * <code>A</code> is fixed by derived classes and the values can be accessed
   * via <code>this</code>.
   */
  public abstract boolean match(T data);

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Filter<?> filter = (Filter<?>) o;

    return getCode().equals(filter.getCode());

  }

  @Override
  public int hashCode() {
    return getCode().hashCode();
  }

  @Override
  public String toString() {
    return "Filter{" + "code='" + code + '\'' + '}';
  }

  /**
   * Generates a predicate to match a {@code T} value to the filter instance
   * preconfigured with a matching value.
   * <p>
   * The actual logic is implementented by the {@link #match(Object)} method and
   * applied to a specific {@code V} value retained by closure.
   * </p>
   *
   * @return Function <code>V -> T -> Boolean</code> partially applied to a
   *         particular <code>V</code> value
   */
  public Predicate<T> fn() {
    return new Predicate<T>() {
      public boolean matches(T t) {
        return match(t);
      }
    };
  }
}
