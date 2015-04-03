package me.manuelp.jfilter;

import me.manuelp.jfilter.data.Person;
import me.manuelp.jfilter.data.Sex;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static me.manuelp.jfilter.data.AgeFilter.ageFilter;
import static me.manuelp.jfilter.data.SexFilter.sexFilter;
import static org.junit.Assert.assertEquals;

public class FilteringTest {
  @Test
  public void canApplyAFilterToAListOfValues() {
    Person p1 = new Person("_", "_", 17, Sex.FEMALE);
    Person p2 = new Person("_", "_", 21, Sex.MALE);

    List<Person> results = Filters.filter(ageFilter(21), Arrays.asList(p1, p2));

    assertEquals(1, results.size());
    assertEquals(p2, results.get(0));
  }

  @Test
  public void canApplyMultipleFiltersToAListOfValues() {
    Person p1 = new Person("_", "_", 17, Sex.FEMALE);
    Person p2 = new Person("_", "_", 17, Sex.MALE);
    Person p3 = new Person("_", "_", 21, Sex.MALE);

    List<Person> results = Filters.filter(
      Arrays.asList(ageFilter(17), sexFilter(Sex.MALE)),
      Arrays.asList(p1, p2, p3));

    assertEquals(1, results.size());
    assertEquals(p2, results.get(0));
  }
}
