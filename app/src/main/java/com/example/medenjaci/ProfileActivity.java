package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.example.medenjaci.util.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView textUsername, textName, textLastName, textPhone, textAddress;
    EditText editName, editLastName, editPhone, editAddress;

    Button change, changePassword, save;

    AwesomeValidation awesomeValidation;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);

        sessionManager = new SessionManager(this);
        textUsername = (TextView) findViewById(R.id.profileUsernameText);
        textName = (TextView) findViewById(R.id.profileNameText);
        textLastName = (TextView) findViewById(R.id.profileLastNameText);
        textPhone = (TextView) findViewById(R.id.profilePhoneText);
        textAddress = (TextView) findViewById(R.id.profileAddressText);

        editName = (EditText) findViewById(R.id.profileNameEdit);
        editLastName = (EditText) findViewById(R.id.profileLastNameEdit);
        editPhone = (EditText) findViewById(R.id.profilePhoneEdit);
        editAddress = (EditText) findViewById(R.id.profileAddressEdit);

        change = (Button) findViewById(R.id.profileChange);
        changePassword = (Button) findViewById(R.id.profileChangePass);
        save = (Button) findViewById(R.id.profileSave);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.profileNameEdit, "[A-Za-z]+( ([A-Z]|[a-z])+)*", R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.profileLastNameEdit, "[A-Za-z]+( ([A-Z]|[a-z])+)*", R.string.invalid_lastname);
        awesomeValidation.addValidation(this, R.id.profileAddressEdit, new SimpleCustomValidation() {
            @Override
            public boolean compare(String s) {

                String regex = "(([A-Z]|[a-z])+ )+(bb|\\d+[a-z]|\\d+), ([A-Z]|[a-z])+( ([A-Z]|[a-z])+)*";
                boolean b = s.matches(regex);
                Log.i("compareInfo", String.valueOf(b));
                return s.matches(regex);
            }
        }, R.string.invalid_address);
        awesomeValidation.addValidation(this, R.id.profilePhoneEdit, "\\d{9,10}", R.string.invalid_phone);

        updateFields();
        setListeners();
    }

    private void setListeners() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textName.setVisibility(View.INVISIBLE);
                textLastName.setVisibility(View.INVISIBLE);
                textPhone.setVisibility(View.INVISIBLE);
                textAddress.setVisibility(View.INVISIBLE);

                editName.setVisibility(View.VISIBLE);
                editLastName.setVisibility(View.VISIBLE);
                editPhone.setVisibility(View.VISIBLE);
                editAddress.setVisibility(View.VISIBLE);


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(SessionManager.KEY_FIRSTNAME, editName.getText().toString());
                    hashMap.put(SessionManager.KEY_LASTNAME, editLastName.getText().toString());
                    hashMap.put(SessionManager.KEY_ADDRESS, editAddress.getText().toString());
                    hashMap.put(SessionManager.KEY_PHONE, editPhone.getText().toString());

                    sessionManager.change_info(hashMap);

                    HashMap<String, Object> hashUpdate = new HashMap<>();
                    hashUpdate.put("first_name", editName.getText().toString());
                    hashUpdate.put("last_name", editLastName.getText().toString());
                    hashUpdate.put("address", editAddress.getText().toString());
                    hashUpdate.put("phone_number", editPhone.getText().toString());

                    FirebaseDatabase.getInstance().getReference("users").child(textUsername.getText().toString()).updateChildren(hashUpdate);


                    textName.setVisibility(View.VISIBLE);
                    textLastName.setVisibility(View.VISIBLE);
                    textPhone.setVisibility(View.VISIBLE);
                    textAddress.setVisibility(View.VISIBLE);

                    editName.setVisibility(View.INVISIBLE);
                    editLastName.setVisibility(View.INVISIBLE);
                    editPhone.setVisibility(View.INVISIBLE);
                    editAddress.setVisibility(View.INVISIBLE);

                    updateFields();
                }


            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "HEJ", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), ChangePasswordActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void updateFields() {

        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String username = userDetails.get(SessionManager.KEY_USERNAME);
        String name = userDetails.get(SessionManager.KEY_FIRSTNAME);
        String lastName = userDetails.get(SessionManager.KEY_LASTNAME);
        String phone = userDetails.get(SessionManager.KEY_PHONE);
        String address = userDetails.get(SessionManager.KEY_ADDRESS);

        textUsername.setText(username);
        textName.setText(name);
        textLastName.setText(lastName);
        textPhone.setText(phone);
        textAddress.setText(address);

        editName.setText(name);
        editLastName.setText(lastName);
        editPhone.setText(phone);
        editAddress.setText(address);


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