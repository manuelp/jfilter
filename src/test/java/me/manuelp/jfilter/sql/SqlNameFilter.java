package me.manuelp.jfilter.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlNameFilter extends SqlFilter {
  private final String name;

  public SqlNameFilter(String tableRef, String name) {
    super(tableRef, "name");
    this.name = name;
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setString(1, name);
  }
}
