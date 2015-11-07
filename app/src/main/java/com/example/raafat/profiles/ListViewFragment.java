package com.example.raafat.profiles;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.raafat.data.Profile;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.util.List;

/**
 * Created by Raafat on 12/09/2015.
 */
public class ListViewFragment extends ListFragment {


    public List<Profile> data;
    public ProfilesArrayAdater adapter;

    public static SwipeToDismissTouchListener<ListViewAdapter> swipeListner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        data = Profile.getAll(MyApplication.db);
        adapter = new ProfilesArrayAdater(getActivity(), R.layout.list_row_layout, data);

        this.setListAdapter(adapter);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onResume() {
        super.onResume();
//        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                final Profile profile = data.get(position);
//                new AlertDialog.Builder(getActivity())
//                        .setMessage("Do you really want to delete profile " + data.get(position).getName() + " ?")
//                        .setTitle("Delete Confirm ")
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                profile.delete(MyApplication.db);
//                                data.clear();
//                                data.addAll(Profile.getAll(MyApplication.db));
//                                adapter.notifyDataSetChanged();
//                            }
//                        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//
//
//                return true;
//            }
//        });

        this.getListView().setDividerHeight(3);
        this.getListView().setFooterDividersEnabled(true);
        this.getListView().setHeaderDividersEnabled(true);

        swipeListner = new SwipeToDismissTouchListener<>(new ListViewAdapter(ListViewFragment.this.getListView()),
                new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListViewAdapter recyclerView, int position) {

                        adapter.getItem(position).delete(MyApplication.db);
                        adapter.remove(adapter.getItem(position));
                        Configuration cfg = new Configuration.Builder()
                                .setBackgroundColor("#a42121")
                                .setDispalyDuration(1500)
                                .setTextColor("#ffffff").build();
                        NiftyNotificationView.build(getActivity()
                                , "Deleted"
                                , Effects.flip, 0, cfg)
                                .show();
                    }
                });


        this.getListView().setOnTouchListener(swipeListner);
        this.getListView().setOnScrollListener(
                (AbsListView.OnScrollListener) swipeListner.makeScrollListener());
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!swipeListner.existPendingDismisses()) {
                    // disable this profile
                    data.get(position).setEnabled(!data.get(position).isEnabled());
                    ContentValues cv = new ContentValues();
                    cv.put(Profile.COL_IS_ENABLED,data.get(position).isEnabled());

                    MyApplication.db.update(Profile.TABLE_NAME,cv,Profile.COL_ID+"=?",
                            new String[]{String.valueOf(data.get(position).getId())});

                    adapter.notifyDataSetChanged();
                    String text = (data.get(position).getName());
                    text += (data.get(position).isEnabled()) ? " Enabled " : " Disabled ";
                    if (!data.get(position).isEnabled())
                        data.get(position).unRigsterProfile();
                    else
                        data.get(position).registerProfileByTime();

                    Configuration cfg;
                    if (!data.get(position).isEnabled())
                        cfg = new Configuration.Builder()
                                .setBackgroundColor("#a42121")
                                .setDispalyDuration(1500)
                                .setAnimDuration(1500)
                                .setTextColor("#ffffff").build();

                    else
                        cfg = new Configuration.Builder()
                                .setBackgroundColor("#12d131")
                                .setDispalyDuration(1500)
                                .setAnimDuration(1500)
                                .setTextColor("#ffffff").build();


                    NiftyNotificationView.build(getActivity()
                            , text
                            , Effects.slideOnTop, 0, cfg)
                            .show();
                }
            }

        });
        adapter.notifyDataSetChanged();

    }


}
