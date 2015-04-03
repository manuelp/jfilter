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
  public WhereClause whereClause() {
    return WhereClause.whereClause(tableRef + "." + name + "=?");
  }

  @Override
  public BindParamsF bindParameter(final int index) {
    return new BindParamsF() {
      @Override
      public PreparedStatement f(PreparedStatement statement)
          throws SQLException {
        statement.setString(index, name);
        return statement;
      }
    };
  }
}
