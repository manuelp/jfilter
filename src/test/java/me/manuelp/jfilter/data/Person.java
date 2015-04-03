package me.manuelp.jfilter.data;

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
    return getSurname().equals(person.getSurname())
        && getSex() == person.getSex();
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
    return "Person{" + "name='" + name + '\'' + ", surname='" + surname + '\''
        + ", age=" + age + ", sex=" + sex + '}';
  }
}
