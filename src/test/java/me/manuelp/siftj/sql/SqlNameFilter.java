package me.manuelp.siftj.sql;

import fj.P2;

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
  public BindParamsF bindParameter() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        p._2().setString(p._1().get(), name);
        return p.map1(ParamIndex.succF());
      }
    };
  }
}
