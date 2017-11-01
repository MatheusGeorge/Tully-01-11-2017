package com.example.edu.menutb.view.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.model.profile.ChallengeArrayAdapter;
import com.example.edu.menutb.model.profile.UserChallenge;
import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/6/17.
 * Classe para carregar as fotos do desafio completado (ABA)
 */

public class PhotoProfileFragment extends Fragment{

    private ArrayList<UserChallenge> arrayListUserChallenge = new ArrayList<>();

    private RecyclerView recyclerView;
    private ChallengeArrayAdapter challengeArrayAdapter;
    View myView;

    String idString;
    String tokenString;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_list_challenge, container, false);


        recyclerView = (RecyclerView) myView.findViewById(R.id.timeline_recycleview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle("");
        challengeArrayAdapter = new ChallengeArrayAdapter(arrayListUserChallenge);

        recyclerView.setAdapter(challengeArrayAdapter);

        /*SharedPreferences idSeguindo = getContext().getSharedPreferences("idSeguindo", Context.MODE_PRIVATE);
        idSeguindo = idSeguindo.getString("idSeguindo", "");*/
        idString = getArguments().getString("idString");
        SharedPreferences token = getContext().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");

        new GetTimelineTask().execute();
        return myView;
    }

    private class GetTimelineTask extends AsyncTask<String, Void, ArrayList<UserChallenge>> {
        protected ArrayList<UserChallenge> doInBackground(String... timeline) {
            try {
                return new ProfileController().loadChallengeAccomplish(tokenString, idString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        protected void onPostExecute(ArrayList<UserChallenge> arrayList) {
            if(arrayList.size() != 0){
                arrayListUserChallenge = arrayList;
                challengeArrayAdapter = new ChallengeArrayAdapter(arrayListUserChallenge);
                recyclerView.setAdapter(challengeArrayAdapter);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }
}
