package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helper.userhelper;
import com.example.model.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

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
    ImageView profPic;
    private Uri croppedImage;
    private final int galCode = 999;

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
        profPic = findViewById(R.id.Addimg);
        uh = new userhelper();
        /*if (fa.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }*/

        profPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, galCode);
            }
        });

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

                    boolean status = uh.AddUser(u, signup.this, password, croppedImage);

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

    public void onRequestPermissionsResult(int requestCode, String[]permissions, int[]grantResults){
        if(requestCode == galCode){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent ig = new Intent(Intent.ACTION_GET_CONTENT);
                ig.setType("image/*");
                startActivityForResult(ig,galCode);
            }
            else
            {
                Toast.makeText(this, "Don't have permission to access gallery", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == galCode && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                profPic.setImageURI(resultUri);
                croppedImage = resultUri;
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void btn_login(View view) {
        startActivity(new Intent(getApplicationContext(),login.class));
    }


}