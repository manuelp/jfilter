package me.manuelp.siftj.data;

import fj.P2;
import me.manuelp.siftj.Filter;
import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgeFilter implements Filter<Person>, SqlFilter {
  private final int age;

  public AgeFilter(int age) {
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public Boolean f(Person p) {
    return p.getAge() == age;
  }

  @Override
  public WhereClause whereClause() {
    return WhereClause.whereClause("t.age=?");
  }

  @Override
  public BindParamsF bindParameter() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        p._2().setInt(p._1().get(), age);
        return p.map1(ParamIndex.succF());
      }
    };
  }

}
