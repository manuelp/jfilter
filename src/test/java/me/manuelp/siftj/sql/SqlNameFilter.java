package me.manuelp.siftj.sql;

import fj.P2;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fj.P.p;

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
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        ParamIndex index = p._1();
        PreparedStatement statement = p._2();
        statement.setString(index.get(), name);
        return p(index.succ(), statement);
      }
    };
  }
}
