package com.example.model;

public class mostsoldprod {
    String prodid, prodname;
    int qty;

    public mostsoldprod(){}

    public mostsoldprod(String id, String n, String q){
        this.prodid=id;
        this.prodname=n;
        this.qty= Integer.parseInt(q);
    }

    public int getQty() {
        return qty;
    }

    public String getProdid() {
        return prodid;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

}
