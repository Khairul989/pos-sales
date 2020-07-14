package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Iterator;

import javax.annotation.Nullable;

public class resetProfile extends AppCompatActivity {

    EditText email;
    TextView fn,emailtv,phone;
    FirebaseFirestore ff;
    TextInputLayout kotak2;
    FirebaseAuth fa;
    String mail,fullname,emails,phoneNo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_profile2);
        email = findViewById(R.id.resetEmail);
        fn = findViewById(R.id.resetFn);
        emailtv = findViewById(R.id.resEmailtv);
        phone = findViewById(R.id.resPhone);
        ff = FirebaseFirestore.getInstance();
        kotak2 = findViewById(R.id.kotak2);
        fa = FirebaseAuth.getInstance();
    }

    public void btnCheck(View view) {
        ff.collection("user")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                boolean found = false;
                for(DocumentSnapshot doc: task.getResult()){
                     mail = doc.getString("email");

                    if (mail.equals(email.getText().toString())){
                        fullname = doc.getString("fullname");
                        emails = doc.getString("email");
                        phoneNo = doc.getString("phone");
                        found = true;
                    }
                }
                if (found ==  true){
                    fn.setText(fullname);
                    emailtv.setText(emails);
                    phone.setText(phoneNo);

                    kotak2.setVisibility(View.VISIBLE);
                }
                else
                {
                    kotak2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "User did not exist. Please enter valid email!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnProceed(View view) {
        final String resetmail = email.getText().toString();
        AlertDialog.Builder a = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        a.setTitle("Reset Password? ");
        a.setMessage("Confirm for password reset?");

        a.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fa.sendPasswordResetEmail(resetmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"A reset link has been sent to your email.",Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Error! Reset link cannot be sent: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        a.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Password reset canceled",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        a.create().show();
    }
}