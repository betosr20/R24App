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
import Models.POJOS.Report;

public class ReportService {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public ReportService() {
        database = FirebaseDatabase.getInstance();
    }

    public void addNewReport(Report report) {
        databaseReference = database.getReference(FirebaseClasses.Report).child(report.getId());
        databaseReference.setValue(report);
        databaseReference.setValue(report);
    }

    public ArrayList<NaturalDisaster> getDisasterTypes() {
        final ArrayList<NaturalDisaster> disasterTypes = new ArrayList<>();

        databaseReference = database.getReference().child(FirebaseClasses.NaturalDisaster);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    disasterTypes.add(data.getValue(NaturalDisaster.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return disasterTypes;
    }
}