package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class editproduct extends AppCompatActivity {
    FirebaseFirestore ff;
    EditText prodName, prodDesc, PPUnit, prodQuantity;
    Spinner ProdCategory;
    Button btnupdate, exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editproduct);
        prodName = findViewById(R.id.pName);
        prodDesc = findViewById(R.id.pDesc);
        PPUnit = findViewById(R.id.pPrice);
        prodQuantity = findViewById(R.id.pQuantity);
        ProdCategory = findViewById(R.id.pCateg);
        btnupdate = findViewById(R.id.btnUpdate);
        exit = findViewById(R.id.btnExit);
        ff = FirebaseFirestore.getInstance();

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prodid = getIntent().getStringExtra("prodID");
                DocumentReference dr = ff.collection("Product").document(prodid);

                System.out.println("prod id: "+prodid);
                product p = new product();

                p.setProdName(prodName.getText().toString());
                p.setProdDesc(prodDesc.getText().toString());
                p.setProdPrice(PPUnit.getText().toString());
                p.setProdQuantity(prodQuantity.getText().toString());
                p.setProdCategory(ProdCategory.getSelectedItem().toString());

                Map<String, Object> pro = new HashMap<>();
                pro.put("prodCategory", p.getProdCategory());
                pro.put("prodDesc",p.getProdDesc());
                pro.put("prodName",p.getProdName());
                pro.put("prodPrice", p.getProdPrice());
                pro.put("prodQuantity",p.getProdQuantity());

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


    }
}