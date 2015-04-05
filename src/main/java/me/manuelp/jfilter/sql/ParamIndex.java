package me.manuelp.jfilter.sql;

import me.manuelp.jfilter.validations.NotNull;

public class ParamIndex {
  private final Integer index;

  public ParamIndex(Integer index) {
    NotNull.check(index);
    this.index = index;
  }

  public static ParamIndex paramIndex(int index) {
    return new ParamIndex(index);
  }

  public Integer get() {
    return index;
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
