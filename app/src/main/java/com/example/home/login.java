package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText email, pass;
    Button btnLogin;
    ProgressBar pb;
    FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login Form");

        email = findViewById(R.id.email2);
        pass = findViewById(R.id.passwords2);
        btnLogin = findViewById(R.id.loginBtn);
        pb = findViewById(R.id.progressBar2);
        fa = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(mail)){
                    email.setError("Email is required!!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is required!!");
                    return;
                }
                pb.setVisibility(View.VISIBLE);

                // check user

                fa.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(login.this, "Error ! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
            }

        });
    }

    public void btn_signup(View view) {
        startActivity(new Intent(getApplicationContext(),signup.class));
    }
    
}