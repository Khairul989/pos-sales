package com.example.home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.model.mostsoldprod;
import com.example.model.product;
import com.example.model.sale;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import static com.example.home.signup.TAG;

public class report extends AppCompatActivity {
    private ArrayList<sale> invArray;
    private ArrayList<mostsoldprod> mostarray;
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    private product p;
    private sale s;
    private String pid,uid;
    private mostsoldprod m;
    private int max = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        uid = fa.getCurrentUser().getUid();
        invArray = new ArrayList<>();
        mostarray = new ArrayList<>();

        ff.collection("Sale").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    double total_year_sale = 0.0;
                    double total_month_sale =0.0;
                    double total_today_sale =0.0;

                    LocalDate date = LocalDate.now();
                    System.out.println(date);
                    String m = String.valueOf(date);
                    String sm[] = m.split("-");
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String userID = doc.getString("uid");
                            if (uid.equals(userID)) {
                                String d =doc.getString("date");
                                System.out.println(d);
                                s = new sale(uid,doc.getString("name"),doc.getString("id"),doc.getString("email"),
                                        doc.getString("phone"),doc.getString("total"),d,doc.getString("time"));
                                invArray.add(s);
                                total_year_sale = total_year_sale +  Double.parseDouble(s.getTotal());
                                String dm[] = d.split("/");
                                //sm[0]=dm[0];
                                if(sm[1].equals(dm[0])){
                                    total_month_sale= total_month_sale + Double.parseDouble(s.getTotal());
                                }
                                if(sm[1].equals(dm[0]) && sm[2].equals(dm[1]) && sm[0].equals(dm[2])){
                                    total_today_sale= total_today_sale + Double.parseDouble(s.getTotal());
                                }
                            }
                        }
                        if (invArray.size()==0){
                            Toast.makeText(report.this, "No Invoice", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else
                        {
                            String mtotal = String.valueOf(total_month_sale);
                            TextView mo = findViewById(R.id.monthsale);
                            TextView datess = findViewById(R.id.dates);
                            mo.setVisibility(View.VISIBLE);
                            datess.setVisibility(View.VISIBLE);
                            mo.setText("Total Sale For This Month : RM "+ mtotal);
                            datess.setText(s.getDate());
                        }
                    }
                }
        });

        ff.collection("SaleProduct").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("uid");
                        if (uid.equals(userID)) {
                            m = new mostsoldprod(doc.getString("prodid"),doc.getString("prodName"),doc.getString("soldQty"));
                            mostarray.add(m);
                        }
                    }

                    if(mostarray.size() == 0){
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                    else
                    {
                        for(int i=0; i<mostarray.size();i++)
                        {
                            if (mostarray.get(i).getQty() > max)
                            {
                                max = mostarray.get(i).getQty();
                                pid = mostarray.get(i).getProdid();
                            }
                        }

                        ff.collection("Product").document(pid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot d, @Nullable FirebaseFirestoreException e) {
                                p = new product(d.getString("userID"),d.getString("prodId"),d.getString("prodName"),d.getString("prodDesc"),d.getString("prodPrice"),d.getString("prodCategory"),d.getString("imgUri"));

                                TextView pn,pp,pd,sq,ts;
                                ImageView mostImg;
                                mostImg = findViewById(R.id.mostsale);
                                pn = findViewById(R.id.prodName);
                                pp = findViewById(R.id.prodPrice);
                                pd = findViewById(R.id.prodDesc);
                                sq = findViewById(R.id.soldQty);
                                ts = findViewById(R.id.totSale);

                                mostImg.setVisibility(View.VISIBLE);
                                pn.setVisibility(View.VISIBLE);
                                pp.setVisibility(View.VISIBLE);
                                pd.setVisibility(View.VISIBLE);
                                sq.setVisibility(View.VISIBLE);
                                ts.setVisibility(View.VISIBLE);

                                Glide.with(report.this).load(p.getImgUri()).into(mostImg);
                                pn.setText("Product: "+p.getProdName());
                                pp.setText("Price (RM): "+p.getProdPrice());
                                pd.setText("Description: "+p.getProdDesc());
                                sq.setText("Sold Quantity: "+max+"");

                                double totsales = max * Double.parseDouble(p.getProdPrice());

                                ts.setText("Total Sale (RM): "+totsales+"");
                            }
                        });
                    }
                }
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