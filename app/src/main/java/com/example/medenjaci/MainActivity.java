package com.example.medenjaci;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medenjaci.beans.User;
import com.example.medenjaci.models.UserModel;
import com.example.medenjaci.util.SessionManager;
import com.example.medenjaci.util.Singleton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);

    }

    public void go_to_register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }


    public void login(View view) {

        String user_name = username.getText().toString();
        String pass = password.getText().toString();
        TextInputLayout textUsername = (TextInputLayout) findViewById(R.id.wrap_login_username);
        TextInputLayout textPass = (TextInputLayout) findViewById(R.id.wrap_login_password);

        textUsername.setError(null);
        textPass.setError(null);

        boolean error = false;

        if (user_name.equals("")) {
            textUsername.setError("Morate uneti korisnicko ime.");
            error = true;
        }

        if (pass.equals("")) {
            textPass.setError("Morate uneti lozinku.");
            error = true;
        }

        if (error)
            return;

        //da li korisnik postoji
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("users");

        Query query = reference.orderByChild("username").equalTo(user_name);
        //User user = singleton.find_user_by_username(user_name);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passfromDB = snapshot.child(user_name).child("password").getValue(String.class);
                    String type = snapshot.child(user_name).child("type").getValue(String.class);
                    if (type.equals("P")) {
                        textPass.setError("Ne postoji korisnik sa unetim korisnickim imenom.");
                        password.setText(null);
                        return;
                    }
                    if (passfromDB.equals(pass)) {
                        String first_name = snapshot.child(user_name).child("first_name").getValue(String.class);
                        String last_name = snapshot.child(user_name).child("last_name").getValue(String.class);
                        String phone_number = snapshot.child(user_name).child("phone_number").getValue(String.class);
                        String address = snapshot.child(user_name).child("address").getValue(String.class);

                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(user_name, first_name, last_name, pass, address, phone_number, type);

                        startActivity(new Intent(getApplicationContext(), IndexConsumerActivity.class));
                    } else {
                        textPass.setError("Uneta lozinka nije korektna.");
                        password.setText("");
                    }
                } else {
                    textUsername.setError("Ne postoji korisnik sa unetim korisnickim imenom.");
                    password.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        TextInputLayout textUsername = (TextInputLayout) findViewById(R.id.wrap_login_username);
        TextInputLayout textPass = (TextInputLayout) findViewById(R.id.wrap_login_password);
        textPass.setError(null);
        textUsername.setError(null);

        username.setText("");
        password.setText("");

    }
}