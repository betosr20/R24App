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
        currentUser.setName(nameInput.getText().toString());
        currentUser.setLastName(lastNameInput.getText().toString());
        currentUser.setUsername(usernameInput.getText().toString());
        currentUser.setCellPhone(phoneNumberInput.getText().toString());
        currentUser.setAddress(addressInput.getText().toString());

        if (userService.updateUser(currentUser)) {
            Toast.makeText(MyProfileActivity.this, "Datos actualizados exitosamente", Toast.LENGTH_LONG).show();
            getCurrentUserInfo();
            disableFields();
        } else {
            Toast.makeText(MyProfileActivity.this, "Error durante el proceso de actualización", Toast.LENGTH_LONG).show();
        }
    }

    public void validatePhoneNumber() {
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.CellphoneAttribute).equalTo(currentUser.getCellPhone());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isValidCellPhone = true;
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);

                        if (user.getCellPhone().equals(currentUser.getCellPhone()) && !user.getId().equals(currentUser.getId())) {
                            phoneNumberLayout.setError("El némero de teléfono ya existe");
                            phoneNumberLayout.requestFocus();
                            isValidCellPhone = false;
                            break;
                        }
                    }

                    if (isValidCellPhone) {
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
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.UsernameAttribute).equalTo(currentUser.getUsername());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isValidUserName = true;
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);

                        if (user.getUsername().equals(currentUser.getUsername()) && !user.getId().equals(currentUser.getId())) {
                            usernameLayout.setError("El nombre de usuario ya existe");
                            usernameLayout.requestFocus();
                            isValidUserName = false;
                            break;
                        }
                    }

                    if (isValidUserName) {
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
        editSaveButton.setText(R.string.editButtonText);
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
            usernameInput.setText(currentUser.getUsername());
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
