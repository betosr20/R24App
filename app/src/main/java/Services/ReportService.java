package Services;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.r24app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.MethodHandle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Activities.ReportIncidentActivity;
import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Models.POJOS.ReportPicture;

public class ReportService {
    private FirebaseDatabase database;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    public static final String TAG = "NOTICIAS";

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
        StorageReference firebaseStorageReference = firebaseStorage.getReference();
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

    public void sendNewReportNotification(Report report, Context context) {
        NotificationHandler notificationHandler = new NotificationHandler(context);
        Notification.Builder notification = new NotificationHandler(context).createReportNotification(report);
        notificationHandler.getManager().notify(1, notification.build());
        notificationHandler.publishNotificationSummaryGroup();
    }

    public void sendNotificationToFirebase(Context context, Report report) throws JSONException {
        RequestQueue mRequestQueue;

        mRequestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        String URL = "https://fcm.googleapis.com/fcm/send";
        JSONObject Data = new JSONObject();
        Data.put("incidentID", report.getId());
        mainObj.put("to", "/topics/" + "newIncident");
        JSONObject notificationObj = new JSONObject();
        notificationObj.put("title", "Nuevo reporte de incidente");
        notificationObj.put("body", report.getType() + " en " + report.getPlace());
        mainObj.put("notification", notificationObj);
        mainObj.put("data", Data);
        Log.d("RESPUESTA DEL SERVER", (String.valueOf(mainObj)));
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.POST, URL,
                mainObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("content-type", "application/json");
                header.put("authorization", "key=AAAAf8ADE8E:APA91bHUICXCSCjQhcZ0_9Fe4m77DoluucwrviZYYGD49Khq28NrZylsK_mvPiHrvgvPTx1yn12HlLS3UoBLFNCdM7Hd6rXAyrei_Z5KmLrZIbbFNayOLjKL3L_xN3JILQM-VnwxPbEg");
                return header;
            }
        };
        mRequestQueue.add(request);
    }


}