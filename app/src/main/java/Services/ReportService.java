package Services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

public class ReportService {
    private FirebaseDatabase database;

    public ReportService() {
        database = FirebaseDatabase.getInstance();
    }

    public void addNewReport(Report report) {
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.Report).child(report.getId());
        databaseReference.setValue(report);
        databaseReference.setValue(report);
    }
}