package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.medenjaci.util.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText editPassOld, editPassNew, editPassNewConfirm;

    TextView newPass;
    AwesomeValidation awesomeValidation;
    Button buttonChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemDetailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        toolbar.setOverflowIcon(drawable);

        editPassOld = (EditText) findViewById(R.id.changeOldPass);
        editPassNew = (EditText) findViewById(R.id.changeNewPass);
        editPassNewConfirm = (EditText) findViewById(R.id.changeNewPassConf);

        newPass = (TextView) findViewById(R.id.changeNewPass);
        buttonChange = (Button) findViewById(R.id.changePassChange);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userDetails = sessionManager.getUsersDetailFromSession();

        String username = userDetails.get(SessionManager.KEY_USERNAME);
        String pass = userDetails.get(SessionManager.KEY_PASSWORD);
        //add validation
        awesomeValidation.addValidation(this, R.id.changeOldPass, pass, R.string.invalid_old_pass);
        awesomeValidation.addValidation(this, R.id.changeNewPass, ".{8,50}", R.string.invalid_password);

        awesomeValidation.addValidation(this, R.id.changeNewPassConf, R.id.changeNewPass, R.string.invalid_pass_confirm);

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(SessionManager.KEY_PASSWORD, newPass.getText().toString());

                    sessionManager.change_info(hashMap);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("password", newPass.getText().toString());

                    reference.child(username).updateChildren(updateMap);

                    Toast.makeText(getApplicationContext(), "Lozinka je uspešno promenjena!", Toast.LENGTH_LONG).show();
                }
                editPassOld.setText(null);
                editPassNew.setText(null);
                editPassNewConfirm.setText(null);
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