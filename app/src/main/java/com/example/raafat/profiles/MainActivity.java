package com.example.raafat.profiles;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {

    //public static boolean preferenceChanged = false;
    public Menu myOptionMenu;
    private MenuItem okItme;

    private boolean backPressed = false;

    ListViewFragment listViewFragment = new ListViewFragment();
    PrefFragment prefFragment = new PrefFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, 0, 0)
                .replace(R.id.myContainer, listViewFragment)
                .addToBackStack(null)

                .commit();


       // listViewFragment.adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

        if(!backPressed) {
            backPressed = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 3500);
            //Toast.makeText(this, getString(R.string.toast_exit_msg), Toast.LENGTH_LONG).show();
            Configuration cfg = new Configuration.Builder()
                    .setBackgroundColor("#a42121")
                    .setDispalyDuration(2000)
                    .setAnimDuration(1500)
                    .setTextColor("#ffffff").build();
            NiftyNotificationView.build(MainActivity.this
                    ,getString(R.string.toast_exit_msg)
                    , Effects.slideIn,0,cfg)
                    .show();
        }else
            super.onBackPressed();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        myOptionMenu = menu;

        okItme = menu.findItem(R.id.action_ok);
        okItme.setVisible(false);
        menu.findItem(R.id.action_cancel).setVisible(false);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        switch (id){
            case R.id.action_settings:
                return true;

            case R.id.action_add_new:
                item.setVisible(false);
                myOptionMenu.findItem(R.id.action_ok).setVisible(true);
                myOptionMenu.findItem(R.id.action_cancel).setVisible(true);


                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, 0, 0)
                        .replace(R.id.myContainer, prefFragment)

                        .commit();

                return true;

            case R.id.action_ok:
                if(!prefFragment.isReady){
                    //Toast.makeText(this,getString(R.string.toast_file_all_msg),Toast.LENGTH_LONG).show();
                    Configuration cfg = new Configuration.Builder()
                            .setBackgroundColor("#a42121")
                            .setDispalyDuration(1500)
                            .setTextColor("#ffffff").build();
                    NiftyNotificationView.build(MainActivity.this
                            ,getString(R.string.toast_file_all_msg)
                            , Effects.flip,0,cfg)
                            .show();
                    return false;
                }
                myOptionMenu.findItem(R.id.action_add_new).setVisible(true);
                myOptionMenu.findItem(R.id.action_cancel).setVisible(false);
                item.setVisible(false);


                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, 0, 0)
                        .replace(R.id.myContainer, listViewFragment)
                        .commit();

                long profileId = prefFragment.profile.save(MyApplication.db);
                Toast.makeText(this,profileId+"",Toast.LENGTH_LONG).show();
                prefFragment.profile.registerProfileByTime();
                listViewFragment.data.add( prefFragment.profile);
                listViewFragment.adapter.notifyDataSetChanged();
                return true;

            case R.id.action_cancel:
                myOptionMenu.findItem(R.id.action_add_new).setVisible(true);
                myOptionMenu.findItem(R.id.action_ok).setVisible(false);
                item.setVisible(false);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, 0, 0)
                        .replace(R.id.myContainer, listViewFragment)
                        .commit();

        }

        return super.onOptionsItemSelected(item);
    }
}
