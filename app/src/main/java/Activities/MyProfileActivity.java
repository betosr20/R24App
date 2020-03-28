package Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;
import Services.UserService;

public class MyProfileActivity extends AppCompatActivity {
    private User currentUser;
    private UserService userService;
    private TextInputEditText nameInput, lastNameInput, usernameInput, phoneNumberInput, addressInput;
    private TextInputLayout nameLayout, lastNameLayout, usernameLayout, phoneNumberLayout, addressLayout;
    private AppCompatImageView deleteImageIcon, chooseImageIcon;
    private Button editSaveButton;
    private FirebaseDatabase database;
    private boolean isValidUsername, isValidCellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        database = FirebaseDatabase.getInstance();
        currentUser = null;
        userService = new UserService();
        getElementsReference();
        addListeners();
        getCurrentUserInfo();
        isValidCellphone = true;
        isValidUsername = true;
    }

    public void getElementsReference() {
        nameLayout = findViewById(R.id.LayoutName);
        lastNameLayout = findViewById(R.id.LayoutLastName);
        usernameLayout = findViewById(R.id.LayoutUserName);
        phoneNumberLayout = findViewById(R.id.LayoutCellPhone);
        addressLayout = findViewById(R.id.LayoutAddress);
        deleteImageIcon = findViewById(R.id.deletePhotoButton);
        chooseImageIcon = findViewById(R.id.selectImageButton);
        editSaveButton = findViewById(R.id.btnEditSave);
        nameInput = findViewById(R.id.etName);
        lastNameInput = findViewById(R.id.etLastName);
        usernameInput = findViewById(R.id.etUserName);
        phoneNumberInput = findViewById(R.id.etCellPhone);
        addressInput = findViewById(R.id.etAddress);
    }

    public void addListeners() {
        editSaveButton.setOnClickListener(v -> {
            if (editSaveButton.getText().equals("EDITAR")) {
                editSaveButton.setText(R.string.saveButtonText);
                enableFields();
            } else {
                if (validateFields()) {
                    validateUserName();
                }
            }
        });

        deleteImageIcon.setOnClickListener(v -> {

        });

        chooseImageIcon.setOnClickListener(v -> {

        });
    }

    public void saveData() {
        Toast.makeText(MyProfileActivity.this, "Datos actualizados correctamente", Toast.LENGTH_LONG).show();
    }

    public void validatePhoneNumber() {
        Query usersQuey = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.CellphoneAttribute).equalTo(currentUser.getCellPhone());
        usersQuey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getCellPhone().equals(currentUser.getCellPhone()) && !user.getId().equals(currentUser.getId())) {
                        Toast.makeText(MyProfileActivity.this, "El número de teléfono ya existe", Toast.LENGTH_LONG).show();
                    } else {
                        saveData();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validateUserName() {
        Query usersQuey = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.UsernameAttribute).equalTo(currentUser.getUserName());
        usersQuey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getUserName().equals(currentUser.getUserName()) && !user.getId().equals(currentUser.getId())) {
                        Toast.makeText(MyProfileActivity.this, "El nombre de usuario ya existe", Toast.LENGTH_LONG).show();
                    } else {
                        validatePhoneNumber();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean validateFields() {
        boolean isValid = true;

        if (nameInput.getText() != null && nameInput.getText().toString().trim().isEmpty()) {
            nameLayout.setError("Espacio requerido *");
            nameLayout.requestFocus();
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (lastNameInput.getText() != null && lastNameInput.getText().toString().trim().isEmpty()) {
            lastNameLayout.setError("Espacio requerido *");
            lastNameLayout.requestFocus();
            isValid = false;
        } else {
            lastNameLayout.setError(null);
        }

        if (usernameInput.getText() != null && usernameInput.getText().toString().trim().isEmpty()) {
            usernameLayout.setError("Espacio requerido *");
            usernameLayout.requestFocus();
            isValid = false;
        } else {
            usernameLayout.setError(null);
        }

        if (phoneNumberInput.getText() != null && phoneNumberInput.getText().toString().trim().isEmpty()) {
            phoneNumberLayout.setError("Espacio requerido *");
            phoneNumberLayout.requestFocus();
            isValid = false;
        } else {
            phoneNumberLayout.setError(null);
        }

        if (addressInput.getText() != null && addressInput.getText().toString().trim().isEmpty()) {
            addressLayout.setError("Espacio requerido *");
            addressLayout.requestFocus();
            isValid = false;
        } else {
            addressLayout.setError(null);
        }

        return isValid;
    }

    public void disableFields() {
        nameLayout.setEnabled(false);
        lastNameLayout.setEnabled(false);
        usernameLayout.setEnabled(false);
        phoneNumberLayout.setEnabled(false);
        addressLayout.setEnabled(false);
        deleteImageIcon.setEnabled(false);
        chooseImageIcon.setEnabled(false);
    }

    public void enableFields() {
        nameLayout.setEnabled(true);
        lastNameLayout.setEnabled(true);
        usernameLayout.setEnabled(true);
        phoneNumberLayout.setEnabled(true);
        addressLayout.setEnabled(true);
        deleteImageIcon.setEnabled(true);
        chooseImageIcon.setEnabled(true);
    }

    public void displayUserInfo() {
        if (currentUser != null) {
            nameInput.setText(currentUser.getName());
            lastNameInput.setText(currentUser.getLastName());
            usernameInput.setText(currentUser.getUserName());
            phoneNumberInput.setText(currentUser.getCellPhone());
            addressInput.setText(currentUser.getAddress());
            nameLayout.setEndIconVisible(false);
            lastNameLayout.setEndIconVisible(false);
            usernameLayout.setEndIconVisible(false);
            addressLayout.setEndIconVisible(false);
            phoneNumberLayout.setEndIconVisible(false);
        }
    }

    public void getCurrentUserInfo() {
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.User).child(userService.getCurrentFirebaseUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUser = dataSnapshot.getValue(User.class);
                    displayUserInfo();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
