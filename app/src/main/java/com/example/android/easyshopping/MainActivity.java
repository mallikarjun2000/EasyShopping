package com.example.android.easyshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.L;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.android.easyshopping.CartActivity.TotalAmount;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    String t;
    String name,rating,price,expiryDate,brand,image,review,formattedDate,offer,sys[],pro[];
    private static EditText getbudget;
    public static int budget=500,mtotal=0;
    public static int left=0;
    public static ArrayList<Item> list = new ArrayList<Item>();
    public static List<String> itemList = new ArrayList<String>();
    private TextView mName, mBrand, mExpiry, mRating, mPrice;
    public static TextView mReview;
    private Toolbar toolBar;
    private ImageView mImage,mAddtoDb,mCart;
    private Button mButton,mAddtoCart,budgetSave;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference().child("groceryitems");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");

        //Date Format
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c);
        sys = formattedDate.split("/");
        Log.i("DATE_SYS",sys[2]);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = mBuilder.create();
        View mView = LayoutInflater.from(this).inflate(R.layout.budgetlayout,null);
        budgetSave = mView.findViewById(R.id.budgetsaveButton);
        getbudget = mView.findViewById(R.id.budgeteditText);
        alertDialog.setView(mView);
        alertDialog.setTitle("Enter the Budget");
        alertDialog.show();
        budgetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budget = Integer.parseInt(String.valueOf(getbudget.getText()));
                Toast.makeText(MainActivity.this,"Buget recieved",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        setContentView(R.layout.activity_main);
        mAddtoCart = findViewById(R.id.itemAddToCart);
        mAddtoDb = findViewById(R.id.main_add_item);
        mCart = findViewById(R.id.show_cart);
        mName = findViewById(R.id.itemName);
        mBrand = findViewById(R.id.itemBrand);
        mExpiry = findViewById(R.id.itemExpiry);
        mRating = findViewById(R.id.itemRating);
        mPrice = findViewById(R.id.itemPrice);
        mImage = findViewById(R.id.itemImage);
        mButton = findViewById(R.id.scanButton);
        mReview = findViewById(R.id.itemRecommendtion);
        toolBar  = findViewById(R.id.main_toolbar);
        toolBar.setTitle("Easy Shop");
        toolBar.setBackgroundColor(Color.parseColor("#263238"));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                scanBarCode(v);
            }
        });

        mAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtotal = mtotal + Integer.parseInt(price);
                if(mtotal <= budget) {
                    /*if(Integer.parseInt(sys[2]) < Integer.parseInt(pro[2])) {
                        Item item = new Item(name, brand, expiryDate, image, price, rating, review);
                        list.add(item);
                        itemList.add(name);
                        Toast.makeText(MainActivity.this, mName.getText().toString().trim() + " Added ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(Integer.parseInt(sys[2]) == Integer.parseInt(pro[2]))
                        {
                            if(Integer.parseInt(sys[1]) < Integer.parseInt(pro[1]))
                            {
                                Item item = new Item(name, brand, expiryDate, image, price, rating, review);
                                list.add(item);
                                itemList.add(name);
                                Toast.makeText(MainActivity.this, mName.getText().toString().trim() + " Added ", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(MainActivity.this,"Item Expired !",Toast.LENGTH_SHORT).show();
                        }
                        if(Integer.parseInt(sys[2]) < Integer.parseInt(pro[2]))
                        {
                            Toast.makeText(MainActivity.this,"Item Expired !",Toast.LENGTH_SHORT).show();
                        }
                    }*/
                    Item item = new Item(name, brand, expiryDate, image, price, rating, review);
                    list.add(item);
                    itemList.add(t);
                    Toast.makeText(MainActivity.this, mName.getText().toString().trim() + " Added ", Toast.LENGTH_SHORT).show();
                    left = budget-mtotal;
                    mReview.setText("Amount Left - "+(left));
                }
                else
                {
                    mtotal = mtotal - Integer.parseInt(price);
                    Toast.makeText(MainActivity.this,"Budget is exceeded",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CartActivity.class);
                startActivity(i);
            }
        });

        mAddtoDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddToDbActivity.class);
                startActivity(i);
            }
        });
    }
    public void scanBarCode(View v) {
        Intent i = new Intent(MainActivity.this, CamScanner.class);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,null);
        if(requestCode == 0)
        {
            if(resultCode == CommonStatusCodes.SUCCESS)
            {
                if(data!=null)
                {
                    Barcode barcode =data.getParcelableExtra("barcode");
                    t= barcode.rawValue;
                    retriveData(t);
                    Toast.makeText(MainActivity.this,t,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"No Data Accuired",Toast.LENGTH_SHORT);
                }
            }
        }
    }
    public void retriveData(final String data)
    {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 name = (String) dataSnapshot.child(""+data).child("name").getValue();
                 brand = (String) dataSnapshot.child(""+data).child("brand").getValue();
                image = (String) dataSnapshot.child(""+data).child("image").getValue();
                expiryDate = (String) dataSnapshot.child(""+data).child("expiry date").getValue();
                price = (String) dataSnapshot.child(""+data).child("price").getValue();
                 rating = (String) dataSnapshot.child(""+data).child("rating").getValue();
                 offer = (String) dataSnapshot.child(""+data).child("offer").getValue();
                mName.setText(name);
                mBrand.setText("Brand "+brand);
                mExpiry.setText("Expiry Date"+ expiryDate);
                mPrice.setText("₹ " + price);
                mRating.setText("rating :"+rating+"/5");
                progressDialog.dismiss();
                if(!offer.equals("null"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setTitle("Offer");
                    alertDialog.setMessage(offer);
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(true);
                }
                Picasso.get().load(image).into(mImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,"data doesn't exi. ",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
