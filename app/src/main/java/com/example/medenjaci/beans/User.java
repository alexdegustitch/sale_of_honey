package com.example.medenjaci.beans;

public class User {

    private String username;
    private String password;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String address;
    private String type;

    public User() {

    }

    public User(String username, String password, String firstName, String lastName, String phone, String address, String type) {
        this.username = username;
        this.password = password;
        this.first_name = firstName;
        this.last_name = lastName;
        this.phone_number = phone;
        this.address = address;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
