package com.example.model;

public class tempProduct {
    String userID, prodId, prodName, prodPrice, prodQuantity,imgUri;

    public tempProduct(){};

    public tempProduct(String uid,String pid,String name,String price,String qty,String imgUri){
        this.userID=uid;
        this.prodId=pid;
        this.prodName=name;
        this.prodPrice=price;
        this.prodQuantity=qty;
        this.imgUri=imgUri;
    }

    public String getUserID(){
        return userID;
    }
    public String getProdId(){
        return prodId;
    }
    public String getProdName() {
        return prodName;
    }
    public String getProdPrice() {
        return prodPrice;
    }
    public String getProdQuantity() {
        return prodQuantity;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }
    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }
    public void setProdQuantity(String prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

}
