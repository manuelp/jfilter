package me.manuelp.jfilter;

import me.manuelp.jfilter.validations.NotNull;

public class Person {
  private final String name;
  private final String surname;
  private final int age;
  private final Sex sex;

  public Person(String name, String surname, int age, Sex sex) {
    NotNull.check(name, surname, sex);
    this.name = name;
    this.surname = surname;
    this.age = age;
    this.sex = sex;
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public int getAge() {
    return age;
  }

  public Sex getSex() {
    return sex;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Person person = (Person) o;

    if (getAge() != person.getAge())
      return false;
    if (!getName().equals(person.getName()))
      return false;
    if (!getSurname().equals(person.getSurname()))
      return false;
    return getSex() == person.getSex();

  }

  @Override
  public int hashCode() {
    int result = getName().hashCode();
    result = 31 * result + getSurname().hashCode();
    result = 31 * result + getAge();
    result = 31 * result + getSex().hashCode();
    return result;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Person{");
    sb.append("name='").append(name).append('\'');
    sb.append(", surname='").append(surname).append('\'');
    sb.append(", age=").append(age);
    sb.append(", sex=").append(sex);
    sb.append('}');
    return sb.toString();
  }
}
