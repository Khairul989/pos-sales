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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity{
    EditText prodName, prodDesc, BPPUnit, PPUnit, prodQuantity, Weight;
    TextView totPrice;
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
        BPPUnit = findViewById(R.id.BPPUnit);
        PPUnit = findViewById(R.id.PPUnit);
        prodQuantity = findViewById(R.id.prodQuantity);
        totPrice = findViewById(R.id.totPrice);
        Weight = findViewById(R.id.weight);
        ProdCategory = findViewById(R.id.prodCategory);
        btnAdd = findViewById(R.id.btnAdd);
        pb = findViewById(R.id.progressBar3);
        ff = FirebaseFirestore.getInstance();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                double sum = Double.parseDouble(PPUnit.getText().toString()) * Double.parseDouble(prodQuantity.getText().toString());
                totPrice.setText("Total Price: RM" + sum);
            }
        });
        //hi

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pn = prodName.getText().toString();
                String pdesc = prodDesc.getText().toString();
                String BUnit = BPPUnit.getText().toString();
                String PUnit = PPUnit.getText().toString();
                String pQuantity = prodQuantity.getText().toString();
                String w = Weight.getText().toString();
                String pCateg = ProdCategory.getSelectedItem().toString();
                String total = totPrice.getText().toString();

                if(TextUtils.isEmpty(pn) || TextUtils.isEmpty(pdesc) || TextUtils.isEmpty(BUnit) || TextUtils.isEmpty(PUnit) || TextUtils.isEmpty(pQuantity) || TextUtils.isEmpty(w) || TextUtils.isEmpty(pCateg)){
                    Toast.makeText(AddProduct.this, "Please make sure all the field is filled in!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseAuth fa = FirebaseAuth.getInstance();
                    String uID = fa.getUid();
                    DocumentReference dr = ff.collection("Product").document();
                    Map<String,Object> product = new HashMap<>();
                    product.put("User ID",uID);
                    product.put("Product ID",dr.getId());
                    product.put("Product Name", pn);
                    product.put("Product Description",pdesc);
                    product.put("Buy Price Per Unit", BUnit);
                    product.put("Price Per Unit",PUnit);
                    product.put("Quantity", pQuantity);
                    product.put("Product Weight",w);
                    product.put("Product Category", pCateg);
                    product.put("Total Price",total);

                    pb.setVisibility(View.VISIBLE);
                    dr.set(product).addOnCompleteListener(new OnCompleteListener<Void>() {
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