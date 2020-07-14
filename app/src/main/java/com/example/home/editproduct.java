package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class editproduct extends AppCompatActivity {
    FirebaseFirestore ff;
    EditText prodName, prodDesc, PPUnit;
    Spinner ProdCategory;
    Button btnupdate, btnexit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduct);
        prodName = findViewById(R.id.pName);
        prodDesc = findViewById(R.id.pDesc);
        PPUnit = findViewById(R.id.pPrice);
        ProdCategory = findViewById(R.id.pCateg);
        btnupdate = findViewById(R.id.btnUpdate);
        btnexit = findViewById(R.id.btnExit);
        ff = FirebaseFirestore.getInstance();
        final String prodid = getIntent().getStringExtra("prodID");

        ff.collection("Product").document(prodid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                prodName.setText(documentSnapshot.getString("prodName"));
                prodDesc.setText(documentSnapshot.getString("prodDesc"));
                PPUnit.setText(documentSnapshot.getString("prodPrice"));
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference dr = ff.collection("Product").document(prodid);

                product p = new product();

                p.setProdName(prodName.getText().toString());
                p.setProdDesc(prodDesc.getText().toString());
                p.setProdPrice(PPUnit.getText().toString());
                p.setProdCategory(ProdCategory.getSelectedItem().toString());

                Map<String, Object> pro = new HashMap<>();
                pro.put("prodCategory", p.getProdCategory());
                pro.put("prodDesc",p.getProdDesc());
                pro.put("prodName",p.getProdName());
                pro.put("prodPrice", p.getProdPrice());

                dr.update(pro).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(editproduct.this, "Product Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), listviewproduct.class));
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(editproduct.this, "Update Failed", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), listviewproduct.class));
                            }
                        });
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
}