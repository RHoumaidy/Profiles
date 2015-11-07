package com.example.raafat.receivers;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.example.raafat.profiles.MyApplication;
import com.example.raafat.profiles.R;

/**
 * Created by Raafat on 13/09/2015.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("AlarmService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        String profileName = intent.getStringExtra(getString(R.string.profile_name_intent_extra));
        boolean activate = intent.getBooleanExtra("ACTIVATE",false);
        String activated  = (activate)? "Activated  ":"DisActivated ";
        sentNotification(profileName,activated);

    }

    private void sentNotification(String st ,String st2){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setColor(Color.GREEN)
                .setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentText(st + " ")
                .setSubText(st2);

        int id = 0;

        MyApplication.notificationManager.notify(id,builder.build());

        PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);

        boolean isScreenOn = pm.isScreenOn();
        if(!isScreenOn){
            PowerManager.WakeLock w1 =
                    pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP,"myLock");
            w1.acquire(5000);
        }
    }
}
