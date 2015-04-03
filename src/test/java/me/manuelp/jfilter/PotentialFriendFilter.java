package me.manuelp.jfilter;

public class PotentialFriendFilter extends Filter<Person> {
  private final int fromAge;
  private final int toAge;
  private final Sex sex;

  public PotentialFriendFilter(int fromAge, int toAge, Sex sex) {
    super("potentialFriend");
    this.fromAge = fromAge;
    this.toAge = toAge;
    this.sex = sex;
  }

  @Override
  public boolean match(Person p) {
    return p.getAge() >= fromAge && p.getAge() <= toAge && p.getSex() == sex;
  }
}
