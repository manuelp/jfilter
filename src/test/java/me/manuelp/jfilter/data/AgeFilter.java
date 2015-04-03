package me.manuelp.jfilter.data;

import me.manuelp.jfilter.Filter;
import me.manuelp.jfilter.sql.SqlFilter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgeFilter extends Filter<Person> implements SqlFilter {
  private final int age;

  public AgeFilter(int age) {
    super("age");
    this.age = age;
  }

  public static AgeFilter ageFilter(int value) {
    return new AgeFilter(value);
  }

  @Override
  public boolean match(Person p) {
    return p.getAge() == age;
  }

  @Override
  public String whereClause() {
    return "t.age=?";
  }

  @Override
  public void bindParameter(PreparedStatement statement, int index)
      throws SQLException {
    statement.setInt(index, age);
  }
}
