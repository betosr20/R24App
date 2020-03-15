package com.example.r24app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnSignUp;
    private String fakeURLImage, email ,passwordFake;
    TextInputLayout inputLayoutEmail, inputLayoutName, inputLayoutLastName, inputLayoutUserName, inputLayoutCellPhone, inputLayoutAddress;
    TextInputEditText  name, lastName, userName, cellPhone, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        fakeURLImage = "facebook.com";
        passwordFake = "danilo123456";
        // Valor del email
        email = getIntent().getStringExtra("emailValue");
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
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    /*
        Metodo que se encarga de extraer los datos de la vista sign_up y enviarlos a Firebase.
     */
   public void registerUser() {
       mAuth.createUserWithEmailAndPassword(email, passwordFake).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if (task.isSuccessful()) {
                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("name")
                           .setValue(name.getText().toString());

                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("lastname")
                           .setValue(lastName.getText().toString());

                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("email")
                           .setValue(email);


                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("username")
                           .setValue(userName.getText().toString());

                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("phoneNumber")
                           .setValue(cellPhone.getText().toString());

                   FirebaseDatabase.getInstance().getReference()
                           .child("Users").child(task.getResult()
                           .getUser().getUid()).child("address")
                           .setValue(address.getText().toString());

                   Toast.makeText(SignUp.this, "Se ha registrado exitosamente", Toast.LENGTH_LONG).show();

               } else {
                   Toast.makeText(SignUp.this, "Fallo de registro, intentarlo más tarde", Toast.LENGTH_LONG).show();
               }
           }
       });
   }
}
