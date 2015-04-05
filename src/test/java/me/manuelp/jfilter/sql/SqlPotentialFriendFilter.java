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
  public WhereClause whereClause() {
    return WhereClause.whereClause(String.format(
      "(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef, tableRef));
  }

  @Override
  public BindParamsF bindParameter(final ParamIndex index) {
    return new BindParamsF() {
      @Override
      public PreparedStatement f(PreparedStatement statement)
          throws SQLException {
        statement.setInt(index.get(), range.getFrom());
        statement.setInt(index.get() + 1, range.getTo());
        statement.setString(index.get() + 2, sex.name());
        return statement;
      }
    };
  }
}
