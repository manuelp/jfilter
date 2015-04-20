package me.manuelp.siftj;

import me.manuelp.siftj.sql.BindParamsFCombinator;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClauseCombinator;

/**
 * Defines some generic functions on {@link SqlFilter}.
 */
public class SqlFilters {
  public static WhereClauseCombinator whereClause() {
    return new WhereClauseCombinator();
  }

  public static BindParamsFCombinator bindParams() {
    return new BindParamsFCombinator();
  }
}
