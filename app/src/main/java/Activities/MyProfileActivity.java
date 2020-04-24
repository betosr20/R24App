package Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import Models.Constants.FirebaseClasses;
import Models.POJOS.CircleTransform;
import Models.POJOS.User;
import Services.UserService;

public class MyProfileActivity extends AppCompatActivity {
    private User currentUser;
    private UserService userService;
    private TextInputEditText nameInput, lastNameInput, usernameInput, phoneNumberInput, addressInput, emailInput;
    private TextInputLayout nameLayout, lastNameLayout, usernameLayout, phoneNumberLayout, addressLayout, emailLayout;
    private ImageView iconEditProfileImage, profileImage;
    private Button editSaveButton;
    private FirebaseDatabase database;
    private Bitmap bitmap;
    private ProgressBar progressBar;
    private Boolean progressBarHide, editPhotoPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setProgressBar();
        database = FirebaseDatabase.getInstance();
        currentUser = null;
        userService = new UserService();
        editPhotoPressed = false;
        getElementsReference();
        addListeners();
        getCurrentUserInfo();
    }

    public void setProgressBar() {
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        progressBarHide = false;
        /*ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();*/
    }

    public void getElementsReference() {
        nameLayout = findViewById(R.id.LayoutName);
        lastNameLayout = findViewById(R.id.LayoutLastName);
        usernameLayout = findViewById(R.id.LayoutUserName);
        phoneNumberLayout = findViewById(R.id.LayoutCellPhone);
        addressLayout = findViewById(R.id.LayoutDetailReport);
        emailLayout = findViewById(R.id.LayoutEmail);
        profileImage = findViewById(R.id.imgBigPhoto);
        iconEditProfileImage = findViewById(R.id.imgEditPhoto);
        editSaveButton = findViewById(R.id.btnEditSave);
        nameInput = findViewById(R.id.etName);
        lastNameInput = findViewById(R.id.etLastName);
        usernameInput = findViewById(R.id.etUserName);
        phoneNumberInput = findViewById(R.id.etCellPhone);
        addressInput = findViewById(R.id.etDetailReport);
        emailInput = findViewById(R.id.etUserEmail);
    }

    public void setImageProfile() {
        editPhotoPressed = true;
        iconEditProfileImage.setImageDrawable(ContextCompat.getDrawable(MyProfileActivity.this, R.drawable.delete));
        Picasso.get().load(currentUser.getProfileImage()).transform(new CircleTransform()).into(profileImage);
        progressBar.setVisibility(View.INVISIBLE);
        progressBarHide = true;

        // Este codigo se puede utilizar para obtener el download URL de la imagen en caso de ser necesario
        /*StorageReference userProfileImageRef = FirebaseStorage.getInstance().getReference();
        StorageReference userProfileImage = userProfileImageRef.child(FirebaseClasses.ProfileImagesFolder + currentUser.getId() + ".png");
        userProfileImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });*/
    }

    public void addListeners() {
        editSaveButton.setOnClickListener(v -> {
            if (editSaveButton.getText().equals("EDITAR") && progressBarHide) {
                editSaveButton.setText(R.string.saveButtonText);
                enableFields();
            } else {
                if (validateFields()) {
                    validateUserName();
                }
            }
        });

        iconEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editPhotoPressed) {
                    selectImage();
                } else {
                    bitmap = null;
                    currentUser.setProfileImage("");
                    editPhotoPressed = false;
                    iconEditProfileImage.setImageDrawable(ContextCompat.getDrawable(MyProfileActivity.this, R.drawable.edit));
                    profileImage.setImageDrawable(ContextCompat.getDrawable(MyProfileActivity.this, R.drawable.ic_person));
                }
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000);
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                //profileImage.setImageBitmap(bitmap);
                Picasso.get().load(data.getData()).transform(new CircleTransform()).into(profileImage);
                editPhotoPressed = true;
                iconEditProfileImage.setImageDrawable(ContextCompat.getDrawable(MyProfileActivity.this, R.drawable.delete));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData() {
        disableFields();
        editSaveButton.setEnabled(false);
        currentUser.setName(nameInput.getText().toString());
        currentUser.setLastName(lastNameInput.getText().toString());
        currentUser.setUsername(usernameInput.getText().toString().toLowerCase());
        currentUser.setCellPhone(phoneNumberInput.getText().toString());
        currentUser.setAddress(addressInput.getText().toString());
        Toast.makeText(MyProfileActivity.this, "Por favor espere...", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userService.updateUser(currentUser)) {
                    try {
                        MyProfileActivity.this.uploadTheSelectedImageToServer();
                        Toast.makeText(MyProfileActivity.this, "Datos actualizados exitosamente", Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyProfileActivity.this, "Los cambios pueden tomar varios minutos en reflejarse", Toast.LENGTH_LONG).show();
                            editSaveButton.setEnabled(true);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }, 3600);

                    //getCurrentUserInfo();
                } else {
                    Toast.makeText(MyProfileActivity.this, "Error durante el proceso de actualización", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }, 3600);
    }

    private void uploadTheSelectedImageToServer() throws IOException {
        userService.uploadTheSelectedImageToServer(bitmap, currentUser, false);
    }

    public void validatePhoneNumber() {
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.CellphoneAttribute).equalTo(phoneNumberInput.getText().toString());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidCellPhone = true;

                if (dataSnapshot.exists()) {
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);

                        if (user.getCellPhone().equals(phoneNumberInput.getText().toString()) && !user.getId().equals(currentUser.getId())) {
                            phoneNumberLayout.setError(getResources().getText(R.string.duplicatedPhoneNumber));
                            phoneNumberLayout.requestFocus();
                            isValidCellPhone = false;
                            break;
                        }
                    }
                }

                if (isValidCellPhone) {
                    saveData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void validateUserName() {
        Query usersQuery = database.getReference(FirebaseClasses.User).orderByChild(FirebaseClasses.UsernameAttribute).equalTo(usernameInput.getText().toString().toLowerCase());

        usersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isValidUserName = true;

                if (dataSnapshot.exists()) {
                    User user;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);
                        String userName = user.getUsername().toLowerCase();
                        String newUsername = usernameInput.getText().toString().toLowerCase();

                        if (userName.equals(newUsername) && !user.getId().equals(currentUser.getId())) {
                            usernameLayout.setError(getResources().getText(R.string.duplicatedUsername));
                            usernameLayout.requestFocus();
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

    public boolean validateFields() {
        boolean isValid = true;

        if (nameInput.getText() != null && nameInput.getText().toString().trim().isEmpty()) {
            nameLayout.setError(getResources().getText(R.string.requiredField));
            nameLayout.requestFocus();
            isValid = false;
        } else {
            nameLayout.setError(null);
        }

        if (lastNameInput.getText() != null && lastNameInput.getText().toString().trim().isEmpty()) {
            lastNameLayout.setError(getResources().getText(R.string.requiredField));
            lastNameLayout.requestFocus();
            isValid = false;
        } else {
            lastNameLayout.setError(null);
        }

        if (usernameInput.getText() != null && usernameInput.getText().toString().trim().isEmpty()) {
            usernameLayout.setError(getResources().getText(R.string.requiredField));
            usernameLayout.requestFocus();
            isValid = false;
        } else {
            usernameLayout.setError(null);
        }

        if (phoneNumberInput.getText() != null && phoneNumberInput.getText().toString().trim().isEmpty()) {
            phoneNumberLayout.setError(getResources().getText(R.string.requiredField));
            phoneNumberLayout.requestFocus();
            isValid = false;
        } else {
            phoneNumberLayout.setError(null);
        }

        if (addressInput.getText() != null && addressInput.getText().toString().trim().isEmpty()) {
            addressLayout.setError(getResources().getText(R.string.requiredField));
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
        emailLayout.setEnabled(false);
        addressLayout.setEnabled(false);
        iconEditProfileImage.setEnabled(false);
        editSaveButton.setText(R.string.editButtonText);
    }

    public void enableFields() {
        nameLayout.setEnabled(true);
        lastNameLayout.setEnabled(true);
        usernameLayout.setEnabled(true);
        phoneNumberLayout.setEnabled(true);
        addressLayout.setEnabled(true);
        iconEditProfileImage.setEnabled(true);
    }

    public void displayUserInfo() {
        if (currentUser != null) {
            nameInput.setText(currentUser.getName());
            emailInput.setText(currentUser.getEmail());
            lastNameInput.setText(currentUser.getLastName());
            usernameInput.setText(currentUser.getUsername());
            phoneNumberInput.setText(currentUser.getCellPhone());
            addressInput.setText(currentUser.getAddress());
            nameLayout.setEndIconVisible(false);
            lastNameLayout.setEndIconVisible(false);
            usernameLayout.setEndIconVisible(false);
            addressLayout.setEndIconVisible(false);
            phoneNumberLayout.setEndIconVisible(false);
            emailLayout.setEndIconVisible(false);
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

                    if (!TextUtils.isEmpty(currentUser.getProfileImage())) {
                        setImageProfile();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        progressBarHide = true;
                        profileImage.setImageDrawable(ContextCompat.getDrawable(MyProfileActivity.this, R.drawable.ic_person));
                    }

                    disableFields();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void windowBack(View v) {
        onBackPressed();
    }
}
