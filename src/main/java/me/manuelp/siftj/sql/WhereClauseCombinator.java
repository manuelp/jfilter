package me.manuelp.siftj.sql;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.callables.JoinString;

import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class WhereClauseCombinator {
  /**
   * Compose multiple {@link SqlFilter}s in AND, generating the corresponding
   * {@link WhereClause}.
   *
   * @param sqlFilters List of {@link SqlFilter} to compose
   * @return Resulting {@link WhereClause}
   */
  public WhereClause and(List<SqlFilter> sqlFilters) {
    return composeWhereClauses(sqlFilters, "AND");
  }

  private WhereClause composeWhereClauses(List<SqlFilter> sqlFilters,
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

  /**
   * Compose multiple {@link SqlFilter}s in OR, generating the corresponding
   * {@link WhereClause}.
   *
   * @param sqlFilters List of {@link SqlFilter} to compose
   * @return Resulting {@link WhereClause}
   */
  public WhereClause or(List<SqlFilter> sqlFilters) {
    return composeWhereClauses(sqlFilters, "OR");
  }
}
