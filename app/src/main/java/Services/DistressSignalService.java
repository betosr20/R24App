package Services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.Constants.FirebaseClasses;
import Models.POJOS.DistressSignal;

public class DistressSignalService {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public DistressSignalService() {
        database = FirebaseDatabase.getInstance();

    }

    public boolean createDistressReport(DistressSignal distressCall){
        final boolean[] successFulRegister = {true};

        databaseReference = database.getReference(FirebaseClasses.DistressSignal).child(distressCall.getId());
        databaseReference.setValue(distressCall)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserService userService = new UserService();
                        userService.updateNeedHelp(true);
                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                successFulRegister[0] = false;
            }
        });

        return successFulRegister[0];
    }

    public boolean deleteDistressReport(String id){

        final boolean[] successFulRegister = {true};

        databaseReference = database.getReference(FirebaseClasses.DistressSignal).child(id);
        databaseReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserService userService = new UserService();
                        userService.updateNeedHelp(false);
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
