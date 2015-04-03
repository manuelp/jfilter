package me.manuelp.jfilter.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlNameFilter implements SqlFilter {
  private String tableRef;
  private final String name;

  public SqlNameFilter(String tableRef, String name) {
    this.tableRef = tableRef;
    this.name = name;
  }

  @Override
  public String whereClause() {
    return tableRef + "." + name + "=?";
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setString(index, name);
  }
}
