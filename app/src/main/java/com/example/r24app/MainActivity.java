package com.example.r24app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Activities.MapActivity;
import Activities.RecoveryPassword;
import Activities.SignUp;
import Models.Constants.FirebaseClasses;
import Models.POJOS.LoadingDialog;
import Models.POJOS.User;
import Services.UserService;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private User user;
    private UserService userService = new UserService();
    private TextInputEditText email, password;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.etLoginPassword);
        login = findViewById(R.id.btnNextSignUp);
        inputLayoutEmail = findViewById(R.id.emailInputLayout);
        inputLayoutPassword = findViewById(R.id.LayoutLoginPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        TextView recoveryPassword = findViewById(R.id.textRecoveryPassword);

        recoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionRecoveryPasswordView();
            }
        });

        TextView singUpLink = findViewById(R.id.textCreateAcount);
        singUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionSingUpView();
            }
        });
    }

    private void login() {
        login.setEnabled(false);


        if (validateInputs()) {
            inputLayoutEmail.setError(null);
            LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
            loadingDialog.startLoadingDialog();
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        getTransitionIntoMainView();
                    } else {

                        boolean validEmailAddressFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();

                        if (!validEmailAddressFormat) {
                            inputLayoutEmail.setError(getResources().getText(R.string.invalidEmailFormat));
                            inputLayoutEmail.requestFocus();
                        } else {
                            Toast.makeText(MainActivity.this, "Las credenciales ingresadas no son válidas", Toast.LENGTH_LONG).show();
                        }
                        loadingDialog.dismissDialog();
                        login.setEnabled(true);
                    }
                }
            });
        }
        login.setEnabled(true);
    }

    private void getTransitionIntoMainView() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
        finish();
    }

    private void transitionSingUpView() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    private void transitionRecoveryPasswordView() {
        Intent intent = new Intent(this, RecoveryPassword.class);
        startActivity(intent);
    }

    private boolean validateInputs() {
        boolean isValid = true;

        if (email.getText() != null && email.getText().toString().trim().isEmpty()) {
            inputLayoutEmail.setError(getResources().getText(R.string.requiredField));
            inputLayoutEmail.requestFocus();
            isValid = false;
        } else {
            inputLayoutEmail.setError(null);
        }

        if (password.getText() != null && password.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getResources().getText(R.string.requiredField));
            isValid = false;
        } else {
            inputLayoutPassword.setError(null);
        }

        return isValid;
    }

    public void windowBack(View v) {
        finish();
    }

}
