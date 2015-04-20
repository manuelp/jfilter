package me.manuelp.siftj;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.callables.JoinString;
import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

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
    return sequence(sqlFilters).map(getBindParamsF()).foldLeft(bindParamsId(),
      composeBindParamsF());
  }

  private static WhereClause composeWhereClauses(List<SqlFilter> sqlFilters,
      String binaryOperator) {
    String whereClauseString = sequence(sqlFilters).map(getWhereClauseF())
        .map(toEnclosedString()).intersperse(" " + binaryOperator + " ")
        .reduce(JoinString.instance);
    return WhereClause.whereClause(whereClauseString);
  }

  private static Callable1<SqlFilter, WhereClause> getWhereClauseF() {
    return new Callable1<SqlFilter, WhereClause>() {
      @Override
      public WhereClause call(SqlFilter sqlFilter) throws Exception {
        return sqlFilter.whereClause();
      }
    };
  }

  private static Callable1<WhereClause, String> toEnclosedString() {
    return new Callable1<WhereClause, String>() {
      @Override
      public String call(WhereClause whereClause) throws Exception {
        return "(" + whereClause.getClause() + ")";
      }
    };
  }

  private static Callable1<SqlFilter, BindParamsF> getBindParamsF() {
    return new Callable1<SqlFilter, BindParamsF>() {
      @Override
      public BindParamsF call(SqlFilter sqlFilter) throws Exception {
        return sqlFilter.bindParameters();
      }
    };
  }

  private static BindParamsF bindParamsId() {
    return new BindParamsF() {
      @Override
      public Pair<ParamIndex, PreparedStatement> call(
          Pair<ParamIndex, PreparedStatement> p) throws SQLException {
        return p;
      }
    };
  }

  private static Callable2<BindParamsF, BindParamsF, BindParamsF> composeBindParamsF() {
    return new Callable2<BindParamsF, BindParamsF, BindParamsF>() {
      @Override
      public BindParamsF call(final BindParamsF a, final BindParamsF b)
          throws Exception {
        return new BindParamsF() {
          @Override
          public Pair<ParamIndex, PreparedStatement> call(
              Pair<ParamIndex, PreparedStatement> p) throws Exception {
            return b.call(a.call(p));
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
