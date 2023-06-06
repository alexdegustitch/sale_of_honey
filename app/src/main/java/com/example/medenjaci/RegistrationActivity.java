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
import com.example.medenjaci.util.SessionManager;
import com.example.medenjaci.util.Singleton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {

    EditText username, password, password2, address, name, lastname, phone;
    TextInputLayout textInputLayoutUsername, textInputLayoutPassword, textInputLayoutPassword2, textInputLayoutFirstName, textInputLayoutLastName, textInputLayoutPhone, textInputLayoutAddress;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.registration_username);
        password = (EditText) findViewById(R.id.registration_password);
        password2 = (EditText) findViewById(R.id.registration_password2);
        address = (EditText) findViewById(R.id.registration_address);
        name = (EditText) findViewById(R.id.registration_firstname);
        lastname = (EditText) findViewById(R.id.registration_lastname);
        phone = (EditText) findViewById(R.id.registration_phone);

        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.wrap_registration_username);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.wrap_registration_password);
        textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.wrap_registration_firstname);
        textInputLayoutPassword2 = (TextInputLayout) findViewById(R.id.wrap_registration_password2);
        textInputLayoutLastName = (TextInputLayout) findViewById(R.id.wrap_registration_lastname);
        textInputLayoutPhone = (TextInputLayout) findViewById(R.id.wrap_registration_phone);
        textInputLayoutAddress = (TextInputLayout) findViewById(R.id.wrap_registration_address);


    }

    private Boolean check_phone_number(String phone_number) {
        String regex = "\\d{9,10}";
        return phone_number.matches(regex);
    }

    private Boolean check_only_letters(String name) {
        String regex = "[A-Za-z]+( ([A-Z]|[a-z])+)*";
        return name.matches(regex);
    }

    private Boolean check_address(String address) {
        String regex = "(([A-Z]|[a-z])+ )+(bb|\\d+[a-z]|\\d+), ([A-Z]|[a-z])+( ([A-Z]|[a-z])+)*";
        return address.matches(regex);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void register(View view) {

        boolean error = false;

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");


        String text_username = username.getText().toString();
        String text_pass = password.getText().toString();
        String text_pass2 = password2.getText().toString();
        String text_address = address.getText().toString();
        String text_name = name.getText().toString();
        String text_lastname = lastname.getText().toString();
        String text_phone = phone.getText().toString();

        textInputLayoutPhone.setError(null);
        textInputLayoutUsername.setError(null);
        textInputLayoutLastName.setError(null);
        textInputLayoutFirstName.setError(null);
        textInputLayoutPhone.setError(null);
        textInputLayoutAddress.setError(null);
        textInputLayoutPassword2.setError(null);
        textInputLayoutPassword.setError(null);

        if (text_username.equals("")) {
            textInputLayoutUsername.setError("Morate uneti korisnicko ime.");
            error = true;
        }

        if (text_pass.equals("")) {
            textInputLayoutPassword.setError("Morate uneti lozinku.");
            error = true;
        }

        if (text_pass2.equals("")) {
            textInputLayoutPassword2.setError("Morate uneti potvrdu lozinke.");
            error = true;
        }

        if (text_address.equals("")) {
            textInputLayoutAddress.setError("Morate uneti adresu.");
            error = true;
        }

        if (text_lastname.equals("")) {
            textInputLayoutLastName.setError("Morate uneti prezime.");
            error = true;
        }

        if (text_name.equals("")) {
            textInputLayoutFirstName.setError("Morate uneti ime.");
            error = true;
        }

        if (text_phone.equals("")) {
            textInputLayoutPhone.setError("Morate uneti kontakt telefon.");
            error = true;
        }

        if (error) {
            password2.setText(null);
            password.setText(null);
            return;
        }

        Query query = reference.orderByChild("username").equalTo(text_username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean error = false;
                if (snapshot.exists()) {
                    textInputLayoutUsername.setError("Korisnicko ime je zauzeto.");
                    password2.setText("");
                    password.setText("");
                    return;
                } else {
                    if (text_pass.length() < 8) {
                        textInputLayoutPassword.setError("Lozinka mora sadrzati minimum 8 karaktera.");
                        password2.setText("");
                        password.setText("");
                        return;
                    }

                    if (!text_pass.equals(text_pass2)) {
                        textInputLayoutPassword2.setError("Potvrda lozinke nije korektna.");
                        password2.setText("");
                        password.setText("");
                        return;
                    }

                    if (!check_address(text_address)) {
                        textInputLayoutAddress.setError("Adresa nije u ispravnom formatu.");
                        error = true;
                    }

                    if (!check_phone_number(text_phone)) {
                        textInputLayoutPhone.setError("Kontakt nije u ispravnom formatu.");
                        error = true;
                    }

                    if (!check_only_letters(text_name)) {
                        textInputLayoutFirstName.setError("Ime moze sadrzati samo slova.");
                        error = true;
                    }

                    if (!check_only_letters(text_lastname)) {
                        textInputLayoutLastName.setError("Prezime moze sadrzati samo slova.");
                        error = true;
                    }

                    if (error) {
                        password2.setText("");
                        password.setText("");
                        return;
                    } else {


                        User user = new User(text_username, text_pass, text_name, text_lastname, text_phone, text_address, "K");
                        reference.child(text_username).setValue(user);


                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession(user.getUsername(), user.getFirst_name(), user.getLast_name(), user.getPassword(), user.getAddress(), user.getPhone_number(), String.valueOf(user.getType()));

                        startActivity(new Intent(getApplicationContext(), IndexConsumerActivity.class));

                    }
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
        textInputLayoutLastName.setError(null);
        textInputLayoutFirstName.setError(null);
        textInputLayoutPhone.setError(null);
        textInputLayoutAddress.setError(null);
        textInputLayoutPassword2.setError(null);
        textInputLayoutPassword.setError(null);
        textInputLayoutUsername.setError(null);

        username.setText("");
        password.setText("");
        password2.setText("");
        lastname.setText("");
        name.setText("");
        phone.setText("");
        address.setText("");

    }
}