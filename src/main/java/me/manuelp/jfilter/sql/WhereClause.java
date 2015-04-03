package me.manuelp.jfilter.sql;

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
