package Services;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Models.POJOS.ReportPicture;

public class ReportService {
    private FirebaseDatabase database;
    private FirebaseStorage firebaseStorage;
    private StorageReference firebaseStorageReference;
    private DatabaseReference databaseReference;

    public ReportService() {
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public boolean addNewReport(Report report) {
        final boolean[] successFulRegister = {true};
        databaseReference = database.getReference(FirebaseClasses.Report).child(String.valueOf(report.getId()));

        databaseReference.setValue(report)
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

    public boolean saveReportImages(ArrayList<Uri> uris, final String reportId) {
        firebaseStorageReference = firebaseStorage.getReference();
        final boolean[] successfulUpload = {true};
        int imagesCounter = 1;

        if (uris != null && !uris.isEmpty()) {
            for (Uri uri : uris) {
                final String imageName = reportId + "_" + imagesCounter;
                UploadTask uploadTask = firebaseStorageReference.child("ReportsImages/" + imageName + ".jpg").putFile(uri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        successfulUpload[0] = false;
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        saveReportPictureDownloadReference(imageName, reportId);
                    }
                });

                imagesCounter++;
            }
        }

        return successfulUpload[0];
    }

    public void saveReportPictureDownloadReference(String imageName, String reportId) {
        ReportPicture reportPicture = new ReportPicture(imageName + ".jpg", reportId, imageName);
        databaseReference = database.getReference(FirebaseClasses.ReportPicture).child(reportPicture.getId());
        databaseReference.setValue(reportPicture);
    }
}