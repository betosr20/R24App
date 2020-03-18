package com.example.r24app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordValidation extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnSignUp;
    private boolean alerts, notifications, needHelp, isActive, timeConfiguration, isOk;
    private String email, fakeURLImage, name, lastName, userName, cellPhone, address;
    TextInputEditText password1, password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_validation);
        mAuth = FirebaseAuth.getInstance();
        fakeURLImage = "facebook.com";
        // boleanos por default
        alerts = true;
        notifications = true;
        needHelp =  false;
        isActive = true;
        timeConfiguration = true;
        isOk = true;
        //Extraer ;os va;ores que vienen de la vista Singup
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");
        lastName = getIntent().getStringExtra("lastName");
        userName = getIntent().getStringExtra("userName");
        cellPhone = getIntent().getStringExtra("cellPhone");
        address = getIntent().getStringExtra("address");
        //
        btnSignUp = findViewById(R.id.idbtnSigup);
        password1 = findViewById(R.id.etPassword1Step2);
        password2 = findViewById(R.id.etPassword2Step2);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password1.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("name")
                            .setValue(name);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("lastname")
                            .setValue(lastName);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("email")
                            .setValue(email);


                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("username")
                            .setValue(userName);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("cellPhone")
                            .setValue(cellPhone);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("address")
                            .setValue(address);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("profileImage")
                            .setValue(fakeURLImage);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("alerts")
                            .setValue(alerts);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("notifications")
                            .setValue(notifications);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("needHelp")
                            .setValue(needHelp);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("isActive")
                            .setValue(isActive);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("timeConfiguration")
                            .setValue(timeConfiguration);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(task.getResult()
                            .getUser().getUid()).child("isOk")
                            .setValue(isOk);



                    Toast.makeText(PasswordValidation.this, "Se ha registrado exitosamente", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification();
                    logInViewTransition();

                } else {
                    Toast.makeText(PasswordValidation.this, "Este usuario ya existe en la base de datos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void logInViewTransition() {
        Intent intent =  new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
