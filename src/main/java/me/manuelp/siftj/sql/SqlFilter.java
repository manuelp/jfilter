package me.manuelp.siftj.sql;

import me.manuelp.siftj.SqlFilters;

import java.sql.PreparedStatement;

/**
 * A SQL filter encapsulates the (context free) WHERE clause construction and
 * parameter setting in a {@link PreparedStatement}.
 * <p>
 * Every SQL filter is an implementation of this interface that encapsulates the
 * actual WHERE clause construction and parameters setting logic.
 * </p>
 * <p>
 * With this abstraction, which ties together a {@link WhereClause} with the
 * corresponding {@link BindParamsF}, we can represent a context-free SQL where
 * clause (a filter on a table reference), that can be composed with other
 * compatible filters and used to run a SELECT query by filtering its results.
 * </p>
 */
public interface SqlFilter {

  /**
   * Generates a WHERE clause for this filter.
   * <p>
   * It can generate complex clauses with multiple values, or simple ones
   * without parameters (for example "IS NOT NULL" clauses).
   * </p>
   * 
   * @return WHERE clause
   */
  WhereClause whereClause();

  /**
   * Returns a {@link BindParamsF binding function} that binds parameters in a
   * {@link PreparedStatement} (or not, if the corresponding
   * {@link #whereClause()} is not parametric (see
   * {@link SqlFilters#bindParamsId()}).
   *
   * @return {@link BindParamsF Function} to bind parameters values in a
   *         {@link PreparedStatement}
   */
  BindParamsF bindParameters();
}
