package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.model.product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static com.example.home.signup.TAG;

public class AddProduct extends AppCompatActivity{
    EditText prodName, prodDesc, PPUnit;
    Spinner ProdCategory;
    Button btnAdd;
    FirebaseFirestore ff;
    ProgressBar pb;
    ImageView prodPic;
    private Uri croppedImage;
    private StorageReference mStorageRef;
    private StorageTask uploadTask;
    private final int galCode = 999;
    product p = new product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product2);
        prodName = findViewById(R.id.prodName);
        prodDesc = findViewById(R.id.prodDesc);
        PPUnit = findViewById(R.id.PPUnit);
        ProdCategory = findViewById(R.id.prodCategory);
        btnAdd = findViewById(R.id.btnAdd);
        prodPic = findViewById(R.id.prodPic);
        pb = findViewById(R.id.progressBar3);
        ff = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prodPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AddProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, galCode);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pn = prodName.getText().toString();
                String pdesc = prodDesc.getText().toString();
                String PUnit = PPUnit.getText().toString();
                String pCateg = ProdCategory.getSelectedItem().toString();

                if(TextUtils.isEmpty(pn) || TextUtils.isEmpty(pdesc) ||TextUtils.isEmpty(PUnit) || TextUtils.isEmpty(pCateg)){
                    Toast.makeText(AddProduct.this, "Please make sure all the field is filled in!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseAuth fa = FirebaseAuth.getInstance();
                    String uID = fa.getUid();
                    String pID = ff.collection("Product").document().getId();

                    final DocumentReference dr = ff.collection("Product").document(pID);
                    mStorageRef = FirebaseStorage.getInstance().getReference("prodImage");
                    String path = croppedImage.getPath();
                    String extension = path.substring(path.lastIndexOf(".")+1);
                    String imgId = System.currentTimeMillis()+"."+extension;
                    StorageReference sRef = mStorageRef.child(imgId);

                    p.setProdId(pID);
                    p.setUserID(uID);
                    p.setProdName(pn);
                    p.setProdDesc(pdesc);
                    p.setProdPrice(PUnit);
                    p.setProdCategory(pCateg);

                    pb.setVisibility(View.VISIBLE);

                    uploadTask = sRef.putFile(croppedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    p.setImgUri(url);
                                    dr.set(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(AddProduct.this,"Prodcut Successfully Added",Toast.LENGTH_SHORT).show();
                                                addSuccess();
                                                pb.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                Toast.makeText(AddProduct.this,"Failed! "+task.getException(),Toast.LENGTH_SHORT).show();
                                                pb.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem mi){
        if(mi.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(mi);
    }
    public void addSuccess(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        b.setTitle("Successful");
        b.setMessage("Your Product has been Added..");

        b.setPositiveButton("Add another product?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddProduct.this,"Please add another product",Toast.LENGTH_SHORT).show();
                prodName.setText("");
                prodDesc.setText("");
                PPUnit.setText("");
            }
        });
        b.setNegativeButton("Back to Homepage", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        AlertDialog a = b.create();
        a.show();
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
                prodPic.setImageURI(resultUri);
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
}