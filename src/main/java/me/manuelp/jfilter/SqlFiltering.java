package me.manuelp.jfilter;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.callables.JoinString;
import me.manuelp.jfilter.sql.BindParamsF;
import me.manuelp.jfilter.sql.ParamIndex;
import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.WhereClause;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class SqlFiltering {
  public static WhereClause whereClause(List<SqlFilter> sqlFilters) {
    String whereClauseString = sequence(sqlFilters).map(getWhereClauseF())
        .map(toEnclosedString()).intersperse(" AND ")
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

  public static BindParamsF bindParams(final List<SqlFilter> sqlFilters) {
    return sequence(sqlFilters).map(getBindParamsF()).foldLeft(bindParamsId(),
      composeBindParamsF());
  }

  private static Callable1<SqlFilter, BindParamsF> getBindParamsF() {
    return new Callable1<SqlFilter, BindParamsF>() {
      @Override
      public BindParamsF call(SqlFilter sqlFilter) throws Exception {
        return sqlFilter.bindParameter();
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
}
