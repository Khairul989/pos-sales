package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class displaysales extends AppCompatActivity {
    private ArrayList<product> pArray;
    private product p;
    FirebaseFirestore ff;
    FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaysales);

        fa = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        final ListView sl = findViewById(R.id.saleView);
        pArray = new ArrayList<>();
        final String uid = fa.getCurrentUser().getUid();

        ff.collection("Product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot d: task.getResult())
                    {
                        String id = d.getString("userID");
                        if(uid.equals(id)){
                            String pn = d.getString("prodName");
                            String pp = d.getString("prodPrice");
                            String pc = d.getString("prodCategory");
                            String pid = d.getString("prodId");
                            String pimg = d.getString("imgUri");

                            p = new product();

                            p.setProdId(pid);
                            p.setProdCategory(pc);
                            p.setUserID(id);
                            p.setProdPrice(pp);
                            p.setProdName(pn);
                            p.setImgUri(pimg);
                            pArray.add(p);
                        }
                    }
                    displaySaleAdapter dsa = new displaySaleAdapter(displaysales.this, pArray);
                    sl.setAdapter(dsa);
                    dsa.notifyDataSetChanged();
                    if(pArray.size() == 0) {
                        Toast.makeText(displaysales.this, "List Not Found", Toast.LENGTH_SHORT).show();
                    }

                    sl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int pos = parent.getPositionForView(view);
                            String pid = pArray.get(pos).getProdId();
                            Intent i = new Intent(getBaseContext(), addsales.class);
                            i.putExtra("prodID",pid);
                            startActivity(i);
                        }
                    });
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = super.getMenuInflater();
        i.inflate(R.menu.display_sale,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.totSale){
            Toast.makeText(this, "Total Sale: ", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}