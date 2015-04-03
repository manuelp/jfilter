package me.manuelp.jfilter.sql;

import me.manuelp.jfilter.data.Range;
import me.manuelp.jfilter.data.Sex;
import me.manuelp.jfilter.validations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlPotentialFriendFilter implements SqlFilter {
  private final Range range;
  private final Sex sex;
  private String tableRef;

  public SqlPotentialFriendFilter(Range range, Sex sex, String tableRef) {
    NotNull.check(range, tableRef);
    this.range = range;
    this.sex = sex;
    this.tableRef = tableRef;
  }

  @Override
  public String whereClause() {
    return String.format("(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef,
      tableRef);
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setInt(index, range.getFrom());
    statement.setInt(index + 1, range.getTo());
    statement.setString(index + 2, sex.name());
  }
}
