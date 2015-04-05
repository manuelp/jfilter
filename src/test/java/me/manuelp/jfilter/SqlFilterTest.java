package me.manuelp.jfilter;

import me.manuelp.jfilter.data.Sex;
import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.SqlNameFilter;
import me.manuelp.jfilter.sql.SqlPotentialFriendFilter;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static me.manuelp.jfilter.data.AgeFilter.ageFilter;
import static me.manuelp.jfilter.data.Range.range;
import static me.manuelp.jfilter.sql.ParamIndex.paramIndex;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SqlFilterTest {
  @Test
  public void canGenerateWhereClause() {
    SqlFilter f = ageFilter(12);

    assertEquals("t.age=?", f.whereClause().getClause());
  }

  @Test
  public void canBindStatementValues() throws SQLException {
    PreparedStatement statement = mock(PreparedStatement.class);
    SqlFilter f = new SqlNameFilter("t", "Larry");

    f.bindParameter(paramIndex(1)).f(statement);

    verify(statement).setString(1, "Larry");
  }

  @Test
  public void canGenerateComplexWhereClauses() {
    SqlFilter f = new SqlPotentialFriendFilter(range(18, 45), Sex.FEMALE, "p");

    assertEquals("(p.age BETWEEN ? AND ?) AND p.sex=?", f.whereClause()
        .getClause());
  }

  @Test
  public void canBindStatementValuesForComplexWhereClauses()
      throws SQLException {
    PreparedStatement statement = mock(PreparedStatement.class);
    SqlFilter f = new SqlPotentialFriendFilter(range(18, 45), Sex.FEMALE, "p");

    f.bindParameter(paramIndex(5)).f(statement);

    verify(statement).setInt(5, 18);
    verify(statement).setInt(5 + 1, 45);
    verify(statement).setString(5 + 2, Sex.FEMALE.name());
  }
}
