package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.r24app.MainActivity;
import com.example.r24app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecoveryPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout layoutEmail;
    TextInputEditText etEmail;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        mAuth = FirebaseAuth.getInstance();
        layoutEmail = findViewById(R.id.LayoutRecoveryPasswordEmail);
        etEmail = findViewById(R.id.etRecoveryPasswordEmail);
        btnChangePassword = findViewById(R.id.idbtnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        if (validateInputs()) {
            layoutEmail.setError(null);

            mAuth.sendPasswordResetEmail(etEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(RecoveryPassword.this, "Se ha enviado un mensaje a su correo para iniciar el proceso.", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(RecoveryPassword.this, MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    boolean validEmailAddressFormat = android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches();

                    if (!validEmailAddressFormat) {
                        layoutEmail.setError(getResources().getText(R.string.invalidEmailFormat));
                        layoutEmail.requestFocus();
                    } else {
                        Toast.makeText(RecoveryPassword.this, "El correo ingresado no existe en la base de datos, por favor ingrese un correo electrónico válido.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if (etEmail.getText() != null && etEmail.getText().toString().trim().isEmpty()) {
            layoutEmail.setError(getResources().getText(R.string.requiredField));
            layoutEmail.requestFocus();
            isValid = false;
        } else {
            layoutEmail.setError(null);
        }
        return isValid;
    }

    public void windowBack(View v) {
        onBackPressed();
    }
}
