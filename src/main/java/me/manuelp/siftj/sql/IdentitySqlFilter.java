package me.manuelp.siftj.sql;

import fj.P2;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IdentitySqlFilter implements SqlFilter {
  @Override
  public WhereClause whereClause() {
    return WhereClause.whereClause("TRUE");
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(P2<ParamIndex, PreparedStatement> x) throws SQLException {
        return x;
      }
    };
  }
}
