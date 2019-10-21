package com.example.myexpense;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class NotifyService extends Service {

    private DataBaseHelper dataBaseHelper;
    public NotifyService(DataBaseHelper dataBaseHelper)
    {
        this.dataBaseHelper=dataBaseHelper;
    }
    public static final String CHANNEL_ID = "notification_channel";
    public static final int NOTIFICATION_ID = 001;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("Testing Notify Service Class!");
        displayNotification(this);
    }

    public void displayNotification(Context object) {
        createNotificationChannel(object);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(object, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.notification_icon);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String todaydate = df.format(c);
        builder.setContentTitle(todaydate);
        builder.setContentText("Expenditure = Rs "+getString(todaydate));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(object);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private String getString(String todaydate)
    {
        DataBaseHelper.setDay_exp_total(0);
        DataBaseHelper.getExpenditureFromDate(dataBaseHelper.getReadableDatabase(),todaydate);
        if(DataBaseHelper.getTotalExpenses(dataBaseHelper.getReadableDatabase())==0)
            return "Empty";
        else
            return  String.valueOf(DataBaseHelper.getTotalExpenses(dataBaseHelper.getReadableDatabase()));
    }

    private static void createNotificationChannel(Context object) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) ;
        {
            CharSequence name = "notification_channel";
            String description = "include all the personal notifcations";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) object.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}