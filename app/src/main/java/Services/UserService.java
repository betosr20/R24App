package Services;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class UserService {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageReference ref;
    private FirebaseStorage firebaseStorage;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    public boolean updateUser(User user) {
        final boolean[] successFulRegister = {true};

        databaseReference.child(FirebaseClasses.User).child(String.valueOf(user.getId())).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successFulRegister[0] = false;
            }
        });

        return successFulRegister[0];
    }

    public boolean addNewUser(User user) {
        final boolean[] successFulRegister = {true};

        databaseReference = database.getReference(FirebaseClasses.User).child(String.valueOf(user.getId()));
        databaseReference.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successFulRegister[0] = false;
            }
        });

        return successFulRegister[0];
    }

    public void uploadTheSelectedImageToServer(Bitmap bitmap, User user, boolean isSignup) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();

            final UploadTask uploadTask = FirebaseStorage.getInstance().getReference().
                    child("myImages").
                    child(getCurrentFirebaseUserId() + ".png").putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            user.setProfileImage(uri.toString());
                            if (isSignup) {
                                addNewUser(user);
                            } else {
                                updateUser(user);
                            }
                        }
                    });
                }
            });

        }
    }

    public void updatePinSetting(boolean setting) {
        String id = getCurrentFirebaseUserId();
        databaseReference.child(FirebaseClasses.User).child(id).child("picker").setValue(setting);
    }

    public void updateheatMapSetting(boolean setting) {
        String id = getCurrentFirebaseUserId();
        databaseReference.child(FirebaseClasses.User).child(id).child("heatMap").setValue(setting);
    }

    public void updateViewTypeSetting(boolean setting) {
        String id = getCurrentFirebaseUserId();
        databaseReference.child(FirebaseClasses.User).child(id).child("viewType").setValue(setting);
    }

    public void getImageProfileUri(User pupdateUser) {
        storageReference = firebaseStorage.getInstance().getReference();
        ref = storageReference.child("myImages/" + pupdateUser.getProfileImage());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                pupdateUser.setProfileImage(uri.toString());
                updateUser(pupdateUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void updateNeedHelp(boolean setting) {
        String id = getCurrentFirebaseUserId();
        databaseReference.child(FirebaseClasses.User).child(id).child("needHelp").setValue(setting);
    }
}
