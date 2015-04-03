package me.manuelp.jfilter;

public class SexFilter extends Filter<Sex, Person> {
  public SexFilter(Sex v) {
    super("sex", v);
  }

  public static SexFilter sexFilter(Sex value) {
    return new SexFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getSex() == getValue();
  }
}
