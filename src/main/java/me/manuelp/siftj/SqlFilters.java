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

/**
 * Defines some generic functions on {@link SqlFilter}.
 */
public class SqlFilters {

  /**
   * Compose multiple {@link SqlFilter} in AND.
   *
   * @param sqlFilters List of {@link SqlFilter} to combine.
   * @return {@link SqlFilter} that combines in AND the given ones.
   */
  public static SqlFilter and(final List<SqlFilter> sqlFilters) {
    return new SqlFilter() {
      @Override
      public WhereClause whereClause() {
        return composeWhereClauses(sqlFilters, "AND");
      }

      @Override
      public BindParamsF bindParameters() {
        return composeBindParamsF(sqlFilters);
      }
    };
  }

  private static BindParamsF composeBindParamsF(List<SqlFilter> sqlFilters) {
    return iterableList(sqlFilters).map(getBindParamsF())
        .foldLeft1(composeBindParamsF());
  }

  private static WhereClause composeWhereClauses(List<SqlFilter> sqlFilters,
      String binaryOperator) {
    String whereClauseString = iterableList(sqlFilters).map(getWhereClauseF())
        .map(toEnclosedString()).intersperse(" " + binaryOperator + " ")
        .foldLeft1(new F2<String, String, String>() {
          @Override
          public String f(String x, String y) {
            return x + y;
          }
        });
    return WhereClause.whereClause(whereClauseString);
  }

  private static F<SqlFilter, WhereClause> getWhereClauseF() {
    return new F<SqlFilter, WhereClause>() {
      @Override
      public WhereClause f(SqlFilter sqlFilter) {
        return sqlFilter.whereClause();
      }
    };
  }

  private static F<WhereClause, String> toEnclosedString() {
    return new F<WhereClause, String>() {
      @Override
      public String f(WhereClause whereClause) {
        return "(" + whereClause.getClause() + ")";
      }
    };
  }

  private static F<SqlFilter, BindParamsF> getBindParamsF() {
    return new F<SqlFilter, BindParamsF>() {
      @Override
      public BindParamsF f(SqlFilter sqlFilter) {
        return sqlFilter.bindParameters();
      }
    };
  }

  public static BindParamsF bindParamsId() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        return p;
      }
    };
  }

  private static F2<BindParamsF, BindParamsF, BindParamsF> composeBindParamsF() {
    return new F2<BindParamsF, BindParamsF, BindParamsF>() {
      @Override
      public BindParamsF f(final BindParamsF a, final BindParamsF b) {
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

  /**
   * Compose multiple {@link SqlFilter} in OR.
   *
   * @param sqlFilters List of {@link SqlFilter} to combine.
   * @return {@link SqlFilter} that combines in OR the given ones.
   */
  public static SqlFilter or(final List<SqlFilter> sqlFilters) {
    return new SqlFilter() {
      @Override
      public WhereClause whereClause() {
        return composeWhereClauses(sqlFilters, "OR");
      }

      @Override
      public BindParamsF bindParameters() {
        return composeBindParamsF(sqlFilters);
      }
    };
  }
}
