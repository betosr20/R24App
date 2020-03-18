package com.example.r24app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Activities.MapActivity;

public class Login extends AppCompatActivity {
    Button btnlogin;
    TextInputLayout emailLayout,passwordLayout;
    TextInputEditText emailEditText, passwordEditText;
    FirebaseAuth mAuth;
    TextView recoveryPasswordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnlogin = findViewById(R.id.idBtnLoging);
        emailEditText =  findViewById(R.id.etLoginEmail);
        passwordEditText =  findViewById(R.id.etLoginPassword);
        recoveryPasswordText = findViewById(R.id.loginRecoveryPassword);
        mAuth = FirebaseAuth.getInstance();
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn() {
        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Login.this, "Se ingresaron correctamente las credenciales", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    getTransitionIntoMainView();
                }else {
                    Toast.makeText(Login.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getTransitionIntoMainView() {
        Intent intent =  new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }
}
