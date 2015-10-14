package me.manuelp.siftj.sql;

import fj.F;
import me.manuelp.siftj.validations.NotNull;

/**
 * Represents an index of a {@link java.sql.PreparedStatement} parameter.
 * 
 * @see SqlFilter
 * @see BindParamsF
 */
public class ParamIndex {
  private final Integer index;

  private ParamIndex(Integer index) {
    NotNull.check(index);
    this.index = index;
  }

  /**
   * Creates a {@link ParamIndex} with the given value.
   * 
   * @param index Value of the index
   * @return {@link ParamIndex}
   */
  public static ParamIndex paramIndex(int index) {
    return new ParamIndex(index);
  }

  public Integer get() {
    return index;
  }

  /**
   * Add the given value to the current {@link ParamIndex}.
   *
   * @param num Value to add to the current index.
   * @return Updated index.
   */
  public ParamIndex add(int num) {
    return new ParamIndex(get() + num);
  }

  /**
   * Adds one to the current index.
   *
   * @return Current {@link ParamIndex} + 1
   */
  public ParamIndex succ() {
    return add(1);
  }

  public static F<ParamIndex, ParamIndex> addF(final int num) {
    return new F<ParamIndex, ParamIndex>() {
      @Override
      public ParamIndex f(ParamIndex paramIndex) {
        return paramIndex.add(num);
      }
    };
  }

  public static F<ParamIndex, ParamIndex> succF() {
    return addF(1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    ParamIndex that = (ParamIndex) o;

    return get().equals(that.get());
  }

  @Override
  public int hashCode() {
    return get().hashCode();
  }

  @Override
  public String toString() {
    return "ParamIndex{" + "index=" + index + '}';
  }
}
