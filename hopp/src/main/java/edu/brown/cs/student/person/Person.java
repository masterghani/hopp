package edu.brown.cs.student.person;

public abstract class Person {

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }
  private String userName = "";
  private String firstName = "";
  private String lastName = "";
  private String address = "";
  private String gender = "";
  private String phoneNumber = "";
  private String birthday = "";
  private String email = "";
  private String token = "";

  public Person(String token, String userName, String firstName, String lastName, String address, 
      String gender, String phoneNumber, String birthday, String email) {
    this.userName = userName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
    this.birthday = birthday;
    this.email = email;
    this.token = token;
  }
  
  public Person() {}

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getFirstName() {
    return this.firstName;
  }
  public String getLastName() {
    return this.lastName;
  }
  public String getAddress() {
    return this.address;
  }

  public String getGender() {
    return this.gender;
  }
  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public String getBirthday() {
    return this.birthday;
  }

  public String getEmail() {
    return this.email;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public void setBirthday (String birthday) {
    this.birthday = birthday;
  }
  public void setEmail (String email) {
    this.email = email;
  }


}
