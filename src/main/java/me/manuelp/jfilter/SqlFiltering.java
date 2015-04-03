package me.manuelp.jfilter;

import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.WhereClause;

import java.util.List;

import static fj.data.List.iterableList;
import static me.manuelp.jfilter.sql.WhereClause.concatF2;
import static me.manuelp.jfilter.sql.WhereClause.getClauseF;

public class SqlFiltering {
  public static WhereClause whereClause(List<SqlFilter> sqlFilters) {
    return iterableList(sqlFilters).map(getClauseF()).foldLeft1(concatF2());
  }

}
