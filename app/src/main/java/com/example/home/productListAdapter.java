package com.example.home;

import android.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.model.product;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class productListAdapter extends BaseAdapter {

    private Activity c;
    private ArrayList<product> p;

    public productListAdapter(Activity context, ArrayList<product> p){
        c = context;
        this.p = p;
    }

    public int getCount(){return p.size();}
    public Object getItem(int i){return p.get(i);}
    public long getItemId(int i){return i;}

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = c.getLayoutInflater();
        View rv = inflater.inflate(R.layout.product_listview,null,true);

        //listviewproduct
        TextView pn, pc, pp;
        ImageView pi;
        pn = rv.findViewById(R.id.ProductName);
        pc = rv.findViewById(R.id.ProductCategory);
        pp = rv.findViewById(R.id.ProductPrice);
        pi = rv.findViewById(R.id.prodImg);

        product pro = p.get(position);
        pn.setText("Item: "+pro.getProdName());
        pc.setText("Category: "+pro.getProdCategory());
        pp.setText("Price: RM"+pro.getProdPrice());
        Glide.with(c).load(pro.getImgUri()).into(pi);

        return rv;
    }
}
