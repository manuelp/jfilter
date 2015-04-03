package me.manuelp.jfilter;

import me.manuelp.jfilter.data.Range;
import me.manuelp.jfilter.data.Sex;
import me.manuelp.jfilter.sql.SqlNameFilter;
import me.manuelp.jfilter.sql.SqlPotentialFriendFilter;
import me.manuelp.jfilter.sql.WhereClause;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class SqlFilteringTest {
  @Test
  public void canComposeWhereClauses() {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");

    WhereClause clause = SqlFiltering.whereClause(Arrays.asList(f1, f2));

    assertEquals(String.format("(%s) AND (%s)", f1.whereClause().getClause(),
      f2.whereClause().getClause()), clause.getClause());
  }
}
