package me.manuelp.jfilter;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Predicate;
import me.manuelp.jfilter.validations.NotNull;

/**
 * @param <V> Type of the filter value
 * @param <T> Type of the values to which this filter can be applied
 */
public abstract class Filter<V, T> {
  private final String code;
  private final V value;

  protected Filter(String code, V value) {
    NotNull.check(code, value);
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public V getValue() {
    return value;
  }

  /**
   * Approximation of a function <code>V -> T -> Bool</code>.
   */
  public abstract boolean match(T data);

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    @SuppressWarnings("unchecked")
    Filter<V, T> filter = (Filter<V, T>) o;

    return getCode().equals(filter.getCode())
        && getValue().equals(filter.getValue());
  }

  @Override
  public int hashCode() {
    int result = getCode().hashCode();
    result = 31 * result + getValue().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Filter{" + "code='" + code + '\'' + ", value=" + value + '}';
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
