package me.manuelp.jfilter.data;

import me.manuelp.jfilter.Filter;

public class AgeFilter extends Filter<Person> {
  private final int age;

  public AgeFilter(int age) {
    super("age");
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getAge() == age;
  }
}
