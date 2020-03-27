package Activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.r24app.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import Models.POJOS.User;
import Services.UserService;

public class MyProfileActivity extends AppCompatActivity {
    private User currentUser;
    private UserService userService;
    private TextInputEditText nameInput, lastNameInput, usernameInput, phoneNumberInput, addressInput;
    private TextInputLayout nameLayout, lastNameLayout, usernameLayout, phoneNumberLayout, addressLayout;
    private AppCompatImageView deleteImageIcon, chooseImageIcon;
    private Button editSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        currentUser = null;
        userService = new UserService();
        getElementsReference();
        addListeners();
        getCurrentUsetInfo();
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
                validateFields();
            }
        });

        deleteImageIcon.setOnClickListener(v -> {

        });

        chooseImageIcon.setOnClickListener(v -> {

        });
    }

    public void validateFields() {

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

    public void getCurrentUsetInfo() {
        currentUser = userService.getCurrentUserInfo();

        if (currentUser != null) {
            nameInput.setText(currentUser.getName());
            lastNameInput.setText(currentUser.getLastName());
            usernameInput.setText(currentUser.getUserName());
            phoneNumberInput.setText(currentUser.getCellPhone());
            addressInput.setText(currentUser.getAddress());
        }
    }
}
