package me.manuelp.jfilter.data;

public class Range {
  private final int from, to;

  public Range(int from, int to) {
    this.from = from;
    this.to = to;
  }

  public static Range range(int from, int to) {
    return new Range(from, to);
  }

  public boolean contains(int n) {
    return from <= n && n <= to;
  }
}
