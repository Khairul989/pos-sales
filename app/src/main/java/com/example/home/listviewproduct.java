package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

import static com.example.home.signup.TAG;

public class listviewproduct extends AppCompatActivity {

    private ArrayList<product> productArray = new ArrayList<>();
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    final Context c = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewproduct);
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        final String uid = fa.getCurrentUser().getUid();
        final ListView it = findViewById(R.id.itemList);

        ff.collection("Product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("userID");
                        if (uid.equals(userID)) {
                            String pid = doc.getString("prodId");
                            String pd = doc.getString("prodDesc");
                            String pn = doc.getString("prodName");
                            String pp = doc.getString("prodPrice");
                            String pc = doc.getString("prodCategory");
                            String pq = "";
                            String pimg = doc.getString("imgUri");
                            product p = new product(userID, pid, pn, pd, pp, pc,pq, pimg);
                            productArray.add(p);
                        }
                    }
                    productListAdapter pla = new productListAdapter(listviewproduct.this, productArray);
                    it.setAdapter(pla);
                    pla.notifyDataSetChanged();
                    if(productArray.size() == 0) {
                        Toast.makeText(c, "List Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting documents", task.getException());
                }
            }
        });
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
        if(item.getItemId() == R.id.deleteprod){
            AlertDialog.Builder b = new AlertDialog.Builder(this,R.style.MyDialogTheme);
            String id = productArray.get(pos).getProdId();
            final DocumentReference dr = ff.collection("Product").document(id);

            b.setTitle("Delete Product");
            b.setMessage("Do you confirm to delete this product?");

            b.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(listviewproduct.this, "Product Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Products.class));
                            }
                            else{
                                Toast.makeText(listviewproduct.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            b.setNegativeButton("Cancel",null);

            b.create().show();

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