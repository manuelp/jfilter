package me.manuelp.jfilter.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * An SQL filter encapsulates the WHERE clause construction and parameter
 * setting in a {@link PreparedStatement}.
 * <p>
 * Every SQL filter is a specific instance of this base class that encapsulates
 * the actual filter value, using the same technique of
 * {@link me.manuelp.jfilter.Filter}.
 * </p>
 */
public abstract class SqlFilter {
  private final String tableRef;
  private final String columnName;

  /**
   * Instantiate a base {@link SqlFilter}.
   *
   * @param tableRef Table reference (every WHERE clause generated is by default
   *          fully qualified
   * @param columnName Column name valid in the specified table reference
   */
  public SqlFilter(String tableRef, String columnName) {
    this.tableRef = tableRef;
    this.columnName = columnName;
  }

  /**
   * Generates a WHERE clause for this filter valid in the context of a query
   * where the table reference and column name configured in the constructor are
   * valid.
   * 
   * @return WHERE clause
   */
  public String whereClause() {
    return tableRef + "." + columnName + "=?";
  }

  /**
   * Bind the parameter in a statement.
   *
   * @param statement Statement to configure
   * @param index Index of the parameter to fill in
   * @throws SQLException
   */
  public abstract void bindParameter(PreparedStatement statement, int index)
      throws SQLException;
}
