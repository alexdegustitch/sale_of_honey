package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medenjaci.beans.Item;
import com.example.medenjaci.beans.Order;
import com.example.medenjaci.beans.OrderItem;
import com.example.medenjaci.util.ItemInOrder;
import com.example.medenjaci.util.ItemOrderAdapter;
import com.example.medenjaci.util.SessionManager;
import com.example.medenjaci.util.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ItemOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textUkupno;
    ItemOrderAdapter itemOrderAdapter;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order);

        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);


        DatabaseReference referenceItemsInOrder = FirebaseDatabase.getInstance().getReference("items_in_order");
        //get items in order
        String id_order = getIntent().getStringExtra("id_order");


        Query findItemsInOrder = referenceItemsInOrder.orderByChild("id_order").equalTo(id_order);

        findItemsInOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ItemInOrder> itemsInOrder = new ArrayList<>();

                double sum = 0;
                Log.i("moje", String.valueOf(snapshot.getChildrenCount()));
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    ItemInOrder itemInOrder = new ItemInOrder();
                    OrderItem orderItem = dataSnapshot.getValue(OrderItem.class);

                    itemInOrder.setName(orderItem.getName());
                    itemInOrder.setPrice(Integer.toString(orderItem.getQuantity()) + " x " + Double.toString(orderItem.getItem_price()) + " = " + Double.toString(orderItem.getTotal_price()) + " RSD");
                    itemInOrder.setIcon(getResources().getIdentifier("item_" + Integer.toString(orderItem.getId_item()), "drawable", getPackageName()));

                    sum += orderItem.getTotal_price();
                    itemsInOrder.add(itemInOrder);
                }


                recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrder);

                itemOrderAdapter = new ItemOrderAdapter(getApplicationContext(), itemsInOrder);

                recyclerView.setAdapter(itemOrderAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                textUkupno = (TextView) findViewById(R.id.orderUkupno);
                textUkupno.setText(Double.toString(sum) + " RSD");


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

                SessionManager sessionManager = new SessionManager(this);
                HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

                String username = userDetails.get(SessionManager.KEY_USERNAME);

                Query findOrdersQuery = FirebaseDatabase.getInstance().getReference("orders").orderByChild("username").equalTo(username);
                findOrdersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Intent intent_ = new Intent(getApplicationContext(), MyOrdersActivity.class);
                            intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent_);
                        } else {
                            Intent intent_ = new Intent(getApplicationContext(), NoOrdersActivity.class);
                            intent_.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(intent_);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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