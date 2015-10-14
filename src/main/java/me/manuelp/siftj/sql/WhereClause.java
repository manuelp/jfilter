package me.manuelp.siftj.sql;

import fj.F;
import fj.F2;
import me.manuelp.siftj.validations.NotNull;

/**
 * Represents a WHERE clause, eg. <code>a.field=?</code> or
 * <code>b IS NOT NULL</code>, etc.
 */
public class WhereClause {
  private final String clause;

  private WhereClause(String clause) {
    NotNull.check(clause);
    this.clause = clause;
  }

  public static WhereClause whereClause(String clause) {
    return new WhereClause(clause);
  }

  public String getClause() {
    return clause;
  }

  public static F<SqlFilter, WhereClause> getClauseF() {
    return new F<SqlFilter, WhereClause>() {
      @Override
      public WhereClause f(SqlFilter sqlFilter) {
        return sqlFilter.whereClause();
      }
    };
  }

  /**
   * Creates a function that concatenates two where clauses with an AND SQL
   * operator, wrapping the original clauses in parenthesis.
   * 
   * @return {@link WhereClause}s concatenation function
   */
  public static F2<WhereClause, WhereClause, WhereClause> concatF2() {
    return new F2<WhereClause, WhereClause, WhereClause>() {
      @Override
      public WhereClause f(WhereClause c1, WhereClause c2) {
        return c1.concat(c2);
      }
    };
  }

  /**
   * Concatenates two where clauses with an AND SQL operator, wrapping the
   * original clauses in parenthesis.
   *
   * @return {@link WhereClause} which is the concatenation
   */
  public WhereClause concat(WhereClause c) {
    return whereClause(
      String.format("(%s) AND (%s)", this.getClause(), c.getClause()));
  }

  /**
   * Checks if this {@link WhereClause} is empty.
   *
   * @return True if empty, false otherwise.
   */
  public boolean isEmpty() {
    return clause.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    WhereClause that = (WhereClause) o;

    return getClause().equals(that.getClause());

  }

  @Override
  public int hashCode() {
    return getClause().hashCode();
  }

  @Override
  public String toString() {
    return "WhereClause{" + "clause='" + clause + '\'' + '}';
  }
}
