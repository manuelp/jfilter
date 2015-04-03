package me.manuelp.jfilter;

public class PotentialFriendFilter extends Filter<Person> {
  private Range ageRange;
  private final Sex sex;

  public PotentialFriendFilter(Range ageRange, Sex sex) {
    super("potentialFriend");
    this.ageRange = ageRange;
    this.sex = sex;
  }

  @Override
  public boolean match(Person p) {
    return ageRange.contains(p.getAge()) && p.getSex() == sex;
  }
}
