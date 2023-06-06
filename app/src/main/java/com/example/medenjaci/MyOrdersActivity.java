package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.medenjaci.beans.Order;
import com.example.medenjaci.util.OrderAdapter;
import com.example.medenjaci.util.OrderHelper;
import com.example.medenjaci.util.SessionManager;
import com.example.medenjaci.util.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<OrderHelper> ordersHelper;

    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);


        //init recycler
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String username = userDetails.get(SessionManager.KEY_USERNAME);


        Query getMyOrdersQuery = FirebaseDatabase.getInstance().getReference("orders").orderByChild("username").equalTo(username);

        getMyOrdersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersHelper = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order o = dataSnapshot.getValue(Order.class);
                    o.setId_order(dataSnapshot.getKey());

                    OrderHelper orderHelper = new OrderHelper();
                    orderHelper.setPrice(o.getTotal_price());

                    if (o.getStatus().equals("I")) {
                        String date = o.getDate_of_shipping();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
                        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

                        //porudzbina je stigla, mora da se update-uje
                        if (LocalDateTime.now().isAfter(dateTime.plusDays(o.getNumber_of_days()))) {
                            o.setStatus("D");
                            HashMap<String, Object> updateMap = new HashMap<>();
                            updateMap.put("status", "D");
                            FirebaseDatabase.getInstance().getReference("orders").child(dataSnapshot.getKey()).updateChildren(updateMap);
                        }
                    }


                    switch (o.getStatus()) {
                        case "O":
                            orderHelper.setStatus("ODBIJENA");
                            orderHelper.setIcon(getResources().getIdentifier("ic_order_canceled", "drawable", getPackageName()));
                            break;
                        case "P":
                            orderHelper.setStatus("NA ČEKANJU");
                            orderHelper.setIcon(getResources().getIdentifier("ic_order_ordered", "drawable", getPackageName()));
                            break;
                        case "I":
                            orderHelper.setStatus("ISPORUCENA");
                            orderHelper.setIcon(getResources().getIdentifier("ic_order_sent", "drawable", getPackageName()));
                            break;
                        case "D":
                            orderHelper.setStatus("DOSTAVLJENA");
                            orderHelper.setIcon(getResources().getIdentifier("ic_order_arrived", "drawable", getPackageName()));
                            break;

                    }
                    orderHelper.setId_order(o.getId_order());
                    orderHelper.setTimeOrder(o.getDate_of_order());
                    ordersHelper.add(orderHelper);
                }

                recyclerView = (RecyclerView) findViewById(R.id.ordersRecyclerView);

                orderAdapter = new OrderAdapter(getApplicationContext(), ordersHelper);

                orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                    @Override
                    public void onButtonClick(int position) {
                        Intent intent = new Intent(getApplicationContext(), ItemOrderActivity.class);
                        intent.putExtra("id_order", ordersHelper.get(position).getId_order());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(orderAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_items:
                intent = new Intent(this, IndexConsumerActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.item_profile:
                intent = new Intent(this, ProfileActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.item_cart:
                intent = new Intent(this, CartActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.item_orders:

                return true;
            case R.id.item_logout:
                SessionManager sessionManagerLogout = new SessionManager(this);
                sessionManagerLogout.logout();

                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}