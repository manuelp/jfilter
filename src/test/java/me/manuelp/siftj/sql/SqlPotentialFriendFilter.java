package me.manuelp.siftj.sql;

import fj.P2;
import me.manuelp.siftj.data.Range;
import me.manuelp.siftj.data.Sex;
import me.manuelp.siftj.validations.NotNull;

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
  public BindParamsF bindParameter() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(P2<ParamIndex, PreparedStatement> p)
          throws SQLException {
        p._2().setInt(p._1().get(), range.getFrom());
        p._2().setInt(p._1().get() + 1, range.getTo());
        p._2().setString(p._1().get() + 2, sex.name());
        return p.map1(ParamIndex.addF(3));
      }
    };
  }

}
