package Services;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class UserService {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private User currentUserInfo;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUserInfo = null;
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    public User getCurrentUserInfo() {
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.User).child(getCurrentFirebaseUserId());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserInfo = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return currentUserInfo;
    }
}
