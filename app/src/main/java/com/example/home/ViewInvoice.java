package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class ViewInvoice extends AppCompatActivity {
    private ArrayList<sale> invArray;
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    final Context c = this;
    ListView it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
        final String uid = fa.getCurrentUser().getUid();
        it = findViewById(R.id.invlist);
        invArray = new ArrayList<>();

        ff.collection("Sale").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("uid");
                        if (uid.equals(userID)) {
                            Toast.makeText(c, "Found", Toast.LENGTH_SHORT).show();
                            sale s = new sale(uid,doc.getString("name"),doc.getString("id"),doc.getString("email"),
                                    doc.getString("phone"),doc.getString("total"),doc.getString("date"),doc.getString("time"));
                            invArray.add(s);
                        }
                        else{
                        }
                    }
                    invoiceListAdapter pla = new invoiceListAdapter(ViewInvoice.this, invArray);
                    it.setAdapter(pla);
                    pla.notifyDataSetChanged();
                    it.setOnItemClickListener(listClick);
                    if(invArray.size() == 0) {
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
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }

    private AdapterView.OnItemClickListener listClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sale s = (sale) it.getItemAtPosition(position);
            Toast.makeText(c, "You have selected "+s.getId(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), viewInvoiceDetail.class);
            intent.putExtra("id",s.getId());
            startActivity(intent);
        }
    };
}