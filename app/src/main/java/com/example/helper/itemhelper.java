package com.example.helper;

import android.util.Log;

import androidx.annotation.NonNull;

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

    public static ArrayList<product> listitem (final String userid){
        final ArrayList<product> pro = new ArrayList<product>();
        System.out.println(userid);
        ff = FirebaseFirestore.getInstance();
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
                            String pq = doc.getString("prodQuantity");
                            String pc = doc.getString("prodCategory");
                            product p = new product(userID, pid, pn, pd, pp, pq, pc);
                            pro.add(p);
                        }
                    }
                }
                else
                {
                    Log.w(TAG, "Error getting documents", task.getException());
                }
            }
        });
        return pro;
    }
}
