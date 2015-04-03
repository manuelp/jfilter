package me.manuelp.jfilter;

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

    Filter<V, T> filter = (Filter<V, T>) o;

    if (!getCode().equals(filter.getCode()))
      return false;
    return getValue().equals(filter.getValue());

  }

  @Override
  public int hashCode() {
    int result = getCode().hashCode();
    result = 31 * result + getValue().hashCode();
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Filter{");
    sb.append("code='").append(code).append('\'');
    sb.append(", value=").append(value);
    sb.append('}');
    return sb.toString();
  }
}
