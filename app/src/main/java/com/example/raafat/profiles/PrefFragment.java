

package com.example.raafat.profiles;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raafat.data.Profile;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PrefFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {

    public Profile profile;
    private Menu myOptionMenu;
    private boolean isNameSet;
    private boolean isRepeatingSet;
    private boolean isActivatoinSet;

    public boolean isReady = false;

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private ListPreference activationModeLP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.new_profile_activity);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isActivatoinSet = false;
        isRepeatingSet = false;
        isRepeatingSet = false;
        isRepeatingSet = false;
        profile = new Profile();
        myOptionMenu = ((MainActivity) getActivity()).myOptionMenu;
        isReady  = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getPreferenceScreen().getSharedPreferences().edit().clear().apply();
        isReady  = false;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().edit().clear().apply();
    }

    @Override
    public void onResume() {
        super.onResume();

        findPreference(MyApplication.AUTO_ACTIVATION_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.CHANGE_NOTIFICATION_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.CHANGE_RINGTONE_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.REPEATIN_DAYS_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.CHANGE_WIFI_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.CHOOSE_NOTIFICATION_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.CHOOSE_RINGTONE_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.PROFILE_NAME_KEY).setOnPreferenceChangeListener(this);
        findPreference(MyApplication.RING_MODE_KEY).setOnPreferenceChangeListener(this);


        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        updatePrefences(findPreference(MyApplication.CHANGE_NOTIFICATION_KEY));
        updatePrefences(findPreference(MyApplication.CHANGE_RINGTONE_KEY));
        updatePrefences(findPreference(MyApplication.REPEATIN_DAYS_KEY));
        updatePrefences(findPreference(MyApplication.CHANGE_WIFI_KEY));
        updatePrefences(findPreference(MyApplication.CHOOSE_NOTIFICATION_KEY));
        updatePrefences(findPreference(MyApplication.CHOOSE_RINGTONE_KEY));
        updatePrefences(findPreference(MyApplication.PROFILE_NAME_KEY));
        updatePrefences(findPreference(MyApplication.RING_MODE_KEY));


    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    private String getRingToneURI() {
        return getPreferenceScreen()
                .getSharedPreferences()
                .getString(MyApplication.CHOOSE_RINGTONE_KEY,
                        Settings.System.DEFAULT_RINGTONE_URI.toString());
    }

    private String getNotiToneURI() {
        return getPreferenceScreen()
                .getSharedPreferences()
                .getString(MyApplication.CHOOSE_NOTIFICATION_KEY,
                        Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }

    private void showFromDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setWindowAnimations(R.style.PauseDialogAnimation);
        dialog.setTitle(R.string.time_dialog_from);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_time_layout);

        Button okButton = (Button) dialog.findViewById(R.id.dialogTimeOkButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogTimeCancelButton);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                profile.setFromTime(calendar.getTimeInMillis());
                dialog.dismiss();
                showToDialog();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showToDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setWindowAnimations(R.style.PauseDialogAnimation);
        dialog.setTitle(R.string.time_dialog_to);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_time_layout);

        Button okButton = (Button) dialog.findViewById(R.id.dialogTimeOkButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialogTimeCancelButton);

        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                profile.setToTime(calendar.getTimeInMillis());
                if (profile.validateTimes()) {
                    dialog.dismiss();
                    isActivatoinSet = true;
                    isReady = isNameSet && isRepeatingSet && isActivatoinSet;

                    profile.setSubTitle("By Time : " + MyApplication.sdf.format(profile.getFromTime()) + " - " +
                            MyApplication.sdf.format(profile.getToTime()));
                    findPreference(MyApplication.AUTO_ACTIVATION_KEY).setSummary(profile.getSubTitle());
                }
                else {
                    // Toast.makeText(getActivity(), getString(R.string.toast_invalid_time_msg), Toast.LENGTH_SHORT).show();
                    Configuration cfg = new Configuration.Builder()
                            .setBackgroundColor("#a42121")
                            .setDispalyDuration(2000)
                            .setAnimDuration(1500)
                            .setTextColor("#ffffff").build();
                    NiftyNotificationView.build(getActivity()
                            , getString(R.string.toast_invalid_time_msg)
                            , Effects.slideIn, 0, cfg)
                            .show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updatePrefences(Preference preference) {
        if (preference instanceof EditTextPreference) {
            if (preference.getKey().equals(MyApplication.PROFILE_NAME_KEY)) {

                EditTextPreference etp = (EditTextPreference) preference;
                if(etp.getText()!=null && !etp.getText().equals("")) {
                    profile.setName(etp.getText());
                    isNameSet = true;
                }
                isReady = isNameSet && isRepeatingSet && isActivatoinSet;
                etp.setSummary(etp.getText());

            }
        } else if (preference instanceof ListPreference) {
            ListPreference lp = (ListPreference)preference;
            if (preference.getKey().equals(MyApplication.AUTO_ACTIVATION_KEY)) {

                lp.setSummary(lp.getEntry());
                int idx = Integer.parseInt(lp.getValue());
                switch (idx) {
                    case 0:
                        break;
                    case 1:
                        showFromDialog();
                        profile.setAcivationMode(MyApplication.BY_TIME);

                        //lp.setSummary(profile.getSubTitle());
                        break;
                    case 2:
                        profile.setAcivationMode(MyApplication.BY_LOCATOIN);
                        profile.setSubTitle("By Location ");

                        isActivatoinSet = true;
                        isReady = isNameSet && isRepeatingSet && isActivatoinSet;
                        break;
                }
                lp.setSummary(profile.getSubTitle());

            } else if (preference.getKey().equals(MyApplication.RING_MODE_KEY)) {

                lp.setSummary(lp.getEntry());
                profile.setRingerMode(Integer.parseInt(lp.getValue()));

            } else if (preference.getKey().equals(MyApplication.CHANGE_WIFI_KEY)) {

                lp.setSummary(lp.getEntry());
                profile.setWifiMode(Integer.parseInt(lp.getValue()));

            }else if(preference.getKey().equals(MyApplication.CHANGE_BLUETOOEH_KEY)){

                lp.setSummary(lp.getEntry());
                profile.setBlueToothMode(Integer.parseInt(lp.getValue()));
            }


        } else if (preference instanceof MultiSelectListPreference) {
            if (preference.getKey().equals(MyApplication.REPEATIN_DAYS_KEY)) {

                MultiSelectListPreference msp = (MultiSelectListPreference) preference;
                Set<String> selectedDays = msp.getValues();
                ArrayList<Integer> selectedIndx = new ArrayList<>();

                for (String day : selectedDays) {
                    selectedIndx.add(Integer.parseInt(day));
                   // Toast.makeText(MyApplication.APP_CNTX,day,Toast.LENGTH_SHORT).show();
                }
                Collections.sort(selectedIndx);
                String summary = "";
                for (int idx : selectedIndx) {
                    summary += MyApplication.DAYS_OF_WEEK[idx%7].substring(0, 3) + ",";
                }

                if (summary.length() == 0)
                    summary = "NONE";

                msp.setSummary(summary);
                profile.setRepeatingDays(selectedIndx);
                isRepeatingSet = true;
                isReady = isNameSet && isRepeatingSet && isActivatoinSet;

            }
        } else if (preference instanceof CheckBoxPreference) {
            CheckBoxPreference cbp = (CheckBoxPreference) preference;
            if (cbp.getKey().equals(MyApplication.CHANGE_RINGTONE_KEY)) {

                findPreference(MyApplication.CHOOSE_RINGTONE_KEY).setEnabled(cbp.isChecked());
                profile.setChangeRingtone(cbp.isChecked());

            } else if (cbp.getKey().equals(MyApplication.CHANGE_NOTIFICATION_KEY)) {
                findPreference(MyApplication.CHOOSE_NOTIFICATION_KEY).setEnabled(cbp.isChecked());
                profile.setChangeNotiTone(cbp.isChecked());

            }
        } else if (preference instanceof RingtonePreference) {
            RingtonePreference rtp = (RingtonePreference) preference;
            if (rtp.getKey().equals(MyApplication.CHOOSE_RINGTONE_KEY)) {
                String ring = getRingToneURI();
                Uri uri = Uri.parse(ring);
                Ringtone ringtone = RingtoneManager.getRingtone(MyApplication.APP_CNTX, uri);
                if (ringtone != null)
                    rtp.setSummary(ringtone.getTitle(getActivity()));

                profile.setRingeToneURI(ring);

            } else if (rtp.getKey().equals(MyApplication.CHOOSE_NOTIFICATION_KEY)) {
                String ring = getNotiToneURI();
                Uri uri = Uri.parse(ring);
                Ringtone ringtone = RingtoneManager.getRingtone(MyApplication.APP_CNTX, uri);
                if(ringtone != null)
                rtp.setSummary(ringtone.getTitle(getActivity()));

                profile.setNotiToneURI(ring);

            }
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        updatePrefences(preference);

        isReady = isNameSet && isRepeatingSet && isActivatoinSet;
     //   myOptionMenu.findItem(R.id.action_ok).setEnabled(isNameSet && isRepeatingSet && isActivatoinSet);


        return true;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePrefences(findPreference(key));
        isReady = isNameSet && isRepeatingSet && isActivatoinSet;
    }
}