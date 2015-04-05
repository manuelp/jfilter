package me.manuelp.jfilter;

import fj.P;
import me.manuelp.jfilter.data.Range;
import me.manuelp.jfilter.data.Sex;
import me.manuelp.jfilter.sql.SqlNameFilter;
import me.manuelp.jfilter.sql.SqlPotentialFriendFilter;
import me.manuelp.jfilter.sql.WhereClause;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import static me.manuelp.jfilter.sql.ParamIndex.paramIndex;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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

  @Test
  public void canComposeParamsBinding() throws SQLException {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");
    PreparedStatement s = mock(PreparedStatement.class);

    SqlFiltering.bindParams(Arrays.asList(f1, f2)).f(P.p(paramIndex(3), s));

    verify(s).setString(3, "Frank");
    verify(s).setInt(4, 20);
    verify(s).setInt(5, 30);
    verify(s).setString(6, Sex.MALE.name());
  }
}
