package com.example.henriwilander.harkkaty;

public class User {

    private String firstName;
    private String surname;
    private int userID;
    private boolean status;
    private String password;
    int age;
    private String username;

    User(String firstName, String surname, String userName, int userID, boolean status, String password, int age){
        this.firstName = firstName;
        this.surname = surname;
        this.userID = userID;
        this.status = status;
        this.password = password;
        this.username = userName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName ;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() { return age;}

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {this.age=age; }

    public String getPassword() { return password;}

    public int getuserID() {
        return userID;
    }

    public boolean getStatus() {
        return status;
    }

}
