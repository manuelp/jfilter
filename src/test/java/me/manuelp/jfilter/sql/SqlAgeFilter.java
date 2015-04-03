package me.manuelp.jfilter.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlAgeFilter extends SqlFilter {
  private final int age;

  public SqlAgeFilter(String tableRef, int age) {
    super(tableRef, "age");
    this.age = age;
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setInt(index, age);
  }
}
