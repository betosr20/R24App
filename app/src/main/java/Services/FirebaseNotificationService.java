package Services;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private UserService userService;

    public FirebaseNotificationService() {
        userService = new UserService();
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    public void subscribeToIncidentNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseClasses.IncidentReportTopic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String v = "kjgdkfjgh";
                            System.out.println(v);
                        }
                    }
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String reportId = remoteMessage.getData().get("reportId");
            String ownerId = remoteMessage.getData().get("ownerId");
            showIncidentNotification(reportId, ownerId);
        }
    }

    private void showIncidentNotification(String reportId, String ownerId) {
        String currentUserId = userService.getCurrentFirebaseUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.Report).child(reportId);
        Context context = this;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Report report = dataSnapshot.getValue(Report.class);

                if (!report.getOwnerId().equals(currentUserId)) {
                    NotificationHandler notificationHandler = new NotificationHandler(context);
                    Notification.Builder notification = new NotificationHandler(context).createReportNotification(report);
                    notificationHandler.getManager().notify(1, notification.build());
                    notificationHandler.publishNotificationSummaryGroup();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
