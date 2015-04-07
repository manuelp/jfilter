package me.manuelp.siftj;

import me.manuelp.siftj.data.Range;
import me.manuelp.siftj.data.Sex;
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
  public void canComposeWhereClauses() {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");

    WhereClause clause = SqlFilters.whereClause(Arrays.asList(f1, f2));

    assertEquals(String.format("(%s) AND (%s)", f1.whereClause().getClause(),
      f2.whereClause().getClause()), clause.getClause());
  }

  @Test
  public void canComposeParamsBinding() throws Exception {
    SqlNameFilter f1 = new SqlNameFilter("p", "Frank");
    SqlPotentialFriendFilter f2 = new SqlPotentialFriendFilter(Range.range(20,
      30), Sex.MALE, "p");
    PreparedStatement s = mock(PreparedStatement.class);

    SqlFilters.bindParams(Arrays.asList(f1, f2)).call(pair(paramIndex(3), s));

    verify(s).setString(3, "Frank");
    verify(s).setInt(4, 20);
    verify(s).setInt(5, 30);
    verify(s).setString(6, Sex.MALE.name());
  }
}
