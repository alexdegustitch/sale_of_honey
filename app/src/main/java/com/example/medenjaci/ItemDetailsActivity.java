package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medenjaci.beans.Cart;
import com.example.medenjaci.beans.Item;
import com.example.medenjaci.util.SessionManager;
import com.example.medenjaci.util.Singleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.medenjaci.util.Singleton.*;

public class ItemDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Item item;
    TextView textName, textDesc, textDescUse, textPrice;
    ImageView image;
    Spinner spinner;
    int quantity_number = 1;

    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);

        textName = (TextView) findViewById(R.id.itemDetailsName);
        textDesc = (TextView) findViewById(R.id.itemDetailsDesc);
        textDescUse = (TextView) findViewById(R.id.itemDetailsDescUse);
        image = (ImageView) findViewById(R.id.itemDetailsImage);
        spinner = (Spinner) findViewById(R.id.itemDetailsSpinner);
        textPrice = (TextView)findViewById(R.id.itemDetailsPrice);

        reference = FirebaseDatabase.getInstance().getReference("items");

        getData();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, R.layout.custom_spinner);

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }


    private void getData() {
        if (getIntent().hasExtra("item_id")) {

            int id = getIntent().getIntExtra("item_id", 1);

            Query findItem = reference.orderByChild("id_item").equalTo(id);
            findItem.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        item = snapshot.child(String.valueOf(id)).getValue(Item.class);
                        setData();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void setData() {
        if (item != null) {
            textName.setText(item.getName());
            textDesc.setText(item.getDesc());
            textDescUse.setText(item.getUse_desc());
            textPrice.setText("Cena : " + item.getPrice() + " RSD");
            image.setImageResource(getResources().getIdentifier("item_" + item.getId_item(), "drawable", getPackageName()));
        }
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        quantity_number = Integer.parseInt(parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void put_item_in_cart(View view) {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String user = userDetails.get(SessionManager.KEY_USERNAME);

        DatabaseReference refCart = FirebaseDatabase.getInstance().getReference("carts");

        Cart cart = new Cart();
        cart.setQuantity(quantity_number);
        cart.setPrice(item.getPrice() * quantity_number);
        cart.setId_item(item.getId_item());
        cart.setUser(user);
        cart.setItem_price(item.getPrice());
        cart.setItem_name(item.getName());

        Query findCart = refCart.orderByChild("user").equalTo(user);

        findCart.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart c = dataSnapshot.getValue(Cart.class);
                    if (c.getId_item() == cart.getId_item()) {
                        HashMap<String, Object> updateMap = new HashMap<>();
                        updateMap.put("quantity", c.getQuantity() + cart.getQuantity());
                        updateMap.put("price", c.getPrice() + cart.getPrice());
                        refCart.child(dataSnapshot.getKey()).updateChildren(updateMap);
                        if (quantity_number > 1) {
                            Toast.makeText(getApplicationContext(), "Dodali ste " + quantity_number + " komada proizvoda \"" + item.getName() + "\"", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Dodali ste " + quantity_number + " komad proizvoda \"" + item.getName() + "\"", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                }

                refCart.push().setValue(cart);
                if (quantity_number > 1) {
                    Toast.makeText(getApplicationContext(), "Dodali ste " + quantity_number + " komada proizvoda \"" + item.getName() + "\"", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Dodali ste " + quantity_number + " komad proizvoda \"" + item.getName() + "\"", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}