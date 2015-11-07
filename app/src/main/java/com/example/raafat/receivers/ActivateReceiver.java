package com.example.raafat.receivers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.raafat.profiles.MyApplication;
import com.example.raafat.data.Profile;
import com.example.raafat.profiles.R;

import java.util.Calendar;

/**
 * Created by Raafat on 13/09/2015.
 */
public class ActivateReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        String profileName = intent.getStringExtra(MyApplication.APP_CNTX.getString(R.string.profile_name_intent_extra));
        String ringToneUri = intent.getStringExtra(MyApplication.APP_CNTX.getString(R.string.ring_tone_intent_extra));
        String notiToneUri = intent.getStringExtra(MyApplication.APP_CNTX.getString(R.string.noti_tone_intent_extra));
        int ringerMode = intent.getIntExtra(MyApplication.APP_CNTX.getString(R.string.ringer_mode_intent_extra), 0);
        boolean changeRintTone = intent.getBooleanExtra(MyApplication.APP_CNTX.getString(R.string.change_ring_tone_intent_extra), false);
        boolean changeNotiTone = intent.getBooleanExtra(MyApplication.APP_CNTX.getString(R.string.change_noti_tone_intent_extra), false);
        int activationMOde = intent.getIntExtra(MyApplication.APP_CNTX.getString(R.string.activation_mode_intent_extra), 0);
        long toTime = intent.getLongExtra(MyApplication.APP_CNTX.getString(R.string.to_time_intent_extra), 0);
        long id = intent.getLongExtra(MyApplication.APP_CNTX.getString(R.string.curr_id_intent_extras), 0);
        int wifiMode = intent.getIntExtra(MyApplication.APP_CNTX.getString(R.string.wifi_mode_intent_extra), 0);
        int blueToothMode = intent.getIntExtra(MyApplication.APP_CNTX.getString(R.string.bluetooth_mode_intent_extra), 0);

        int currDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        Profile prevProfile = new Profile();
        prevProfile.setId(id);
        prevProfile.setName(profileName);
        //
        prevProfile.setFromTime(Calendar.getInstance().getTimeInMillis()+1*90*1000);
        prevProfile.setToTime(0);
        prevProfile.setAcivationMode(activationMOde);
        prevProfile.setChangeNotiTone(changeNotiTone);
        prevProfile.setChangeRingtone(changeNotiTone);
        prevProfile.setNotiToneURI(
                RingtoneManager.getActualDefaultRingtoneUri(MyApplication.APP_CNTX, RingtoneManager.TYPE_NOTIFICATION).toString());
        prevProfile.setRingeToneURI(
                RingtoneManager.getActualDefaultRingtoneUri(MyApplication.APP_CNTX, RingtoneManager.TYPE_RINGTONE).toString());

        int currMode = MyApplication.audioManager.getRingerMode();

        boolean currWMode = MyApplication.wifiManager.isWifiEnabled();
        boolean currBMode = false;
        if (MyApplication.bluetoothAdapter != null)
            currBMode = MyApplication.bluetoothAdapter.isEnabled();

        switch (blueToothMode) {
            case MyApplication.ON_WIFI_MODE:
                MyApplication.bluetoothAdapter.enable();
                prevProfile.setBlueToothMode(currBMode ? MyApplication.ON_WIFI_MODE : MyApplication.OFF_WIFI_MODE);
                break;
            case MyApplication.OFF_WIFI_MODE:
                MyApplication.bluetoothAdapter.disable();
                prevProfile.setBlueToothMode(currBMode ? MyApplication.ON_WIFI_MODE : MyApplication.OFF_WIFI_MODE);
                break;
            case MyApplication.TOGLE_WIFI_MODE:
                if (currBMode)
                    MyApplication.bluetoothAdapter.disable();
                else
                    MyApplication.bluetoothAdapter.enable();
                prevProfile.setBlueToothMode(MyApplication.TOGLE_WIFI_MODE);
                break;
            default:
                break;
        }

        switch (wifiMode) {
            case MyApplication.ON_WIFI_MODE:
                MyApplication.wifiManager.setWifiEnabled(true);
                prevProfile.setWifiMode(currWMode ? MyApplication.ON_WIFI_MODE : MyApplication.OFF_WIFI_MODE);
                break;
            case MyApplication.OFF_WIFI_MODE:
                MyApplication.wifiManager.setWifiEnabled(false);
                prevProfile.setWifiMode(currWMode ? MyApplication.ON_WIFI_MODE : MyApplication.OFF_WIFI_MODE);
                break;
            case MyApplication.TOGLE_WIFI_MODE:
                MyApplication.wifiManager.setWifiEnabled(!currWMode);
                prevProfile.setWifiMode(MyApplication.TOGLE_WIFI_MODE);
                break;
            default:
                break;

        }

        switch (currMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                prevProfile.setRingerMode(MyApplication.RING_VIBERATE_RINGER_MODE);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                prevProfile.setRingerMode(MyApplication.SILANCE_RINGER_MODE);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                prevProfile.setRingerMode(MyApplication.VIBERATE_RINGER_MODE);
                break;
        }

        if (changeRintTone) {

            prevProfile.setNotiToneURI(Settings.System.DEFAULT_RINGTONE_URI.getPath());
            Uri uri = Uri.parse(ringToneUri);
            RingtoneManager.setActualDefaultRingtoneUri(MyApplication.APP_CNTX,
                    RingtoneManager.TYPE_RINGTONE, uri);
        }
        if (changeNotiTone) {
            prevProfile.setNotiToneURI(Settings.System.DEFAULT_NOTIFICATION_URI.getPath());
            Uri uri = Uri.parse(notiToneUri);
            RingtoneManager.setActualDefaultRingtoneUri(MyApplication.APP_CNTX,
                    RingtoneManager.TYPE_NOTIFICATION, uri);
        }

        switch (ringerMode) {
            case MyApplication.CURRENT_RINGER_MODE:
                prevProfile.setRingerMode(MyApplication.CURRENT_RINGER_MODE);
                break;
            case MyApplication.RING_VIBERATE_RINGER_MODE:
                MyApplication.audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
            case MyApplication.VIBERATE_RINGER_MODE:
                MyApplication.audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case MyApplication.SILANCE_RINGER_MODE:
                MyApplication.audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
        }


        Intent intent1 = new Intent(context, NotificationService.class);
        intent1.putExtra("ACTIVATE", false);
        if (toTime != 0) {
            prevProfile.registerPrevProfile(currDay);
            intent1.putExtra("ACTIVATE", true);
        }


        intent1.putExtra(context.getString(R.string.profile_name_intent_extra), profileName);
        startWakefulService(context, intent1);
        setResultCode(Activity.RESULT_OK);

    }
}
