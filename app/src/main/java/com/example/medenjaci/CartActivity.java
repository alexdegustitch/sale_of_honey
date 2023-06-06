package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medenjaci.beans.Cart;
import com.example.medenjaci.beans.Item;
import com.example.medenjaci.beans.Order;
import com.example.medenjaci.beans.OrderItem;
import com.example.medenjaci.util.CartItem;
import com.example.medenjaci.util.ItemCartAdapter;
import com.example.medenjaci.util.SessionManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.example.medenjaci.util.Singleton.*;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView textViewSum;
    ItemCartAdapter itemCartAdapter;

    ArrayList<CartItem> itemsInCart;
    List<Cart> carts;

    DatabaseReference reference;
    Button orderOrder;

    ConstraintLayout constraintLayoutEmptyCart, constraintLayoutSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);

        orderOrder = (Button) findViewById(R.id.cartNaruci);
        constraintLayoutEmptyCart = (ConstraintLayout) findViewById(R.id.emptyCart);
        constraintLayoutSum = (ConstraintLayout) findViewById(R.id.sumCart);
        //inicijalizacija recyclerview-a
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();


        String username = userDetails.get(SessionManager.KEY_USERNAME);

        reference = FirebaseDatabase.getInstance().getReference("carts");

        carts = new LinkedList<>();

        Query findCarts = reference.orderByChild("user").equalTo(username);

        findCarts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                carts = new LinkedList<>();
                itemsInCart = new ArrayList<>();

                double sum = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart cart = dataSnapshot.getValue(Cart.class);
                    cart.setId_cart(dataSnapshot.getKey());
                    carts.add(cart);

                    sum += cart.getPrice();

                    String part1 = Integer.toString(cart.getQuantity()) + " x " + Double.toString(cart.getItem_price()) + " RSD";
                    String part2 = "= " + Double.toString(cart.getPrice()) + " RSD";

                    CartItem cartItem = new CartItem(getResources().getIdentifier("item_" + cart.getId_item(), "drawable", getPackageName()), cart.getItem_name(), part1, part2);
                    itemsInCart.add(cartItem);

                }


                textViewSum = (TextView) findViewById(R.id.orderUkupno);
                textViewSum.setText(Double.toString(sum) + " RSD");

                //recycler view
                recyclerView = (RecyclerView) findViewById(R.id.recyclerViewOrder);

                itemCartAdapter = new ItemCartAdapter(getApplicationContext(), itemsInCart);

                itemCartAdapter.setOnItemClickListener(new ItemCartAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        removeItem(position);
                    }
                });
                recyclerView.setAdapter(itemCartAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                if (itemsInCart.size() == 0) {
                    constraintLayoutEmptyCart.setVisibility(ConstraintLayout.VISIBLE);
                    recyclerView.setVisibility(RecyclerView.INVISIBLE);
                    constraintLayoutSum.setVisibility(ConstraintLayout.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LocalDateTime localDateTime = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");
                String formattedDateTime = localDateTime.format(formatter);

                Order order = new Order();
                order.setDate_of_order(formattedDateTime);
                order.setNumber_of_days(0);
                order.setStatus("P");
                order.setUsername(username);
                order.setTotal_price(Double.parseDouble(textViewSum.getText().toString().split(" ")[0]));

                String key = FirebaseDatabase.getInstance().getReference("orders").push().getKey();
                FirebaseDatabase.getInstance().getReference("orders").child(key).setValue(order);

                for (Cart c : carts) {

                    OrderItem orderItem = new OrderItem();
                    orderItem.setId_item(c.getId_item());
                    orderItem.setId_order(key);
                    orderItem.setItem_price(c.getItem_price());
                    orderItem.setName(c.getItem_name());
                    orderItem.setQuantity(c.getQuantity());
                    orderItem.setTotal_price(c.getPrice());

                    FirebaseDatabase.getInstance().getReference("items_in_order").push().setValue(orderItem);
                    FirebaseDatabase.getInstance().getReference("carts").child(c.getId_cart()).removeValue();
                }

                constraintLayoutEmptyCart.setVisibility(ConstraintLayout.VISIBLE);
                recyclerView.setVisibility(RecyclerView.INVISIBLE);
                constraintLayoutSum.setVisibility(ConstraintLayout.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Poružbina je poslata!", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void removeItem(int position) {
        CartItem item = itemsInCart.remove(position);
        itemCartAdapter.notifyItemRemoved(position);

        double sum = Double.parseDouble(textViewSum.getText().toString().split(" ")[0]);
        sum -= carts.get(position).getPrice();
        Cart cart = carts.remove(position);
        textViewSum.setText(Double.toString(sum) + " RSD");

        reference.child(cart.getId_cart()).removeValue();

        if (itemsInCart.size() == 0) {
            constraintLayoutEmptyCart.setVisibility(ConstraintLayout.VISIBLE);
            recyclerView.setVisibility(RecyclerView.INVISIBLE);
            constraintLayoutSum.setVisibility(ConstraintLayout.INVISIBLE);
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