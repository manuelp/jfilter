package me.manuelp.jfilter;

import com.googlecode.totallylazy.Pair;
import me.manuelp.jfilter.data.Sex;
import me.manuelp.jfilter.sql.ParamIndex;
import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.SqlNameFilter;
import me.manuelp.jfilter.sql.SqlPotentialFriendFilter;
import org.junit.Test;

import java.sql.PreparedStatement;

import static me.manuelp.jfilter.data.AgeFilter.ageFilter;
import static me.manuelp.jfilter.data.Range.range;
import static me.manuelp.jfilter.sql.WhereClause.whereClause;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SqlFilterTest {
  @Test
  public void canGenerateWhereClause() {
    SqlFilter f = ageFilter(12);

    assertEquals(whereClause("t.age=?"), f.whereClause());
  }

  @Test
  public void canBindStatementValues() throws Exception {
    PreparedStatement statement = mock(PreparedStatement.class);
    SqlFilter f = new SqlNameFilter("t", "Larry");

    f.bindParameter().call(Pair.pair(ParamIndex.paramIndex(1), statement));

    verify(statement).setString(1, "Larry");
  }

  @Test
  public void canGenerateComplexWhereClauses() {
    SqlFilter f = new SqlPotentialFriendFilter(range(18, 45), Sex.FEMALE, "p");

    assertEquals(whereClause("(p.age BETWEEN ? AND ?) AND p.sex=?"),
      f.whereClause());
  }

  @Test
  public void canBindStatementValuesForComplexWhereClauses() throws Exception {
    PreparedStatement statement = mock(PreparedStatement.class);
    SqlFilter f = new SqlPotentialFriendFilter(range(18, 45), Sex.FEMALE, "p");

    f.bindParameter().call(Pair.pair(ParamIndex.paramIndex(5), statement));

    verify(statement).setInt(5, 18);
    verify(statement).setInt(6, 45);
    verify(statement).setString(7, Sex.FEMALE.name());
  }
}
