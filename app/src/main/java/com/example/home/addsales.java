package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.model.product;
import com.example.model.tempProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class addsales extends AppCompatActivity {
    FirebaseFirestore ff;
    ImageView itmImg;
    Button sales;
    EditText qty;
    TextView price, totPrice,cekPrice,pn;
    private FirebaseAuth fa;
    final Context c = this;
    tempProduct tp = new tempProduct();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsales);

        fa = FirebaseAuth.getInstance();
        ff =FirebaseFirestore.getInstance();
        itmImg = findViewById(R.id.itemImg);
        sales = findViewById(R.id.btnaddsales);
        qty = findViewById(R.id.tvQuantity);
        price = findViewById(R.id.tvPrice);
        totPrice = findViewById(R.id.textView3);
        cekPrice = findViewById(R.id.textView4);
        pn = findViewById(R.id.prodName);
        final String pid = getIntent().getStringExtra("prodID");

        ff.collection("Product").document(pid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot ds, @Nullable FirebaseFirestoreException e) {
                Glide.with(addsales.this).load(ds.getString("imgUri")).into(itmImg);
                String imgs = ds.getString("imgUri");
                pn.setText(ds.getString("prodName"));
                price.setText(ds.getString("prodPrice"));
                tp.setImgUri(imgs);
            }
        });
        cekPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double prices = Double.parseDouble(price.getText().toString());
                int qtys = Integer.parseInt(qty.getText().toString());

                double total = prices * qtys;

                totPrice.setText(total+"" );
                totPrice.setVisibility(View.VISIBLE);
            }
        });

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodname = pn.getText().toString();
                String pprice = totPrice.getText().toString();
                String pQuantity = qty.getText().toString();

                DocumentReference dr = ff.collection("TempProduct").document(pid);
                String uID = fa.getUid();
                tp.setUserID(uID);
                tp.setProdId(pid);
                tp.setProdName(prodname);
                tp.setProdPrice(pprice);
                tp.setProdQuantity(pQuantity);

                dr.set(tp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(c,"Product Successfully Added",Toast.LENGTH_SHORT).show();
                            addSuccess();
                        }
                        else
                        {
                            Toast.makeText(c,"Failed! "+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addSuccess(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        b.setTitle("Successful");
        b.setMessage("Your Product has been Added..");

        b.setPositiveButton("Add another product?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), displaysales.class));
                Toast.makeText(getApplicationContext(),"Please add another product",Toast.LENGTH_SHORT).show();
            }
        });
        b.setNegativeButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(), pos_main.class));
            }
        });
        AlertDialog a = b.create();
        a.show();
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
}