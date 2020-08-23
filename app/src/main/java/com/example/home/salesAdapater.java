package com.example.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.product;
import com.example.model.tempProduct;

import java.util.ArrayList;

public class salesAdapater extends BaseAdapter {
    private Activity c;
    private ArrayList<tempProduct> p;

    public salesAdapater(Activity context, ArrayList<tempProduct> p){
        c = context;
        this.p = p;
    }

    public int getCount(){return p.size();}
    public Object getItem(int i){return p.get(i);}
    public long getItemId(int i){return i;}

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = c.getLayoutInflater();
        View rv = inflater.inflate(R.layout.sale_view,null,true);

        //listviewproduct
        TextView pn, pq, pp;
        ImageView pi;
        pn = rv.findViewById(R.id.saleName);
        pq = rv.findViewById(R.id.tvCat);
        pp = rv.findViewById(R.id.salePrice);
        pi = rv.findViewById(R.id.saleImage);

        tempProduct pro = p.get(position);
        pn.setText("Item: "+pro.getProdName());
        pp.setText("Price: RM"+pro.getProdPrice());
        Glide.with(c).load(pro.getImgUri()).into(pi);

        return rv;
    }
}
