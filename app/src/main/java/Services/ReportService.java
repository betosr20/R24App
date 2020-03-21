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
    }

    public void getDisasterTypes(final ArrayList<String> disasterTypeList) {
        databaseReference = database.getReference(FirebaseClasses.NaturalDisaster);
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