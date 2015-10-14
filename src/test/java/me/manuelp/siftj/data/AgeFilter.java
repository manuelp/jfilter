package me.manuelp.siftj.data;

import fj.P2;
import me.manuelp.siftj.Filter;
import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static fj.P.p;

public class AgeFilter implements Filter<Person>, SqlFilter {
  private final int age;

  private AgeFilter(int age) {
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
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public P2<ParamIndex, PreparedStatement> f(
          P2<ParamIndex, PreparedStatement> p) throws SQLException {
        ParamIndex index = p._1();
        PreparedStatement s = p._2();
        s.setInt(index.get(), age);
        return p(index.succ(), s);
      }
    };
  }
}
