package me.manuelp.siftj.sql;

import java.sql.PreparedStatement;

/**
 * An SQL filter encapsulates the WHERE clause construction and parameter
 * setting in a {@link PreparedStatement}.
 * <p>
 * Every SQL filter is an implementation of this interface that encapsulates the
 * actual filter value, using the same technique of
 * {@link me.manuelp.siftj.Filter}.
 * </p>
 */
public interface SqlFilter {

  /**
   * Generates a WHERE clause for this filter valid in the context of a query
   * where the table reference and column name configured in the constructor are
   * valid.
   * <p>
   * Can generate complex clauses with multiple values, or simple ones without
   * parameters (for example "IS NOT NULL" clauses).
   * </p>
   * 
   * @return WHERE clause
   */
  WhereClause whereClause();

  /**
   *
   * Returns a {@link BindParamsF binding function} that binds parameters in a
   * {@link PreparedStatement} (or not, if the corresponding
   * {@link #whereClause()} is not parametric.
   *
   * @return {@link BindParamsF Function} to bind parameters values in a
   *         {@link PreparedStatement}
   */
  // TODO Rename in "bindParameters"
  BindParamsF bindParameter();
}
