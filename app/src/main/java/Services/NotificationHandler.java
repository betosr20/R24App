package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.example.r24app.R;

import Activities.ReportDetail.ReportDetailContainer;
import Activities.ReportIncidentActivity;
import Models.Constants.DataConstants;
import Models.POJOS.DistressSignal;
import Models.POJOS.Report;

public class NotificationHandler extends ContextWrapper {
    private NotificationManager notificationManager;

    public NotificationHandler(Context context) {
        super(context);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel highChannel = new NotificationChannel(DataConstants.CHANNEL_HIGH_ID, DataConstants.CHANNEL_HIGH_NAME, NotificationManager.IMPORTANCE_HIGH);
            highChannel.setShowBadge(true);
            highChannel.enableVibration(true);
            highChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(highChannel);
        }
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    private Notification.Builder createNotificationWithChannel(Report report) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(this, ReportDetailContainer.class);
            String id = report.getId();
            intent.putExtra("idReport", id);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            return new Notification.Builder(getApplicationContext(), DataConstants.CHANNEL_HIGH_ID)
                    .setContentTitle(getResources().getString(R.string.reportIncidentNotification))
                    .setContentText(report.getType() + " en " + report.getPlace())
                    .setSmallIcon(R.drawable.uptodatelogo)
                    .setGroup(DataConstants.SUMMARY_GROUP_NAME)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.uptodatelogo))
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);
        }

        return null;
    }

    public Notification.Builder createNotificationWithoutChannel(Report report) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle(getResources().getString(R.string.reportIncidentNotification))
                .setContentText(report.getType() + " en " + report.getPlace())
                .setSmallIcon(R.drawable.uptodatelogo)
                .setVibrate(new long[] { 2000, 2000, 2000, 2000, 2000 })
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.uptodatelogo))
                .setAutoCancel(true);
    }

    public Notification.Builder createReportNotification(Report report) {
        if (Build.VERSION.SDK_INT >= 26) {
            return createNotificationWithChannel(report);
        } else {
            return createNotificationWithoutChannel(report);
        }
    }

    public void publishNotificationSummaryGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification summaryNotification = new Notification.Builder(getApplicationContext(), DataConstants.CHANNEL_HIGH_ID)
                    .setSmallIcon(R.drawable.ic_report_black_24dp)
                    .setGroup(DataConstants.SUMMARY_GROUP_NAME)
                    .setGroupSummary(true)
                    .build();
            getManager().notify(DataConstants.SUMMARY_GROUP_ID, summaryNotification);
        }
    }

    private Notification.Builder createDistressWithChannel(DistressSignal distress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            return new Notification.Builder(getApplicationContext(), DataConstants.CHANNEL_HIGH_ID)
                    .setContentTitle("Nueva alerta de auxilio ")
                    .setContentText(distress.getName() +  " " + distress.getLastName() + " ha emitido una señal de auxilio" +
                            " en " + distress.getLocationPlace())
                    .setSmallIcon(R.drawable.uptodatelogo)
                    .setGroup(DataConstants.SUMMARY_GROUP_NAME)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.uptodatelogo))
                    .setAutoCancel(true);
        }

        return null;
    }

    public Notification.Builder createDistressWithoutChannel(DistressSignal distress) {
        return new Notification.Builder(getApplicationContext())
                .setContentTitle("Nueva alerta de auxilio")
                .setContentText(distress.getName() +  " " + distress.getLastName() + " ha emitido una señal de auxilio" +
                        " en " + distress.getLocationPlace())
                .setSmallIcon(R.drawable.uptodatelogo)
                .setVibrate(new long[] { 2000, 2000, 2000, 2000, 2000 })
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.uptodatelogo))
                .setAutoCancel(true);
    }

    public Notification.Builder createDistressNotification(DistressSignal distress) {
        if (Build.VERSION.SDK_INT >= 26) {
            return createDistressWithChannel(distress);
        } else {
            return createDistressWithoutChannel(distress);
        }
    }
}
