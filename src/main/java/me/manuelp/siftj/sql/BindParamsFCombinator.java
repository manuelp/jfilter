package me.manuelp.siftj.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class BindParamsFCombinator {
  /**
   * Compose multiple {@link SqlFilter}s in AND, generating the corresponding
   * {@link BindParamsF}.
   *
   * @param sqlFilters List of {@link SqlFilter} to compose
   * @return Resulting {@link BindParamsF}
   */
  public BindParamsF and(List<SqlFilter> sqlFilters) {
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
