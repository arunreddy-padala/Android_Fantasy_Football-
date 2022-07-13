package edu.neu.cs5520sp22.fantasyleaguecompanion.Notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.neu.cs5520sp22.fantasyleaguecompanion.R;


public class NotifyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyUser")
                .setSmallIcon(R.drawable.message1_foreground)
                .setContentTitle("Fantasy Football League")
                .setContentText("Reminder to set your match day lineup!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());



    }
}
