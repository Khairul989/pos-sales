package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.product;
import com.example.model.sale;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.home.signup.TAG;

public class viewInvoiceDetail extends AppCompatActivity {
    private ArrayList<sale> invArray;
    private ArrayList<product> productArray;
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    final Context c = this;
    String inv;
    ListView it;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice_detail);

        inv= getIntent().getStringExtra("id");

        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        final String uid = fa.getCurrentUser().getUid();
        //it = findViewById(R.id.invlist);
        invArray = new ArrayList<>();
        ff.collection("Sale").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("uid");
                        if (uid.equals(userID)) {
                            String invid =doc.getString("id");
                            if(invid.equals(inv)){
                            sale s = new sale(uid,doc.getString("name"),invid,doc.getString("email"),
                                    doc.getString("phone"),doc.getString("total"),doc.getString("date"),doc.getString("time"));
                            invArray.add(s);
                                TextView invidtv = findViewById(R.id.invid);
                                invidtv.setText(s.getId());
                                TextView invdate = findViewById(R.id.invDate);
                                invdate.setText(s.getDate());
                                TextView custphone = findViewById(R.id.custphone);
                                custphone.setText(s.getPhone());
                                TextView custname = findViewById(R.id.custname);
                                custname.setText(s.getName());
                                TextView custemail = findViewById(R.id.custemail);
                                custemail.setText(s.getEmail());
                                TextView totalprice = findViewById(R.id.totalprice);
                                totalprice.setText(s.getTotal());
                            }
                        }
                    }
                    if(invArray.size() == 0) {
                        Toast.makeText(c, "List Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        TableLayout t1;
        final TableLayout tl = (TableLayout) findViewById(R.id.tablesale);
        TableRow tr_head = new TableRow(c);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.GRAY);        // part1
        tr_head.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        final TextView label_hello = new TextView(c);
        label_hello.setId(20);
        label_hello.setText("Product (Quantity)-Price");
        label_hello.setTextColor(Color.WHITE);          // part2
        //label_hello.setPadding(5, 5, 5, 5);
        tr_head.addView(label_hello);// add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.MATCH_PARENT));

        productArray = new ArrayList<>();
        ff.collection("SaleProduct").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int i=0;
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("uid");
                        if (uid.equals(userID)) {
                            String saleid =doc.getString("saleID");
                            if(saleid.equals(inv)){
                                Toast.makeText(c, "Found", Toast.LENGTH_SHORT).show();
                                TableRow tr_head = new TableRow(c);
                                tr_head.setId(i+1);
                                tr_head.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                                TextView data = new TextView(c);
                                data.setId(i+111);
                                data.setText(doc.getString("prodname")+" ("+doc.getString("soldQty")+") -"+doc.getString("price"));
                                data.setTextColor(Color.BLACK);          // part2
                                //label_hello.setPadding(5, 5, 5, 5);
                                tr_head.addView(data);// add the column to the table row here

                                tl.addView(tr_head, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.MATCH_PARENT));


                            }
                        }
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting documents", task.getException());

                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                DocumentReference del = ff.collection("Sale").document(fa.getCurrentUser().getUid());
                del.delete();
                startActivity(new Intent(getApplicationContext(),ViewInvoice.class));
                return true;
        }

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
    }
}