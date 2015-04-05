package me.manuelp.siftj.sql;

import com.googlecode.totallylazy.Function1;
import me.manuelp.siftj.validations.NotNull;

public class ParamIndex {
  private final Integer index;

  public ParamIndex(Integer index) {
    NotNull.check(index);
    this.index = index;
  }

  public static ParamIndex paramIndex(int index) {
    return new ParamIndex(index);
  }

  public ParamIndex add(int num) {
    return new ParamIndex(get() + num);
  }

  public ParamIndex succ() {
    return add(1);
  }

  public static Function1<ParamIndex, ParamIndex> addF(final int num) {
    return new Function1<ParamIndex, ParamIndex>() {
      @Override
      public ParamIndex call(ParamIndex paramIndex) {
        return paramIndex.add(num);
      }
    };
  }

  public static Function1<ParamIndex, ParamIndex> succF() {
    return addF(1);
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
