package me.manuelp.siftj;

import me.manuelp.siftj.data.AgeFilter;
import me.manuelp.siftj.data.Range;
import me.manuelp.siftj.data.Sex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.SqlNameFilter;
import me.manuelp.siftj.sql.SqlPotentialFriendFilter;
import me.manuelp.siftj.sql.WhereClause;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.util.Arrays;

import static com.googlecode.totallylazy.Pair.pair;
import static me.manuelp.siftj.sql.ParamIndex.paramIndex;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SqlFiltersTest {
  @Test
  public void canComposeWhereClausesInAnd() {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");

    WhereClause clause = SqlFilters.and(Arrays.asList(f1, f2)).whereClause();

    assertEquals(String.format("(%s) AND (%s)", f1.whereClause().getClause(),
      f2.whereClause().getClause()), clause.getClause());
  }

  @Test
  public void canComposeWhereClausesInOr() {
    SqlFilter f1 = new SqlNameFilter("p", "Frank");
    SqlFilter f2 = new SqlNameFilter("p", "Jerry");

    WhereClause clause = SqlFilters.or(Arrays.asList(f1, f2)).whereClause();

    assertEquals(String.format("(%s) OR (%s)", f1.whereClause().getClause(), f2
        .whereClause().getClause()), clause.getClause());
  }

  @Test
  public void canComposeWhereClausesInOrAndAnd() {
    SqlFilter f1 = new SqlNameFilter("p", "Frank");
    SqlFilter f2 = new SqlNameFilter("p", "Jerry");
    SqlFilter f3 = new AgeFilter(31);

    SqlFilter c1 = SqlFilters.and(Arrays.asList(f1, f3));
    SqlFilter c2 = SqlFilters.or(Arrays.asList(c1, f2));
    WhereClause whereClause = c2.whereClause();

    assertEquals(String.format("((%s) AND (%s)) OR (%s)", f1.whereClause()
        .getClause(), f3.whereClause().getClause(), f2.whereClause()
        .getClause()), whereClause.getClause());
  }

  @Test
  public void canComposeParamsBindingInAnd() throws Exception {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");
    PreparedStatement s = mock(PreparedStatement.class);

    SqlFilters.and(Arrays.asList(f1, f2)).bindParameter()
        .call(pair(paramIndex(3), s));

    verify(s).setString(3, "Frank");
    verify(s).setInt(4, 20);
    verify(s).setInt(5, 30);
    verify(s).setString(6, Sex.MALE.name());
  }

  @Test
  public void canComposeParamsBindingInOr() throws Exception {
    SqlFilter f1 = new SqlNameFilter("p", "Frank");
    SqlFilter f2 = new SqlNameFilter("p", "James");
    PreparedStatement s = mock(PreparedStatement.class);

    SqlFilters.and(Arrays.asList(f1, f2)).bindParameter()
        .call(pair(paramIndex(3), s));

    verify(s).setString(3, "Frank");
    verify(s).setString(4, "James");
  }

  @Test
  public void canComposeParamsBindingInOrAndAnd() throws Exception {
    SqlFilter f1 = new SqlNameFilter("p", "Frank");
    SqlFilter f2 = new SqlNameFilter("p", "Jerry");
    SqlFilter f3 = new AgeFilter(31);
    PreparedStatement s = mock(PreparedStatement.class);

    SqlFilter c1 = SqlFilters.and(Arrays.asList(f1, f3));
    SqlFilter c2 = SqlFilters.or(Arrays.asList(c1, f2));
    c2.bindParameter().call(pair(paramIndex(1), s));

    verify(s).setString(1, "Frank");
    verify(s).setInt(2, 31);
    verify(s).setString(3, "Jerry");
  }
}
