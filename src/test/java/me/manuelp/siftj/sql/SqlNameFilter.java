package me.manuelp.siftj.sql;

import com.googlecode.totallylazy.Pair;

import java.sql.PreparedStatement;

import static com.googlecode.totallylazy.Pair.pair;

public class SqlNameFilter implements SqlFilter {
  private String tableRef;
  private final String name;

  public SqlNameFilter(String tableRef, String name) {
    this.tableRef = tableRef;
    this.name = name;
  }

  @Override
  public WhereClause whereClause() {
    return new WhereClause(tableRef + "." + name + "=?");
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public Pair<ParamIndex, PreparedStatement> call(
          Pair<ParamIndex, PreparedStatement> p) throws Exception {
        ParamIndex index = p.first();
        PreparedStatement statement = p.second();
        statement.setString(index.get(), name);
        return pair(index.succ(), statement);
      }
    };
  }
}
