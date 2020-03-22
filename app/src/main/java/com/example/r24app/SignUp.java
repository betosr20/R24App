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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnNextStep;
    private String fakeURLImage, email ,passwordFake;
    TextInputLayout  inputLayoutName, inputLayoutLastName, inputLayoutUserName, inputLayoutCellPhone, inputLayoutAddress;
    TextInputEditText  name, lastName, userName, cellPhone, address;
    private boolean alerts, notifications, needHelp, isActive, timeConfiguration, isOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        mAuth = FirebaseAuth.getInstance();
        // Input y editText de Nombre
        inputLayoutName =  findViewById(R.id.LayoutName);
        name = findViewById(R.id.etName);
        // Input y editText de Apellidos
        inputLayoutLastName = findViewById(R.id.LayoutLast);
        lastName = findViewById(R.id.etLastName);
        // Input y editText de nombre de usuario
        inputLayoutUserName = findViewById(R.id.LayoutUserName);
        userName = findViewById(R.id.etUserName);
        // Input y editText de Numero de telefono
        inputLayoutCellPhone = findViewById(R.id.LayoutCellPhone);
        cellPhone = findViewById(R.id.etCellPhone);
        // Input y editText de direccion
        inputLayoutAddress = findViewById(R.id.LayoutAddress);
        address = findViewById(R.id.etAddres);
        // boton que se encarga a disparar la accion de registarse en FireBase.
        btnNextStep = findViewById(R.id.btnSignUp);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStepToRegisterUser();
            }
        });
        // boleanos por default
        alerts = true;
        notifications = true;
        needHelp =  false;
        isActive = true;
        timeConfiguration = true;
        isOk = true;
    }
    /*
        Metodo que se encarga de extraer los datos de la vista sign_up y enviarlos a Firebase.
     */
   public void nextStepToRegisterUser() {
        if (validateInputs() != false) {
            Intent intent =  new Intent(this, PasswordValidation.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("lastName", lastName.getText().toString());
            intent.putExtra("userName", userName.getText().toString());
            intent.putExtra("cellPhone", cellPhone.getText().toString());
            intent.putExtra("address", address.getText().toString());
            startActivity(intent);
            finish();
        }
   }
   public boolean validateInputs() {
       boolean isValid = true;
       if (name.getText() != null && name.getText().toString().trim().isEmpty()) {
           inputLayoutName.setError("Espacio requerido *");
           inputLayoutName.requestFocus();
           isValid = false;
       } else {
           inputLayoutName.setError(null);
       }
       if (lastName.getText() != null && lastName.getText().toString().trim().isEmpty()) {
           inputLayoutLastName.setError("Espacio requerido *");
           inputLayoutLastName.requestFocus();
           isValid = false;
       } else {
           inputLayoutLastName.setError(null);
       }
       if (userName.getText() != null && userName.getText().toString().trim().isEmpty()) {
           inputLayoutUserName.setError("Espacio requerido *");
           inputLayoutUserName.requestFocus();
           isValid = false;
       } else {
           inputLayoutUserName.setError(null);
       }
       if (cellPhone.getText() != null && cellPhone.getText().toString().trim().isEmpty()) {
           inputLayoutCellPhone.setError("Espacio requerido *");
           inputLayoutCellPhone.requestFocus();
           isValid = false;
       } else {
           inputLayoutCellPhone.setError(null);
       }
       if (address.getText() != null && address.getText().toString().trim().isEmpty()) {
           inputLayoutAddress.setError("Espacio requerido *");
           inputLayoutAddress.requestFocus();
           isValid = false;
       } else {
           inputLayoutAddress.setError(null);
       }

       return isValid;
   }


}
