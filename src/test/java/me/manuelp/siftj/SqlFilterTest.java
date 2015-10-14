package me.manuelp.siftj;

import me.manuelp.siftj.data.Sex;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.SqlNameFilter;
import me.manuelp.siftj.sql.SqlPotentialFriendFilter;
import org.junit.Test;

import java.sql.PreparedStatement;

import static fj.P.p;
import static me.manuelp.siftj.data.AgeFilter.ageFilter;
import static me.manuelp.siftj.data.Range.range;
import static me.manuelp.siftj.sql.WhereClause.whereClause;
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

    f.bindParameters().f(p(ParamIndex.paramIndex(1), statement));

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

    f.bindParameters().f(p(ParamIndex.paramIndex(5), statement));

    verify(statement).setInt(5, 18);
    verify(statement).setInt(6, 45);
    verify(statement).setString(7, Sex.FEMALE.name());
  }
}
