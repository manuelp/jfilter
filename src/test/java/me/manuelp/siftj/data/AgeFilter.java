package me.manuelp.siftj.data;

import com.googlecode.totallylazy.Pair;
import me.manuelp.siftj.Filter;
import me.manuelp.siftj.sql.BindParamsF;
import me.manuelp.siftj.sql.ParamIndex;
import me.manuelp.siftj.sql.SqlFilter;
import me.manuelp.siftj.sql.WhereClause;

import java.sql.PreparedStatement;

import static com.googlecode.totallylazy.Pair.pair;

public class AgeFilter extends Filter<Person> implements SqlFilter {
  private final int age;

  public AgeFilter(int age) {
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean matches(Person p) {
    return p.getAge() == age;
  }

  @Override
  public WhereClause whereClause() {
    return new WhereClause("t.age=?");
  }

  @Override
  public BindParamsF bindParameters() {
    return new BindParamsF() {
      @Override
      public Pair<ParamIndex, PreparedStatement> call(
          Pair<ParamIndex, PreparedStatement> p) throws Exception {
        ParamIndex index = p.first();
        PreparedStatement s = p.second();
        s.setInt(index.get(), age);
        return pair(index.succ(), s);
      }
    };
  }
}
