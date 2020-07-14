package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helper.userhelper;
import com.example.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class viewprofile extends AppCompatActivity {
TextView verified,notverified;
EditText fn,email,phone;
Button update, verify;
FirebaseAuth fa;
FirebaseFirestore ff;
FirebaseUser users;
ImageView profPic;
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
        fa = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        profPic = findViewById(R.id.userProfPic);
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
            update.setVisibility(View.VISIBLE);
            verified.setVisibility(View.VISIBLE);
            profPic.setVisibility(View.VISIBLE);
            DocumentReference dr = ff.collection("user").document(fa.getUid());
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    fn.setText(documentSnapshot.getString("fullname"));
                    email.setText(documentSnapshot.getString("email"));
                    phone.setText(documentSnapshot.getString("phone"));
                    Glide.with(viewprofile.this).load(documentSnapshot.getString("imageUri")).into(profPic);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
    public void deleteUser(View view) {
        final String id = fa.getCurrentUser().getUid();
        final AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        users = fa.getCurrentUser();

        b.setTitle("User Deletion");
        b.setMessage("You sure to delete this account?");
        b.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                users.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            DocumentReference dr = ff.collection("user").document(id);
                           final CollectionReference cr = ff.collection("Product");

                            dr.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            DocumentReference dp = ff.collection("Product").document();
                                            for(QueryDocumentSnapshot doc: task.getResult()){
                                                String uid = doc.getString("userID");
                                                if(id.equals(uid)){
                                                    dp.delete();
                                                }
                                            }
                                            Toast.makeText(viewprofile.this, "We are sad on your leaving ;(.. Do join us again in the future", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), login.class));
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
        b.setNegativeButton("Cancel", null);

        b.create().show();
    }
}