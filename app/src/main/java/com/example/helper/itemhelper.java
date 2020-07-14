package com.example.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.home.productListAdapter;
import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.home.signup.TAG;

public class itemhelper {
    static FirebaseFirestore ff;
    static FirebaseAuth fa;

    public itemhelper(){}

    public static void listitem (final String userid, final Activity c, final ListView it){
        ff = FirebaseFirestore.getInstance();
        final ArrayList<product> productArray = new ArrayList<>();
        ff.collection("Product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String userID = doc.getString("userID");
                        if (userid.equals(userID)) {
                            String pid = doc.getString("prodId");
                            String pd = doc.getString("prodDesc");
                            String pn = doc.getString("prodName");
                            String pp = doc.getString("prodPrice");
                            String pc = doc.getString("prodCategory");
                            String pq ="";
                            String pimg = doc.getString("imgUri");
                            product p = new product(userID, pid, pn, pd, pp, pc,pq, pimg);
                            productArray.add(p);
                        }
                    }
                    productListAdapter pla = new productListAdapter((Activity) c, productArray);
                    it.setAdapter(pla);
                    pla.notifyDataSetChanged();
                    if(productArray.size() == 0) {
                        Toast.makeText(c, "List Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting documents", task.getException());
                }
            }
        });
    }
}
