package com.example.helper;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.home.MainActivity;
import com.example.home.signup;
import com.example.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.home.signup.TAG;

public class userhelper {

    private FirebaseUser users;
    private FirebaseFirestore ff;
    private FirebaseAuth fa;
    private static signup s = new signup();
    private static MainActivity m = new MainActivity();
    public userhelper(){
        ff = FirebaseFirestore.getInstance();
        fa = FirebaseAuth.getInstance();
    }

    public boolean AddUser(final user u, final Activity c, String pass){
        fa = FirebaseAuth.getInstance();
        fa.createUserWithEmailAndPassword(u.getEmail(),pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //Send email verification
                    FirebaseUser fuser = fa.getCurrentUser();
                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(c, "Verification email send, Please check your email", Toast.LENGTH_SHORT).show();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(c, "Error! "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    //Create user data and store in firebase
                    String uid = fa.getCurrentUser().getUid();
                    DocumentReference dr = ff.collection("user").document(uid);
                    u.setId(uid);
                    dr.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User created : " + u.getId());
                        }
                    });
                    Toast.makeText(c, "Account Successfully Registered", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(c, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return true;
    }

    public boolean update(user u, DocumentReference dr, final Activity c){
        users = fa.getCurrentUser();
        users.updateEmail(u.getEmail());
        Map<String, Object> User = new HashMap<>();
        User.put("fullname", u.getFullname());
        User.put("email", u.getEmail());
        User.put("phone", u.getPhone());
        dr.update(User).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c, "Account Updated", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });
        return true;
    }

}
