package Services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Models.POJOS.User;

public class UserService {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    public boolean addNewUser(User user) {
        final boolean[] successFulRegister = {true};

        DatabaseReference databaseReference = database.getReference(FirebaseClasses.User).child(String.valueOf(user.getId()));
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
}
