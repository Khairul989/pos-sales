package com.example.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    TextView title;
    FirebaseAuth fa;
    FirebaseFirestore ff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView ProdCard = findViewById(R.id.cardProd);
        CardView setting = findViewById(R.id.setting);

        ProdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Products.class));
            }
        });
        title = findViewById(R.id.welcUser);
        fa = FirebaseAuth.getInstance();
        ff = FirebaseFirestore.getInstance();

        //Get fullname
        DocumentReference dr = ff.collection("user").document(fa.getCurrentUser().getUid());

        DocumentReference dd = ff.collection("Product").document();
        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                title.setText("Welcome "+documentSnapshot.getString("fullname"));
            }
        });

        //View profile
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),viewprofile.class));
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.product_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = super.getMenuInflater();
        i.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            logout();
            return true;
        }
        return false;
    }
    public void logout(){
        AlertDialog.Builder b = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        b.setTitle("Alert!");
        b.setMessage("Are you sure to Logout?");

        b.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fa.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),login.class));
                Toast.makeText(getApplicationContext(), "Logout Succesful", Toast.LENGTH_SHORT).show();
            }
        });
        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Logout Canceled", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog a = b.create();
        a.show();
    }

}
