package com.example.edu.menutb.view.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.profile.FollowingArrayAdapter;
import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/6/17.
 * Classe para mostrar na aba seguindo os usuários
 */

public class FollowingFragment extends Fragment {
    View myView;

    private RecyclerView recyclerView;
    GetUsersTask mGetUsersTask;

    String tokenString;
    String idString;
    String idUsuario;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_list_follow, container, false);

        SharedPreferences token = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        getActivity().setTitle("");
        SharedPreferences id = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        idUsuario = id.getString("id", "");
        idString = getArguments().getString("idString");

        recyclerView = (RecyclerView) myView.findViewById(R.id.recycleViewListPeople);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //followingArrayAdapter = new FollowingArrayAdapter(arrayListUserTully, getContext());
        //recyclerView.setAdapter(followingArrayAdapter);

        mGetUsersTask = new GetUsersTask();
        mGetUsersTask.execute();

        return myView;
    }

    private class GetUsersTask extends AsyncTask<String, Void, ArrayList<UserTully>> {

        protected ArrayList<UserTully> doInBackground(String... params) {
            try {
                return new SearchController().searchUsersNoFilter(tokenString, idString, "seguindo");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            mGetUsersTask = null;
            //showProgress(false);
        }

        protected void onPostExecute(ArrayList<UserTully> userTullyArrayList) {
            FollowingArrayAdapter followingArrayAdapter = new FollowingArrayAdapter(userTullyArrayList, getContext(), idUsuario, tokenString);
            recyclerView.setAdapter(followingArrayAdapter);
            recyclerView.smoothScrollToPosition(0);
        }
    }
}
