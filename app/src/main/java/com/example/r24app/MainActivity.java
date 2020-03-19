package com.example.r24app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import Activities.MapActivity;
import Activities.ReportIncidentActivity;

public class MainActivity extends AppCompatActivity {
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
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        if (email.getText() != null && email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError("Espacio requerido *");
        } else {
            inputLayoutEmail.setError(null);
            Intent intent = new Intent(this, SignUp.class);
            intent.putExtra("emailValue", email.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.map2:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                return true;
            case R.id.report:
                Intent reportActivity = new Intent(this, ReportIncidentActivity.class);
                startActivity(reportActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
