package com.example.raafat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.raafat.data.Profile;
import com.example.raafat.profiles.MyApplication;

import java.util.List;

/**
 * Created by Raafat on 24/10/2015.
 */
public class BootCompletReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        // load every profile in the db and register it
        // if the current time is between the activation time and the disactivation time
        // activate this profile ...


        Toast.makeText(MyApplication.APP_CNTX,"This is Me !\n the Profile App :P\nAnd i have started :PP",Toast.LENGTH_LONG).show();

        List<Profile> profiles = Profile.getAll(MyApplication.db);

        for(Profile p : profiles){
            p.registerProfileByTime();
        }


    }
}
