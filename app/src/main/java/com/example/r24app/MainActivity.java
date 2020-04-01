package com.example.r24app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Activities.MapActivity;
import Activities.RecoveryPassword;
import Activities.SignUp;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    TextInputEditText email, password;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private TextView recoveryPassword, singUpLink;
    Button ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTransitionIntoMainView();
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.etLoginPassword);
        ingresar = findViewById(R.id.btnNextSignUp);
        inputLayoutEmail = findViewById(R.id.emailInputLayout);
        inputLayoutPassword = findViewById(R.id.LayoutLoginPassword);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        recoveryPassword = findViewById(R.id.textRecoveryPassword);
        recoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionRecoveryPasswordView();
            }
        });
        singUpLink = findViewById(R.id.textCreateAcount);
        singUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionSingUpView();
            }
        });
    }

    private void login() {
        if (validateInputs() != false) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Se ingresaron correctamente las credenciales", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        getTransitionIntoMainView();
                    } else {
                        Toast.makeText(MainActivity.this, "Las credenciales ingresadas no son válidas", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void getTransitionIntoMainView() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    private void transitionSingUpView() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
        finish();
    }

    private void transitionRecoveryPasswordView() {
        Intent intent = new Intent(this, RecoveryPassword.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if (email.getText() != null && email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError("Espacio requerido *");
            inputLayoutEmail.requestFocus();
            isValid = false;
        } else {
            inputLayoutEmail.setError(null);
        }

        if (password.getText() != null && password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Espacio requerido *");
            isValid = false;
        } else {
            inputLayoutPassword.setError(null);
        }
        return isValid;
    }
}
