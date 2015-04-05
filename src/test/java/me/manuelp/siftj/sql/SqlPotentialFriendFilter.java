package me.manuelp.siftj.sql;

import com.googlecode.totallylazy.Pair;
import me.manuelp.siftj.data.Range;
import me.manuelp.siftj.data.Sex;
import me.manuelp.siftj.validations.NotNull;

import java.sql.PreparedStatement;

import static com.googlecode.totallylazy.Pair.pair;

public class SqlPotentialFriendFilter implements SqlFilter {
  private final Range range;
  private final Sex sex;
  private String tableRef;

  public SqlPotentialFriendFilter(Range range, Sex sex, String tableRef) {
    NotNull.check(range, tableRef);
    this.range = range;
    this.sex = sex;
    this.tableRef = tableRef;
  }

  @Override
  public WhereClause whereClause() {
    return new WhereClause(String.format(
      "(%s.age BETWEEN ? AND ?) AND %s.sex=?", tableRef, tableRef));
  }

  @Override
  public BindParamsF bindParameter() {

    return new BindParamsF() {
      @Override
      public Pair<ParamIndex, PreparedStatement> call(
          Pair<ParamIndex, PreparedStatement> p) throws Exception {
        ParamIndex index = p.first();
        PreparedStatement statement = p.second();
        statement.setInt(index.get(), range.getFrom());
        statement.setInt(index.add(1).get(), range.getTo());
        statement.setString(index.add(2).get(), sex.name());
        return pair(index.add(3), statement);
      }
    };
  }
}
