package me.manuelp.jfilter.data;

import me.manuelp.jfilter.Filter;

public class SexFilter implements Filter<Person> {
  private final Sex sex;

  public SexFilter(Sex v) {
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
