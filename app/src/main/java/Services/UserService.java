package Services;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
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

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }

    public void updatePinSetting(boolean setting){
        String id = getCurrentFirebaseUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseClasses.User).child(id).child("picker")
                .setValue(setting);
    }

    public void updateheatMapSetting(boolean setting){
        String id = getCurrentFirebaseUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseClasses.User).child(id).child("hotMap")
                .setValue(setting);
    }

    public void updateViewTypeSetting(boolean setting){
        String id = getCurrentFirebaseUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseClasses.User).child(id).child("viewType")
                .setValue(setting);
    }

}
