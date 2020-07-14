package com.example.home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.model.product;
import com.example.model.sale;

import java.util.ArrayList;

public class invoiceListAdapter extends BaseAdapter {
    private Activity c;
    private ArrayList<sale> s;

    public invoiceListAdapter(Activity context, ArrayList<sale> p){
        c = context;
        this.s = p;
    }

    public int getCount(){return s.size();}
    public Object getItem(int i){return s.get(i);}
    public long getItemId(int i){return i;}

    public View getView(int position, View view, ViewGroup parent){

        LayoutInflater inflater = c.getLayoutInflater();
        View rv = inflater.inflate(R.layout.invoice_listview,null,true);

        TextView invid, name, date, total;
        invid = rv.findViewById(R.id.invid);
        name = rv.findViewById(R.id.custname);
        date = rv.findViewById(R.id.invdate);
        total = rv.findViewById(R.id.totalinv);

        sale sl = s.get(position);
        invid.setText("Invoive ID :"+sl.getId());
        name.setText("Details :"+sl.getName()+"("+sl.getPhone()+"-"+sl.getEmail()+")");
        date.setText("Date Invoice : "+sl.getDate());
        total.setText("Price: RM "+sl.getTotal());
       // pp.setText("Price: RM"+pro.getProdPrice());

        return rv;
    }
}
