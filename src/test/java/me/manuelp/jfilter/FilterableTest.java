package me.manuelp.jfilter;

import org.junit.Test;

import static me.manuelp.jfilter.AgeFilter.ageFilter;
import static me.manuelp.jfilter.SexFilter.sexFilter;
import static org.junit.Assert.*;

public class FilterableTest {
  @Test
  public void simpleFilters() {
    Person p = new Person("Name", "Surname", 18, Sex.FEMALE);

    assertTrue(ageFilter(18).match(p));
    assertTrue(sexFilter(Sex.FEMALE).match(p));
    assertFalse(sexFilter(Sex.MALE).match(p));
  }


}