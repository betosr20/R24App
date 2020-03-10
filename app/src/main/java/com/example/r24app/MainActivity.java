package com.example.r24app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import Activities.MapActivity;
import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    TextInputEditText email;
    TextInputLayout inputLayoutEmail;
    Button nextStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        email = findViewById(R.id.emailInput);
        nextStep = findViewById(R.id.btnNextSignUp);
        inputLayoutEmail = findViewById(R.id.emailInputLayout);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyIfUserExits();
            }
        });
    }
    private void verifyIfUserExits() {
        if (email.getText() != null && email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError("Espacio requerido *");
        } else {
            inputLayoutEmail.setError(null);
        }
    }





}
