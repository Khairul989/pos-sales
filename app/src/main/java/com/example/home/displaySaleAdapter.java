package com.example.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.model.product;

import java.util.ArrayList;

public class displaySaleAdapter extends BaseAdapter {
    private Activity c;
    private ArrayList<product> p;

    public displaySaleAdapter(Activity c, ArrayList<product> p) {
        this.c = c;
        this.p = p;
    }

    @Override
    public int getCount() {
        return p.size();
    }

    @Override
    public Object getItem(int i) {
        return p.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = c.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.sale_view, null, true);
        //displaysales
        TextView sn, sp, sq;
        ImageView imgsale;

        sn = rowView.findViewById(R.id.saleName);
        sp = rowView.findViewById(R.id.salePrice);
        sq = rowView.findViewById(R.id.tvCat);
        imgsale = rowView.findViewById(R.id.saleImage);

        product sale = p.get(position);
        sn.setText("Item: "+sale.getProdName());
        sp.setText("Price: RM"+sale.getProdPrice());
        sq.setText("Category: "+sale.getProdCategory());

        Glide.with(c).load(sale.getImgUri()).into(imgsale);

        return rowView;
    }

}
