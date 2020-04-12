package Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.MainActivity;
import com.example.r24app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Models.POJOS.User;
import Services.FirebaseNotificationService;
import Services.UserService;

public class PasswordValidation extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name, lastName, userName, cellPhone, address, userId;
    private TextInputEditText password1, email;
    private TextInputLayout layoutEmail, layoutPassword;
    private UserService userService;
    private Uri chosenImageData;
    private User refreshUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_validation);
        mAuth = FirebaseAuth.getInstance();
        userService = new UserService();

        //Extraer los valores que vienen de la vista Singup
        email = findViewById(R.id.etEmailSignUp);
        name = getIntent().getStringExtra("name");
        lastName = getIntent().getStringExtra("lastName");
        userName = getIntent().getStringExtra("userName");
        cellPhone = getIntent().getStringExtra("cellPhone");
        address = getIntent().getStringExtra("address");
        //profileImage = getIntent().getStringExtra("profileImage");
        chosenImageData = Uri.parse(getIntent().getStringExtra("imageUri"));

        Button btnSignUp = findViewById(R.id.idbtnSigup);
        password1 = findViewById(R.id.etPassword1Step2);
        //layouts
        layoutEmail = findViewById(R.id.LayoutEmailSignUp);
        layoutPassword = findViewById(R.id.LayoutPassword1Step2);
        //boton
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        if (validateInputs()) {
            layoutEmail.setError(null);

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userId = task.getResult().getUser().getUid();

                        User newUser = new User(userId, name, lastName, userName.toLowerCase(), email.getText().toString(), cellPhone, address, "",
                                true, true, true, false, true, true, true, true, false);
                        refreshUser = newUser;

                        if (userService.addNewUser(newUser)) {
                            try {
                                new FirebaseNotificationService().subscribeToIncidentNotifications();
                                uploadTheSelectedImageToServer(newUser);
                                Toast.makeText(PasswordValidation.this, "Se ha registrado exitosamente", Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                assert user != null;
                                user.sendEmailVerification();
                                logInViewTransition();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PasswordValidation.this, "Error durante el proceso de registro", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        boolean validEmailAddressFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();

                        if (!validEmailAddressFormat) {
                            layoutEmail.setError(getResources().getText(R.string.invalidEmailFormat));
                            layoutEmail.requestFocus();
                        } else {
                            Toast.makeText(PasswordValidation.this, "Esta dirección de correo ya existe en la base de datos", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }

    private void logInViewTransition() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if (email.getText().toString() != null && email.getText().toString().trim().isEmpty()) {
            layoutEmail.setError(getResources().getText(R.string.requiredField));
            layoutEmail.requestFocus();
            isValid = false;
        } else {
            layoutEmail.setError(null);
        }
        if (password1.getText().toString() != null && password1.getText().toString().trim().isEmpty()) {
            layoutPassword.setError(getResources().getText(R.string.requiredField));
            layoutPassword.requestFocus();
            isValid = false;
        } else {
            layoutPassword.setError(null);

        }

        return isValid;
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private void uploadTheSelectedImageToServer(User newUser) throws IOException {
        if (chosenImageData != null && !TextUtils.isEmpty(chosenImageData.toString())) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
            userService.uploadTheSelectedImageToServer(bitmap, newUser, true);
        }
    }

    public void windowBack(View v) {
        onBackPressed();
    }
}
