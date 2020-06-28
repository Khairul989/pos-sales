package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.userhelper;
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

public class signup extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText fn, email, phone, pass, confPass;
    RadioGroup gender;
    RadioButton rb;
    Button regButton;
    FirebaseAuth fa;
    ProgressBar pb;
    TextView backLogin;
    FirebaseFirestore ff;
    userhelper uh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Signup Form");

        fn = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.passwords);
        confPass = findViewById(R.id.confirmPass);
        gender = findViewById(R.id.gender);
        regButton = findViewById(R.id.register);
        pb = findViewById(R.id.progressBar);
        fa = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();
        backLogin = findViewById(R.id.backLogin);
        uh = new userhelper();
        /*if (fa.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }*/

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mail = email.getText().toString().trim();
                final String password = pass.getText().toString().trim();
                final String fname = fn.getText().toString();
                final String phoneNo = phone.getText().toString();
                String cpass = confPass.getText().toString();
                int radioId = gender.getCheckedRadioButtonId();
                rb = findViewById(radioId);

                if (TextUtils.isEmpty(mail)){
                    email.setError("Email is required!!");
                    return;
                }
                if(TextUtils.isEmpty(fname)){
                    fn.setError("Fullname field is required!!");
                    return;
                }
                if(TextUtils.isEmpty(phoneNo)){
                    phone.setError("Phone Number field is required!!");
                    return;
                }
                if(TextUtils.isEmpty(cpass)){
                    confPass.setError("Confirm Password field is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is required!!");
                    return;
                }
                if(password.length() < 6 || password.length() > 8){
                    pass.setError("Password must be 6 to 8 character");
                    return;
                }
                if(phoneNo.length() <10 || phoneNo.length() > 12){
                    phone.setError("Phone Number must be 10 to 12 digits");
                }
                pb.setVisibility(View.VISIBLE);
                //User Registration to firebase
                if(cpass.equals(password) && TextUtils.isDigitsOnly(phoneNo) && phoneNo.length() >=10 && phoneNo.length() <= 12 && password.length() >= 6 && password.length() <= 8){
                    user u = new user();
                    u.setFullname(fname);
                    u.setPhone(phoneNo);
                    u.setEmail(mail);
                    u.setGender(rb.getText().toString());

                    boolean status = uh.AddUser(u, signup.this, password);

                    if(status)
                    {
                        pb.setVisibility(View.GONE);
                        startActivity(new Intent(getApplicationContext(), login.class));
                    }
                }
                else
                {
                    Toast.makeText(signup.this,"Please make sure Password and Confirm Password is the same value", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }
        });

    }

    public void btn_login(View view) {
        startActivity(new Intent(getApplicationContext(),login.class));
    }
}