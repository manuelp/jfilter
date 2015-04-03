package me.manuelp.jfilter.data;

import me.manuelp.jfilter.Filter;

public class PotentialFriendFilter implements Filter<Person> {
  private Range ageRange;
  private final Sex sex;

  public PotentialFriendFilter(Range ageRange, Sex sex) {
    this.ageRange = ageRange;
    this.sex = sex;
  }

  @Override
  public Boolean f(Person p) {
    return ageRange.contains(p.getAge()) && p.getSex() == sex;
  }
}
