package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helper.itemhelper;
import com.example.model.product;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

public class listviewproduct extends AppCompatActivity {

    private ArrayList<product> productArray;
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    final Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewproduct);
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        String uid = fa.getCurrentUser().getUid();
        productArray = itemhelper.listitem(uid);
        final ListView it = findViewById(R.id.itemList);
        productListAdapter pla = new productListAdapter((Activity) c, productArray);
        it.setAdapter(pla);
        pla.notifyDataSetChanged();
        if(productArray.size() == 0) {
            Toast.makeText(getApplicationContext(), "List Not Found", Toast.LENGTH_SHORT).show();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.registerForContextMenu(it);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.detail_product, menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int pos = info.position;

        if (item.getItemId() == R.id.Editprod){
            String id = productArray.get(pos).getProdId();
            Intent i = new Intent(getBaseContext(), editproduct.class);
            i.putExtra("prodID",id);
            startActivity(i);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
}