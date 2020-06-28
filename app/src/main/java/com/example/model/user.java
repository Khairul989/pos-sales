package com.example.model;

public class user {
    String id,fullname,email,phone,gender;

    public user(String i,String f,String e, String p, String g ){
        id = i;
        fullname = f;
        email = e;
        phone = p;
        gender = g;
    }

    public user() {

    }

    public String getFullname(){
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public String getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }

}
