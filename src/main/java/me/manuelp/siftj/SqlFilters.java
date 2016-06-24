package me.manuelp.siftj;

import static fj.data.List.iterableList;

import fj.F;
import fj.F2;
import fj.P2;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.IdentitySqlFilter;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

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
  public static <T extends SqlFilter> SqlFilter and(final List<T> sqlFilters) {
    if(sqlFilters.isEmpty())
      return new IdentitySqlFilter();
    else if (sqlFilters.size() == 1)
      return sqlFilters.get(0);

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

  private static <T extends SqlFilter> BindParamsF composeBindParamsF(
      List<T> sqlFilters) {
    return iterableList(sqlFilters).map(SqlFilters.<T> getBindParamsF())
        .foldLeft1(composeBindParamsF());
  }

  private static <T extends SqlFilter> WhereClause composeWhereClauses(
      List<T> sqlFilters, String binaryOperator) {
    String whereClauseString = iterableList(sqlFilters)
        .map(SqlFilters.<T> getWhereClauseF()).map(toEnclosedString())
        .intersperse(" " + binaryOperator + " ")
        .foldLeft1(new F2<String, String, String>() {
          @Override
          public String f(String x, String y) {
            return x + y;
          }
        });
    return WhereClause.whereClause(whereClauseString);
  }

  private static <T extends SqlFilter> F<T, WhereClause> getWhereClauseF() {
    return new F<T, WhereClause>() {
      @Override
      public WhereClause f(T sqlFilter) {
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

  private static <T extends SqlFilter> F<T, BindParamsF> getBindParamsF() {
    return new F<T, BindParamsF>() {
      @Override
      public BindParamsF f(T sqlFilter) {
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
  public static <T extends SqlFilter> SqlFilter or(final List<T> sqlFilters) {
    if(sqlFilters.isEmpty())
      return new IdentitySqlFilter();
    else if (sqlFilters.size() == 1)
      return sqlFilters.get(0);

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
