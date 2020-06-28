package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProduct extends AppCompatActivity{
    EditText prodName, prodDesc, PPUnit, prodQuantity;
    Spinner ProdCategory;
    Button btnAdd;
    FirebaseFirestore ff;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);
        prodName = findViewById(R.id.prodName);
        prodDesc = findViewById(R.id.prodDesc);
        PPUnit = findViewById(R.id.PPUnit);
        prodQuantity = findViewById(R.id.prodQuantity);
        ProdCategory = findViewById(R.id.prodCategory);
        btnAdd = findViewById(R.id.btnAdd);
        pb = findViewById(R.id.progressBar3);
        ff = FirebaseFirestore.getInstance();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pn = prodName.getText().toString();
                String pdesc = prodDesc.getText().toString();
                String PUnit = PPUnit.getText().toString();
                String pQuantity = prodQuantity.getText().toString();
                String pCateg = ProdCategory.getSelectedItem().toString();

                if(TextUtils.isEmpty(pn) || TextUtils.isEmpty(pdesc) ||TextUtils.isEmpty(PUnit) || TextUtils.isEmpty(pQuantity)|| TextUtils.isEmpty(pCateg)){
                    Toast.makeText(AddProduct.this, "Please make sure all the field is filled in!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseAuth fa = FirebaseAuth.getInstance();
                    String uID = fa.getUid();
                    String pID = ff.collection("Product").document().getId();
                    DocumentReference dr = ff.collection("Product").document(pID);

                    product p = new product(uID,pID,pn,pdesc,PUnit,pQuantity,pCateg);

                    pb.setVisibility(View.VISIBLE);
                    dr.set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddProduct.this,"Prodcut Successfully Added",Toast.LENGTH_SHORT).show();
                                addSuccess();
                                pb.setVisibility(View.GONE);
                            }
                            else
                            {
                                Toast.makeText(AddProduct.this,"Failed! "+task.getException(),Toast.LENGTH_SHORT).show();
                                pb.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
    public void addSuccess(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        b.setTitle("Successful");
        b.setMessage("Your Product has been Added..");

        b.setPositiveButton("Add another product?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddProduct.this,"Please add another product",Toast.LENGTH_SHORT).show();
                prodName.setText("");
                prodDesc.setText("");
                prodQuantity.setText("");
                PPUnit.setText("");
            }
        });
        b.setNegativeButton("Back to Homepage", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        AlertDialog a = b.create();
        a.show();
    }
}