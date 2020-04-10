package Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.r24app.MainActivity;
import com.example.r24app.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import Activities.MapActivity;
import Activities.ReportIncidentActivity;
import Models.Constants.DataConstants;
import Models.Constants.FirebaseClasses;
import Models.POJOS.Report;
import Models.POJOS.User;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Context context = this;
    public static final String TAG = "NOTICIAS";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onNewToken(String s) {

        super.onNewToken(s);

        String token = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "ESTOY EN EL HPTA ONRECEIVE!!!!!!! BIEN PERO MAL");
            if(remoteMessage.getData().size()>0){
                Log.d(TAG, "DATA: " + remoteMessage.getData().get("incidentID"));
                showIncidentNotification(remoteMessage.getData().get("incidentID"));
            }
    }

    private void showIncidentNotification(String tittle){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseClasses.Report).child(tittle);
        Context context = this;
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Report report = dataSnapshot.getValue(Report.class);
                NotificationHandler notificationHandler = new NotificationHandler(context);
                Notification.Builder notification = new NotificationHandler(context).createReportNotification(report);
                notificationHandler.getManager().notify(1, notification.build());
                notificationHandler.publishNotificationSummaryGroup();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



}
