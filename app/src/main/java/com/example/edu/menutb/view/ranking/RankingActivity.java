package com.example.edu.menutb.view.ranking;

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
import com.example.edu.menutb.controller.RankingController;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.ranking.RankingArrayAdapter;
import com.example.edu.menutb.model.search.SearchArrayAdapter;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 11/22/17.
 */

public class RankingActivity extends Fragment {
    private View myView;
    private String tokenString;
    private GetRanking getRanking;
    private RankingArrayAdapter rankingArrayAdapter;
    private RecyclerView recyclerView;
    private String idString;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ranking_list, container, false);
        SharedPreferences token = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        SharedPreferences id = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = id.getString("id", "");
        recyclerView = (RecyclerView) myView.findViewById(R.id.rankingRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getRanking = new GetRanking();
        getRanking.execute();
        return myView;
    }

    private class GetRanking extends AsyncTask<String, Void, ArrayList<UserTully>> {
        protected ArrayList<UserTully> doInBackground(String... params) {
            ArrayList<UserTully> arrayListUserTully = new ArrayList<>();
            try {
                arrayListUserTully = new RankingController().getRanking(tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayListUserTully;
        }

        @Override
        protected void onCancelled() {
            getRanking = null;
            //showProgress(false);
        }

        protected void onPostExecute(ArrayList<UserTully> userTullyArrayList) {
            if (userTullyArrayList.size() == 0) {
                Snackbar.make(myView.findViewById(R.id.relativeLayoutRanking), getString(R.string.noUserFound), Snackbar.LENGTH_LONG)
                        .setAction(getText(R.string.close), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                return;
            } else {
                rankingArrayAdapter = new RankingArrayAdapter(userTullyArrayList, getContext(), idString, tokenString);
                recyclerView.setAdapter(rankingArrayAdapter);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }
}
