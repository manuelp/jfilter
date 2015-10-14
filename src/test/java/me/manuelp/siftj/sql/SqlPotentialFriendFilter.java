package me.manuelp.siftj.sql;

import fj.P2;
import me.manuelp.siftj.data.Range;
import me.manuelp.siftj.data.Sex;
import me.manuelp.siftj.validations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fj.P.p;

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
    return WhereClause.whereClause(String
        .format("(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef, tableRef));
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        ParamIndex index = p._1();
        PreparedStatement statement = p._2();
        statement.setInt(index.get(), range.getFrom());
        statement.setInt(index.add(1).get(), range.getTo());
        statement.setString(index.add(2).get(), sex.name());
        return p(index.add(3), statement);
      }
    };
  }
}
