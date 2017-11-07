package com.example.edu.menutb.view.timeline;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.TimelineController;
import com.example.edu.menutb.model.service.RecyclerItemClickListener;
import com.example.edu.menutb.model.timeline.TimelineArrayAdapter;
import com.example.edu.menutb.model.timeline.TimelinePhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Edu on 18/07/2017.
 */

public class TimelineActivity extends Fragment {
    private ArrayList<TimelinePhoto> arrayListTimelinePhotos = new ArrayList<TimelinePhoto>();
    private RecyclerView recyclerView;
    private TimelineArrayAdapter timelineArrayAdapter;

    private String idString;
    private String tokenString;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.timeline_list, container, false);
        recyclerView = (RecyclerView) myView.findViewById(R.id.timeline_recycleview);
        recyclerView.setHasFixedSize(true);
        getActivity().setTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        timelineArrayAdapter = new TimelineArrayAdapter(arrayListTimelinePhotos, getContext());
        recyclerView.setAdapter(timelineArrayAdapter);
        loadSharedPreferences();
        new GetTimelineTask(myView.findViewById(R.id.relativeLayoutTimeLine)).execute();
        return myView;
    }

    private void loadSharedPreferences(){
        SharedPreferences idBusca = getContext().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = idBusca.getString("id", "");

        SharedPreferences token = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
    }

    private class GetTimelineTask extends AsyncTask<String, Void, ArrayList<TimelinePhoto>> {
        View rootView;
        GetTimelineTask(View view){
            this.rootView = view;
        }
        protected ArrayList<TimelinePhoto> doInBackground(String... timeline) {
            ArrayList<TimelinePhoto> timelinePhotoArrayList = new ArrayList<>();
            try {
                timelinePhotoArrayList = new TimelineController().getTimeline(idString, tokenString);
            } catch (Exception e){
                e.printStackTrace();
            }
            return timelinePhotoArrayList;
        }

        protected void onPostExecute(ArrayList<TimelinePhoto> timelinePhotos) {
            if(timelinePhotos.size() == 0){
                Snackbar.make(rootView, getString(R.string.noPhotoTimeline), Snackbar.LENGTH_LONG)
                        .setAction(getText(R.string.close), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
            }
            else{
                arrayListTimelinePhotos = timelinePhotos;
                timelineArrayAdapter = new TimelineArrayAdapter(arrayListTimelinePhotos, getContext(), idString, tokenString);
                recyclerView.setAdapter(timelineArrayAdapter);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }
}
