package com.example.model;

public class sale {
    String name,id,email,phone,total,uid,date,time;
    public sale(){};
    public sale(String uid,String n,String i, String e,String p,String t,String d,String ti){
        this.name=n;
        this.id=i;
        this.email=e;
        this.phone=p;
        this.total=t;
        this.uid=uid;
        this.date=d;
        this.time=ti;
    }


    public String getId() {
        return id;
    }
    public void setId(String n) {
        this.id = n;
    }

    public String getName() {
        return name;
    }
    public void setName(String n) {
        this.name = n;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String n) {
        this.email = n;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String n) {
        this.phone = n;
    }

    public String getTotal() {
        return total;
    }
    public void setTotal(String n) {
        this.total = n;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String n) {
        this.uid = n;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String n) {
        this.date = n;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String n) {
        this.time = n;
    }
}
