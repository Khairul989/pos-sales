package com.example.model;

public class product {
    String userID, prodId, prodName, prodDesc, prodPrice, prodQuantity, prodCategory;

    public product(){}

    public product(String userID, String prodId, String prodName, String prodDesc, String prodPrice, String prodQuantity, String prodCategory) {
        this.userID = userID;
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodQuantity = prodQuantity;
        this.prodCategory = prodCategory;
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

    public String getProdDesc() {
        return prodDesc;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public String getProdQuantity() {
        return prodQuantity;
    }

    public String getProdCategory() {
        return prodCategory;
    }
    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public void setProdQuantity(String prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }

    @Override
    public String toString() {
        return "product{" +
                "userID='" + userID + '\'' +
                ", prodId='" + prodId + '\'' +
                ", prodName='" + prodName + '\'' +
                ", prodDesc='" + prodDesc + '\'' +
                ", prodPrice='" + prodPrice + '\'' +
                ", prodQuantity='" + prodQuantity + '\'' +
                ", prodCategory='" + prodCategory + '\'' +
                '}';
    }
}
