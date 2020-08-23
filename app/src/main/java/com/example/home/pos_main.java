package com.example.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.model.product;
import com.example.model.sale;
import com.example.model.saleProduct;
import com.example.model.tempProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

import static com.example.home.signup.TAG;

public class pos_main extends AppCompatActivity {
    private ArrayList<tempProduct> productArray;
    private ArrayList<saleProduct> spa = new ArrayList<>();
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    final Context c = this;
    ListView it;
    EditText txtname,txtemail,txtphone;
    sale sales = new sale();
    saleProduct sp = new saleProduct();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_main);
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        final String uid = fa.getCurrentUser().getUid();
        it = findViewById(R.id.itemList);
        productArray = new ArrayList<>();
        ff.collection("TempProduct").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                double total = 0.0;
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("userID");
                        if (uid.equals(userID)) {
                            String pid = doc.getString("prodId");
                            String pn = doc.getString("prodName");
                            String pp = doc.getString("prodPrice");
                            String pi = doc.getString("imgUri");
                            String pq = doc.getString("prodQuantity");
                            tempProduct p = new tempProduct(userID, pid, pn, pp, pq,pi);
                            productArray.add(p);
                            double price = Double.parseDouble(pp);
                            total = total + price;
                        }
                    }
                    salesAdapater pla = new salesAdapater(pos_main.this, productArray);
                    it.setAdapter(pla);
                    pla.notifyDataSetChanged();
                    it.setOnItemClickListener(listClick);
                    if(productArray.size() == 0) {
                        Toast.makeText(c, "List Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting documents", task.getException());

                }
                EditText price = (EditText)findViewById(R.id.tvTotalPrice);
                price.setText(total+"");
            }
        });
        ff.collection("user").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                txtname = (EditText)findViewById(R.id.tvCust);
                txtemail = (EditText)findViewById(R.id.tvEmail);
                txtphone=(EditText)findViewById(R.id.tvNo);
                txtname.setText(documentSnapshot.getString("fullname"));
                txtemail.setText(documentSnapshot.getString("email"));
                txtphone.setText(documentSnapshot.getString("phone"));
            }
        });
        ff.collection("Sale").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                sales.setTotal(documentSnapshot.getString("total"));
            }
        });

        ff.collection("SaleProduct").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot d:task.getResult()){
                        if (fa.getCurrentUser().getUid().equals(d.getString("uid"))){
                            sp = new saleProduct(d.getString("uid"),d.getString("prodid"),d.getString("saleID"),d.getString("prodname"),d.getString("price"),d.getString("soldQty"));
                            spa.add(sp);
                        }
                    }
                }
            }
        });

        Button btnadd = findViewById(R.id.btnproceed);
        btnadd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txttotal = (EditText)findViewById(R.id.tvTotalPrice);
                txtname = (EditText)findViewById(R.id.tvCust);
                txtemail = (EditText)findViewById(R.id.tvEmail);
                txtphone=(EditText)findViewById(R.id.tvNo);

                final String name = txtname.getText().toString();
                final String total = txttotal.getText().toString();
                final String email = txtemail.getText().toString();
                final String phone = txtphone.getText().toString();

                final String SaleID = ff.collection("Sale").document().getId();
                final DocumentReference dr = ff.collection("Sale").document(uid);
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                final String strDate = dateFormat.format(date);
                Date time = Calendar.getInstance().getTime();
                DateFormat times = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
                final String strTime = times.format(time);
                double tot = 0.0;
                if(sales.getTotal() == null) {
                    tot = Double.parseDouble(total) + 0;
                }
                else
                {
                    tot = Double.parseDouble(total) + Double.parseDouble(sales.getTotal());
                }
                sales = new sale(uid, name, SaleID, email, phone, String.valueOf(tot), strDate, strTime);
                dr.set(sales).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            double p =0.0;
                            int q=0;
                            for(int i=0;i<productArray.size();i++){
                                if(spa.size() == 0){
                                    p = Double.parseDouble(productArray.get(i).getProdPrice())+0;
                                    q = Integer.parseInt(productArray.get(i).getProdQuantity())+0;
                                    sp = new saleProduct(uid,productArray.get(i).getProdId(),SaleID,productArray.get(i).getProdName(),String.valueOf(p),String.valueOf(q));
                                    DocumentReference dr = ff.collection("SaleProduct").document(productArray.get(i).getProdId());
                                    dr.set(sp);
                                }
                                else if (spa.size() != 0)
                                {
                                    sp = new saleProduct(uid,productArray.get(i).getProdId(),SaleID,productArray.get(i).getProdName(),productArray.get(i).getProdPrice(),productArray.get(i).getProdQuantity());
                                    DocumentReference dr = ff.collection("SaleProduct").document(productArray.get(i).getProdId());
                                    dr.set(sp);

                                    for (int k=0; k<spa.size();k++){
                                        if(spa.get(k).getProdid().equals(productArray.get(i).getProdId())){
                                            p = Double.parseDouble(productArray.get(i).getProdPrice())+Double.parseDouble(spa.get(k).getPrice());
                                            q = Integer.parseInt(productArray.get(i).getProdQuantity())+Integer.parseInt(spa.get(k).getSoldQty());
                                            sp = new saleProduct(uid,productArray.get(i).getProdId(),SaleID,productArray.get(i).getProdName(),String.valueOf(p),String.valueOf(q));
                                            dr.set(sp);
                                        }
                                    }
                                }
                                DocumentReference del = ff.collection("TempProduct").document(productArray.get(i).getProdId());
                                del.delete();
                                Toast.makeText(c,"Product Successfully Added",Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(c,"Product Successfully Added",Toast.LENGTH_SHORT).show();
                            Intent intentAdd = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intentAdd);
                        }
                        else
                        {
                            Toast.makeText(c,"Failed! "+task.getException(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // get the menu inflater
        MenuInflater inflater = super.getMenuInflater();
        // inflate the menu using our XML menu file id, options_menu
        inflater.inflate(R.menu.menu_pos, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_prod:
                startActivity(new Intent(getApplicationContext(),displaysales.class));
                return true;
        }
        return false;
    }
    private AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final product prod = (product) it.getItemAtPosition(position);
            Toast.makeText(c, "You have selected "+prod.getProdName(), Toast.LENGTH_SHORT).show();
            AlertDialog.Builder b = new AlertDialog.Builder(pos_main.this, R.style.MyDialogTheme);
            b.setTitle("Delete product?");
            b.setMessage("Your added product will be deleted");

            b.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DocumentReference del = ff.collection("TempProduct").document(prod.getProdId());
                    del.delete();
                    startActivity(new Intent(getApplicationContext(), pos_main.class));
                }
            });
            b.setNegativeButton("Cancel", null);
            b.create().show();
        }
    };
}