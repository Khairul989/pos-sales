package com.example.model;

import java.io.Serializable;

public class product implements Serializable {
    String userID, prodId, prodName, prodDesc, prodPrice, prodCategory, pQuantity, imgUri;

    public product(){}

    public product(String userID, String prodId, String prodName, String prodDesc, String prodPrice, String prodCategory, String pQuantity, String img) {
        this.userID = userID;
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodCategory = prodCategory;
        this.pQuantity = pQuantity;
        imgUri = img;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
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
                ", prodCategory='" + prodCategory + '\'' +
                '}';
    }
}
