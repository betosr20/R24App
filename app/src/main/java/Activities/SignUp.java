package Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import Models.Constants.FirebaseClasses;
import Models.POJOS.CircleTransform;
import Models.POJOS.User;

public class SignUp extends AppCompatActivity {
    private ImageView iconEditProfileImage, profileImage;
    private TextInputLayout inputLayoutName, inputLayoutLastName, inputLayoutUserName, inputLayoutCellPhone, inputLayoutAddress;
    private TextInputEditText name, lastName, userName, cellPhone, address;
    private FirebaseDatabase database;
    private Uri chosenImageData;
    private boolean editPhotoPressed;

    public SignUp() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        database = FirebaseDatabase.getInstance();
        editPhotoPressed = false;

        inputLayoutName = findViewById(R.id.LayoutName);
        inputLayoutLastName = findViewById(R.id.LayoutLastName);
        inputLayoutCellPhone = findViewById(R.id.LayoutCellPhone);
        inputLayoutUserName = findViewById(R.id.LayoutUserName);
        inputLayoutAddress = findViewById(R.id.LayoutAddress);

        name = findViewById(R.id.etName);
        lastName = findViewById(R.id.etLastName);
        userName = findViewById(R.id.etUserName);
        cellPhone = findViewById(R.id.etCellPhone);
        address = findViewById(R.id.etAddress);

        Button btnNextStep = findViewById(R.id.btnSignUp);
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStepToRegisterUser();
            }
        });

        profileImage = findViewById(R.id.imgBigPhoto);

        iconEditProfileImage = findViewById(R.id.imgEditPhoto);
        iconEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editPhotoPressed) {
                    selectImage();
                } else {
                    editPhotoPressed = false;
                    iconEditProfileImage.setImageDrawable(ContextCompat.getDrawable(SignUp.this, R.drawable.edit));
                    profileImage.setImageDrawable(ContextCompat.getDrawable(SignUp.this, R.drawable.ic_person));
                }
            }
        });
    }

    public void nextStepToRegisterUser() {
        if (validateInputs()) {
            validateUserName();
        }
    }

    public void moveToNextScreen() {
        Intent intent = new Intent(this, PasswordValidation.class);
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("lastName", lastName.getText().toString());
        intent.putExtra("userName", userName.getText().toString());
        intent.putExtra("cellPhone", cellPhone.getText().toString());
        intent.putExtra("address", address.getText().toString());

        if (chosenImageData != null) {
            intent.putExtra("imageUri", chosenImageData.toString());
        } else {
            intent.putExtra("imageUri", "");
        }

        startActivity(intent);
    }

    public boolean validateInputs() {
        boolean isValid = true;

        if (name.getText() != null && name.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getResources().getText(R.string.requiredField));
            inputLayoutName.requestFocus();
            isValid = false;
        } else {
            inputLayoutName.setError(null);
        }

        if (lastName.getText() != null && lastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getResources().getText(R.string.requiredField));
            inputLayoutLastName.requestFocus();
            isValid = false;
        } else {
            inputLayoutLastName.setError(null);
        }

        if (userName.getText() != null && userName.getText().toString().trim().isEmpty()) {
            inputLayoutUserName.setError(getResources().getText(R.string.requiredField));
            inputLayoutUserName.requestFocus();
            isValid = false;
        } else {
            inputLayoutUserName.setError(null);
        }

        if (cellPhone.getText() != null && cellPhone.getText().toString().trim().isEmpty()) {
            inputLayoutCellPhone.setError(getResources().getText(R.string.requiredField));
            inputLayoutCellPhone.requestFocus();
            isValid = false;
        } else {
            inputLayoutCellPhone.setError(null);
        }

        if (address.getText() != null && address.getText().toString().trim().isEmpty()) {
            inputLayoutAddress.setError(getResources().getText(R.string.requiredField));
            inputLayoutAddress.requestFocus();
            isValid = false;
        } else {
            inputLayoutAddress.setError(null);
        }

        return isValid;
    }

    public void validatePhoneNumber() {
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.CellphoneAttribute).equalTo(cellPhone.getText().toString());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidCellPhone = true;

                if (dataSnapshot.exists()) {
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);

                        if (user.getCellPhone().equals(cellPhone.getText().toString())) {
                            inputLayoutCellPhone.setError(getResources().getText(R.string.duplicatedPhoneNumber));
                            inputLayoutCellPhone.requestFocus();
                            isValidCellPhone = false;
                            break;
                        }
                    }
                }

                if (isValidCellPhone) {
                    moveToNextScreen();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validateUserName() {
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.UsernameAttribute).equalTo(userName.getText().toString().toLowerCase());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidUserName = true;

                if (dataSnapshot.exists()) {
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);

                        String userNameFound = user.getUsername().toLowerCase();
                        String newUsername = userName.getText().toString().toLowerCase();

                        if (userNameFound.equals(newUsername)) {
                            inputLayoutUserName.setError(getResources().getText(R.string.duplicatedUsername));
                            inputLayoutUserName.requestFocus();
                            isValidUserName = false;
                            break;
                        }
                    }
                }

                if (isValidUserName) {
                    validatePhoneNumber();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void selectImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            chosenImageData = data.getData();

            try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                //profileImage.setImageBitmap(bitmap);

                Picasso.get().load(data.getData()).transform(new CircleTransform()).into(profileImage);
                editPhotoPressed = true;
                iconEditProfileImage.setImageDrawable(ContextCompat.getDrawable(SignUp.this, R.drawable.delete));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*private void deleteImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Eliminar Imagen")
                .setMessage(R.string.detalle_dialogDelete_message)
                .setPositiveButton(R.string.label_dialog_delete, (dialogInterface, i) -> configImageView(null))
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.show();
    }*/

    public void windowBack(View v) {
        onBackPressed();
    }
}
