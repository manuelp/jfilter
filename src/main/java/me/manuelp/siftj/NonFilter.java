package me.manuelp.siftj;

public class NonFilter<T> implements Filter<T> {
  private boolean output;

  private NonFilter(boolean output) {
    this.output = output;
  }

  public static <T> NonFilter<T> nonFilter(boolean output) {
    return new NonFilter<T>(output);
  }

  @Override
  public Boolean f(T t) {
    return output;
  }
}
