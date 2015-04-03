package me.manuelp.jfilter;

import me.manuelp.jfilter.data.Person;
import me.manuelp.jfilter.data.PotentialFriendFilter;
import me.manuelp.jfilter.data.Sex;
import org.junit.Test;

import static me.manuelp.jfilter.data.AgeFilter.ageFilter;
import static me.manuelp.jfilter.data.Range.range;
import static me.manuelp.jfilter.data.SexFilter.sexFilter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FilterTest {
  @Test
  public void simpleFilters() {
    Person p = new Person("Name", "Surname", 18, Sex.FEMALE);

    assertTrue(ageFilter(18).f(p));
    assertTrue(sexFilter(Sex.FEMALE).f(p));
    assertFalse(sexFilter(Sex.MALE).f(p));
  }

  @Test
  public void thereCanBeComplexFilters() {
    Person p = new Person("Name", "Surname", 18, Sex.FEMALE);

    assertTrue(new PotentialFriendFilter(range(18, 35), Sex.FEMALE).f(p));
    assertFalse(new PotentialFriendFilter(range(20, 35), Sex.FEMALE).f(p));
  }

}