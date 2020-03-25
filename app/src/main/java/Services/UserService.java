package Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserService {
    private FirebaseAuth mAuth;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
    }

    public String getCurrentFirebaseUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        return firebaseUser != null ? firebaseUser.getUid() : "";
    }
}
