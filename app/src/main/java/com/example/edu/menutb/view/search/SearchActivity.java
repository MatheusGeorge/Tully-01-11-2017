package com.example.edu.menutb.view.search;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.model.search.SearchArrayAdapter;
import com.example.edu.menutb.model.UserTully;
import java.util.ArrayList;

/**
 * Created by Edu on 20/07/2017.
 * Procura usuários com o text digitado
 */

public class SearchActivity extends Fragment {

    View myView;
    SearchView searchView;
    String textInput;
    private RecyclerView recyclerView;
    private SearchArrayAdapter searchArrayAdapter;
    GetUsersTask mGetUsersTask;

    String tokenString;
    String idString;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.search_list, container, false);

        SharedPreferences id = this.getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = id.getString("id", "");
        SearchView searchView = (SearchView) myView.findViewById(R.id.searchViewRanking);
        int idView = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText editView = (EditText) searchView.findViewById(idView);
        editView.setTextColor(getResources().getColor(R.color.colorPrimary));
        editView.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        SharedPreferences token = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
        getActivity().setTitle("");
        recyclerView = (RecyclerView) myView.findViewById(R.id.ranking_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //searchArrayAdapter = new SearchArrayAdapter(arrayListUserTully,getContext());
        //recyclerView.setAdapter(searchArrayAdapter);

        searchView = (SearchView) myView.findViewById(R.id.searchViewRanking);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                textInput = query;
                mGetUsersTask = new GetUsersTask(textInput);
                mGetUsersTask.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return myView;
    }

    private class GetUsersTask extends AsyncTask<String, Void, ArrayList<UserTully>> {
        private final String mInputText;

        GetUsersTask(String inputText) {
            mInputText = inputText;
        }

        protected ArrayList<UserTully> doInBackground(String... params) {
            ArrayList<UserTully> arrayListUserTully = new ArrayList<>();
            try {
                arrayListUserTully = new SearchController().searchUsersWithFilter(idString, tokenString, mInputText);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayListUserTully;
        }

        @Override
        protected void onCancelled() {
            mGetUsersTask = null;
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
                searchArrayAdapter = new SearchArrayAdapter(userTullyArrayList, getContext(), idString, tokenString);
                recyclerView.setAdapter(searchArrayAdapter);
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }
}