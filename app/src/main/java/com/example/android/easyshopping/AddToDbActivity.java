package com.example.android.easyshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddToDbActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Toolbar toolbar;
    private Button button;
    private EditText nName , nBrand , nexp , nImg , nProductId,nPrice , nRating , nReview ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_db);

        database = FirebaseDatabase.getInstance();

        toolbar = findViewById(R.id.entryToolbar);

        toolbar.setTitle("Cart");
        toolbar.setBackgroundColor(Color.parseColor("#263238"));

        button = findViewById(R.id.button);
        nName = findViewById(R.id.cartName);
        nBrand = findViewById(R.id.cartbrand);
        nexp = findViewById(R.id.cartExpirydate);
        nPrice = findViewById(R.id.cartPrice);
        nRating = findViewById(R.id.cartRating);
        nImg = findViewById(R.id.cartImage);
        nReview = findViewById(R.id.cartReview);
        nProductId = findViewById(R.id.cartproductid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pid = nProductId.getText().toString().trim();
                reference = database.getReference().child("groceryitems").child(pid);
                HashMap<String , String> map = new HashMap<>();
                map.put("name",nName.getText().toString().trim());
                map.put("brand",nBrand.getText().toString().trim());
                map.put("image",nImg.getText().toString().trim());
                map.put("expiry date",nexp.getText().toString().trim());
                map.put("price",nPrice.getText().toString().trim());
                map.put("rating",nRating.getText().toString().trim());
                map.put("review",nReview.getText().toString().trim());

                reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddToDbActivity.this,"Sucessfully Added!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
