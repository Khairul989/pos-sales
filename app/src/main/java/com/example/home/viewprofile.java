package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.userhelper;
import com.example.model.user;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class viewprofile extends AppCompatActivity {
TextView verified,notverified;
EditText fn,email,phone;
Button update, verify, back;
FirebaseAuth fa;
FirebaseFirestore ff;
FirebaseUser users;
private userhelper uh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);

        fn = findViewById(R.id.tvFn);
        email = findViewById(R.id.tvEmail);
        phone = findViewById(R.id.tvPhone);
        update = findViewById(R.id.btnUpdateProfile);
        verified = findViewById(R.id.verified);
        notverified = findViewById(R.id.notVerified);
        verify = findViewById(R.id.verify);
        back = findViewById(R.id.btnBack);
        fa = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        //Display verified user
        users = fa.getCurrentUser();

        if(!users.isEmailVerified())
        {
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Verification email send, Please check your email", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
        else
        {
            //Get data from firestore and display into textview
            notverified.setVisibility(View.GONE);
            verify.setVisibility(View.GONE);
            fn.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            verified.setVisibility(View.VISIBLE);
            DocumentReference dr = ff.collection("user").document(fa.getUid());
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    fn.setText(documentSnapshot.getString("fullname"));
                    email.setText(documentSnapshot.getString("email"));
                    phone.setText(documentSnapshot.getString("phone"));
                }
            });
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uh = new userhelper();
                DocumentReference dr = ff.collection("user").document(fa.getUid());
                user u = new user();
                 u.setFullname(fn.getText().toString());
                 u.setEmail(email.getText().toString());
                 u.setPhone(phone.getText().toString());


                boolean status = uh.update(u, dr, viewprofile.this);
                if (status) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

    }

    public void backHome(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}