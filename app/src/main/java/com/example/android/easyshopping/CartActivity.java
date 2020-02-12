package com.example.android.easyshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.example.android.easyshopping.MainActivity.budget;
import static com.example.android.easyshopping.MainActivity.itemList;
import static com.example.android.easyshopping.MainActivity.left;
import static com.example.android.easyshopping.MainActivity.list;
import static com.example.android.easyshopping.MainActivity.mReview;
import static com.example.android.easyshopping.MainActivity.mtotal;

public class CartActivity extends AppCompatActivity {

    private Socket socket;

    private static final int SERVERPORT = 1234;
    private static final String SERVER_IP = "192.168.0.5";

    public static final String TAG = "CART_LIST";
    private int ActivityRequestCode=200;
    private Toolbar toolBar;
    private ListView listView;
    private TextView totalAmountView ;
    public static int TotalAmount = 0;
    private Button deleteButton,paymentButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Thread t = new Thread(new ClientThread());
        //t.start();
        Log.i("THREAD_CLIENT","THREAD STARTED");


        toolBar  = findViewById(R.id.main_toolbar);
        toolBar.setTitle("Cart");
        toolBar.setBackgroundColor(Color.parseColor("#263238"));

        paymentButton = findViewById(R.id.paymentButton);
        totalAmountView = findViewById(R.id.cartTotal);
        listView = findViewById(R.id.itemesList);
        final ItemAdapter itemAdapter = new ItemAdapter(CartActivity.this,list);
        listView.setAdapter(itemAdapter);
        TotalAmount  = getTotalAmount(list);
        totalAmountView.setText("₹ "+TotalAmount);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final int p = position;
                final Item item = list.get(p);
                AlertDialog.Builder mBuillder = new AlertDialog.Builder(CartActivity.this);
                final AlertDialog alertDialog = mBuillder.create();
                View mView = LayoutInflater.from(CartActivity.this).inflate(R.layout.singlelistitem,null);
                alertDialog.setView(mView);
                alertDialog.setTitle("Do you wish to Delete ?");
                deleteButton = mView.findViewById(R.id.deletebutton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(list!=null)
                        {
                            list.remove(p);
                            itemList.remove(p);
                            int pro = Integer.parseInt(list.get(p).getPrice());
                            left = left + pro;
                            mReview.setText("Amount Left -"+(left));
                            Log.i(TAG,""+list.size()+" - "+p);
                            itemAdapter.notifyDataSetChanged();
                            Toast.makeText(CartActivity.this,"Item "+position+" is removed",Toast.LENGTH_SHORT).show();
                            mtotal -= Integer.parseInt(list.get(position).getPrice());
                            alertDialog.dismiss();
                            finish();
                            Intent i = new Intent(CartActivity.this,CartActivity.class);
                            startActivity(i);
                        }
                    }
                });
                alertDialog.show();
            }
        });
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f = getBillString(itemList);
                MessageSender messageSender = new MessageSender();
                messageSender.execute(f);
                String url = "http://m.p-y.tm";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
    private int getTotalAmount(ArrayList<Item> list){
        int totalAmount=0;
        for(int i =0 ; i < list.size();i++)
        {
            totalAmount += Integer.parseInt(list.get(i).getPrice());
        }
        return totalAmount;
    }

    private String getBillString(List<String> list2)
    {
        String s="";
        Set<String> hashsetList = new HashSet<String>(list2);
        Iterator<String> itr = hashsetList.iterator();
        int i=0;
        while (itr.hasNext()) {
            s = s + itr.next() + ";";
        }
        return s;
    }

}
