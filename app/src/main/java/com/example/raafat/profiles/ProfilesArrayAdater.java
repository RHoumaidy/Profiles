package com.example.raafat.profiles;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raafat.data.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 12/09/2015.
 */
public class ProfilesArrayAdater extends ArrayAdapter<Profile> {

    private Context ctx;
    private LayoutInflater inflater;
    private List<Profile>data;
    private int res;

    public ProfilesArrayAdater(Context context, int resource, List<Profile> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
        this.data = objects;
        this.res = resource;
    }

    @Override
    public Profile getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public int getPosition(Profile item) {
       return this.data.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int finPos = position;
        if(convertView == null){
            convertView = this.inflater.inflate(this.res,parent,false);
        }

        Profile profile = data.get(position);

        CheckedTextView checkedTextView = (CheckedTextView)convertView.findViewById(R.id.enabledCkBx);
        TextView profileTile = (TextView)convertView.findViewById(R.id.profileTitle);
        TextView profileSubTitle = (TextView)convertView.findViewById(R.id.profileSubTitle);

        Button undoBtn = (Button)convertView.findViewById(R.id.undoBtn);

        checkedTextView.setChecked(profile.isEnabled());
        profileTile.setText(profile.getName());
        profileSubTitle.setText(profile.getSubTitle());

        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ListViewFragment.swipeListner.existPendingDismisses()) {
                    ListViewFragment.swipeListner.undoPendingDismiss();

                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }
}
