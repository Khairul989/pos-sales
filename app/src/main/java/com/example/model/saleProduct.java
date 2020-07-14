package com.example.model;

public class saleProduct {

    String Uid,prodid, SaleID, prodname, price, SoldQty;
    public saleProduct(){};
    public saleProduct(String Uid,String prodid, String SaleID,String prodname, String price, String SoldQty){
        this.Uid=Uid;
        this.prodid=prodid;
        this.SaleID=SaleID;
        this.prodname=prodname;
        this.SoldQty=SoldQty;
        this.price=price;
    }

    public String getUid() {
        return Uid;
    }
    public void setUid(String n) {
        this.Uid = n;
    }

    public String getProdid() {
        return prodid;
    }
    public void setProdid(String n) {
        this.prodid = n;
    }

    public String getSaleID() {
        return SaleID;
    }
    public void setSaleID(String n) { this.SaleID = n; }

    public String getProdname() {
        return prodname;
    }
    public void setProdname(String n) {
        this.prodname = n;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String n) {
        this.price = n;
    }

    public String getSoldQty() {
        return SoldQty;
    }
    public void setSoldQty(String n) {
        this.SoldQty = n;
    }
}
