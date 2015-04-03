package me.manuelp.jfilter;

import me.manuelp.jfilter.sql.SqlAgeFilter;
import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.SqlNameFilter;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SqlFilterTest {
  @Test
  public void canGenerateWhereClause() {
    SqlFilter f = new SqlAgeFilter("t", 12);

    assertEquals("t.age=?", f.whereClause());
  }

  @Test
  public void canBindStatementValues() throws SQLException {
    PreparedStatement statement = mock(PreparedStatement.class);
    SqlFilter f = new SqlNameFilter("t", "Larry");

    f.bindParameter(statement, 1);

    verify(statement).setString(1, "Larry");
  }
}
