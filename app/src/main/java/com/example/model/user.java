package com.example.model;

public class user {
    String id,fullname,email,phone,gender, imageUri;

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public user(String i, String f, String e, String p, String g, String img){
        imageUri = img;
        id = i;
        fullname = f;
        email = e;
        phone = p;
        gender = g;
    }

    public String getImageUri() {
        return imageUri;
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
