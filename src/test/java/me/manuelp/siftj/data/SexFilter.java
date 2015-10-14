package me.manuelp.siftj.data;

import me.manuelp.siftj.Filter;

public class SexFilter implements Filter<Person> {
  private final Sex sex;

  private SexFilter(Sex v) {
    this.sex = v;
  }

  public static SexFilter sexFilter(Sex value) {
    return new SexFilter(value);
  }

  @Override
  public Boolean f(Person p) {
    return p.getSex() == sex;
  }
}
