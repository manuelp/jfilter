package me.manuelp.jfilter;

import fj.F2;
import me.manuelp.jfilter.sql.SqlFilter;
import me.manuelp.jfilter.sql.WhereClause;

import java.util.List;

public class SqlFiltering {
  public static WhereClause whereClause(List<SqlFilter> sqlFilters) {
    return fj.data.List.iterableList(sqlFilters).map(WhereClause.getClauseF())
        .foldLeft1(new F2<WhereClause, WhereClause, WhereClause>() {
          @Override
          public WhereClause f(WhereClause c1, WhereClause c2) {
            return WhereClause.whereClause(String.format("(%s) AND (%s)",
              c1.getClause(), c2.getClause()));
          }
        });
  }
}
