package me.manuelp.jfilter;

public class SexFilter extends Filter<Person> {
  private final Sex sex;

  public SexFilter(Sex v) {
    super("sex");
    this.sex = v;
  }

  public static SexFilter sexFilter(Sex value) {
    return new SexFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getSex() == sex;
  }
}
