package me.manuelp.jfilter;

public class AgeFilter extends Filter<Integer, Person> {
  public AgeFilter(int value) {
    super("age", value);
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getAge() == getValue();
  }
}
