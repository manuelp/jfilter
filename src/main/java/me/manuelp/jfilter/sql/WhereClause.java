package me.manuelp.jfilter.sql;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import me.manuelp.jfilter.validations.NotNull;

public class WhereClause {
  private final String clause;

  public WhereClause(String clause) {
    NotNull.check(clause);
    this.clause = clause;
  }

  public static WhereClause whereClause(String clause) {
    return new WhereClause(clause);
  }

  public String getClause() {
    return clause;
  }

  public static Function1<SqlFilter, WhereClause> getClauseF() {
    return new Function1<SqlFilter, WhereClause>() {
      @Override
      public WhereClause call(SqlFilter sqlFilter) {
        return sqlFilter.whereClause();
      }
    };
  }

  public static Function2<WhereClause, WhereClause, WhereClause> concatF2() {
    return new Function2<WhereClause, WhereClause, WhereClause>() {
      @Override
      public WhereClause call(WhereClause c1, WhereClause c2) {
        return c1.concat(c2);
      }
    };
  }

  public WhereClause concat(WhereClause c) {
    return whereClause(String.format("(%s) AND (%s)", this.getClause(),
      c.getClause()));
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
