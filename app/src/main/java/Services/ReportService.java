package Services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Repo;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

public class ReportService {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public ReportService() {
        database = FirebaseDatabase.getInstance();
    }

    public void addNewReport(Report report) {
        databaseReference =  database.getReference(FirebaseClasses.Report).child(report.getId());
        databaseReference.setValue(report);
    }
}
