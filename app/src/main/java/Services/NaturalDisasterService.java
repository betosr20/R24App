package Services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.Constants.FirebaseClasses;
import Models.POJOS.NaturalDisaster;

public class NaturalDisasterService {
    private FirebaseDatabase database;

    public NaturalDisasterService() {
        database = FirebaseDatabase.getInstance();
    }

    public void getDisasterTypes(final ArrayList<String> disasterTypeList) {
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.NaturalDisaster);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NaturalDisaster naturalDisaster;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        naturalDisaster = snapshot.getValue(NaturalDisaster.class);
                        disasterTypeList.add(naturalDisaster.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
