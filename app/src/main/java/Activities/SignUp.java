package Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.r24app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnNextStep;
    private ImageView imgSelectImage;
    private ImageView iconimgSelectImage;
    private ImageView iconDeleteImage;
    private String imageIdentifier, uploadedImageLink;
    TextInputLayout inputLayoutName, inputLayoutLastName, inputLayoutUserName, inputLayoutCellPhone, inputLayoutAddress;
    TextInputEditText name, lastName, userName, cellPhone, address;
    private boolean alerts, notifications, needHelp, isActive, timeConfiguration, isOk;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        mAuth = FirebaseAuth.getInstance();
        // Input y editText de Nombre
        inputLayoutName = findViewById(R.id.LayoutName);
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
        needHelp = false;
        isActive = true;
        timeConfiguration = true;
        isOk = true;
        //images
        iconimgSelectImage = findViewById(R.id.imgFromGallery);
        imgSelectImage = findViewById(R.id.imgBigFoto);
        iconDeleteImage = findViewById(R.id.imgDeleteFoto);
        iconDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
        iconimgSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    /*
        Metodo que se encarga de extraer los datos de la vista sign_up y enviarlos a Firebase.
     */
    public void nextStepToRegisterUser() {
        uploadTheSelectedImageTotheServer();
        if (validateInputs() != false) {
            Intent intent = new Intent(this, PasswordValidation.class);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("lastName", lastName.getText().toString());
            intent.putExtra("userName", userName.getText().toString());
            intent.putExtra("cellPhone", cellPhone.getText().toString());
            intent.putExtra("address", address.getText().toString());
            intent.putExtra("profileImage", uploadedImageLink);
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
            Uri chosenImageData = data.getData();
            configImageView(data.getDataString());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImageData);
                imgSelectImage.setImageBitmap(bitmap);


            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    private void uploadTheSelectedImageTotheServer() {
        // Get the data from an ImageView as bytes
        if (bitmap != null) {

            imgSelectImage.setDrawingCacheEnabled(true);
            imgSelectImage.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            imageIdentifier = UUID.randomUUID().toString() + ".png";


            final UploadTask uploadTask = FirebaseStorage.getInstance().getReference().
                    child("myImages").
                    child(imageIdentifier).putBytes(data);
            uploadedImageLink = imageIdentifier;

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            uploadedImageLink = task.getResult().toString();

                        }
                    });


                }
            });
        }
    }


    private void deleteImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Eliminar Imagen")
                .setMessage(R.string.detalle_dialogDelete_message)
                .setPositiveButton(R.string.label_dialog_delete, (dialogInterface, i) -> configImageView(null))
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.show();
    }

    private void configImageView(String fotoUrl) {
        if (fotoUrl != null) {
        } else {
            imgSelectImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person));
        }
    }
}
