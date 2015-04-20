package me.manuelp.siftj.data;

import me.manuelp.siftj.Filter;

public class SexFilter extends Filter<Person> {
  private final Sex sex;

  public SexFilter(Sex v) {
    this.sex = v;
  }

  public static SexFilter sexFilter(Sex value) {
    return new SexFilter(value);
  }

  @Override
  public boolean matches(Person p) {
    return p.getSex() == sex;
  }
}