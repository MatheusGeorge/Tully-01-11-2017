package com.example.edu.menutb.view.notification;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.NotificationController;
import com.example.edu.menutb.controller.RankingController;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.notificacao.NotificationArrayAdapter;
import com.example.edu.menutb.model.notificacao.Notifications;
import com.example.edu.menutb.model.ranking.RankingArrayAdapter;
import com.example.edu.menutb.view.ranking.RankingActivity;

import java.util.ArrayList;

/**
 * Created by Edu on 18/07/2017.
 */

public class NotificationAcitivity extends Fragment {
    View myView;
    private String tokenString;
    private NotificationArrayAdapter notificationArrayAdapter;
    private RecyclerView recyclerView;
    private String idString;
    private GetNotifications getNotifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.notifications_list, container, false);
        getActivity().setTitle("");
        SharedPreferences token = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        SharedPreferences id = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = id.getString("id", "");
        recyclerView = (RecyclerView) myView.findViewById(R.id.notificationsRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getNotifications = new GetNotifications();
        getNotifications.execute();
        return myView;
    }


    private class GetNotifications extends AsyncTask<String, Void, ArrayList<Notifications>> {
        @Override
        protected ArrayList<Notifications> doInBackground(String... params) {
            try {
                return new NotificationController().loadNotifications(idString, tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onCancelled() {
            getNotifications = null;
            //showProgress(false);
        }

        protected void onPostExecute(ArrayList<Notifications> notificationsArrayList) {
            if (notificationsArrayList.size() == 0) {
                Snackbar.make(myView.findViewById(R.id.relativeLayoutNotifications), getString(R.string.noNotificationFound), Snackbar.LENGTH_LONG)
                        .setAction(getText(R.string.close), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                return;
            } else {
                notificationArrayAdapter = new NotificationArrayAdapter(notificationsArrayList, getContext(), idString, tokenString);
                recyclerView.setAdapter(notificationArrayAdapter);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }
}
