package me.manuelp.siftj;

import fj.F;
import fj.F2;
import fj.P2;
import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static fj.data.List.iterableList;
import static me.manuelp.siftj.sql.WhereClause.concatF2;
import static me.manuelp.siftj.sql.WhereClause.getClauseF;

public class SqlFiltering {
  public static WhereClause whereClause(List<SqlFilter> sqlFilters) {
    return iterableList(sqlFilters).map(getClauseF()).foldLeft1(concatF2());
  }

  public static BindParamsF bindParams(final List<SqlFilter> sqlFilters) {
    return iterableList(sqlFilters).map(getBindParamsF()).foldLeft(
      composeBindParamsF(), bindParamsId());
  }

  private static F<SqlFilter, BindParamsF> getBindParamsF() {
    return new F<SqlFilter, BindParamsF>() {
      @Override
      public BindParamsF f(SqlFilter sqlFilter) {
        return sqlFilter.bindParameter();
      }
    };
  }

  private static F2<BindParamsF, BindParamsF, BindParamsF> composeBindParamsF() {
    return new F2<BindParamsF, BindParamsF, BindParamsF>() {
      @Override
      public BindParamsF f(final BindParamsF a, final BindParamsF b) {
        // Cannot compose Try1?
        return new BindParamsF() {
          @Override
          public P2<ParamIndex, PreparedStatement> f(
              P2<ParamIndex, PreparedStatement> p) throws SQLException {
            return b.f(a.f(p));
          }
        };
      }
    };
  }

  private static BindParamsF bindParamsId() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        return p;
      }
    };
  }
}
