package com.example.edu.menutb.model.search;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.model.profile.FollowerArrayAdapter;
import com.example.edu.menutb.model.service.RecyclerItemClickListener;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.view.profile.ProfileAnotherActivity;
import com.example.edu.menutb.model.service.CalculateLevel;

import java.util.ArrayList;

/**
 * Created by zBrito on 09/12/2017.
 */

public class SearchArrayAdapter extends RecyclerView.Adapter<SearchArrayAdapter.ViewHolder> {

    private ArrayList<UserTully> arrayListUserTully;
    RecyclerView recyclerView;
    Context context;
    String idString;
    String tokenString;

    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public SearchArrayAdapter(ArrayList<UserTully> myArrayListUserTully, Context context, String idString, String tokenString){
        this.arrayListUserTully = myArrayListUserTully;
        this.context = context;
        this.idString = idString;
        this.tokenString = tokenString;
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public static ImageView photoPerfilRanking;
        public static TextView name;
        public static TextView userName;
        public static TextView local;
        public static TextView level;
        public static Context context;

        public ViewHolder(final View itemView) {
            super(itemView);

            photoPerfilRanking = (ImageView) itemView.findViewById(R.id.imageViewPerfilSearch);
            name = (TextView) itemView.findViewById(R.id.textViewNameSearch);
            userName = (TextView) itemView.findViewById(R.id.textViewUserNameSearch);
            level = (TextView) itemView.findViewById(R.id.textViewLevelUserSearch);
            local = (TextView) itemView.findViewById(R.id.textViewUserLocal);
            context = itemView.getContext();
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ONBIND VIEW HOLDER
    //===================================================================================================================================================================================
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final UserTully userTully = arrayListUserTully.get(position);
        ViewHolder.name.setText(userTully.getName());
        ViewHolder.userName.setText(userTully.getUserName());
        ViewHolder.local.setText(userTully.getPais() + " - " + userTully.getCidade());
        ViewHolder.level.setText(ViewHolder.context.getString(R.string.profileLevel) + new CalculateLevel().calculateLevelToPerfil(userTully.getExperiencia()));
        if (userTully.getFoto_url().equals("")){
            ViewHolder.photoPerfilRanking.setImageResource(R.mipmap.ic_nav_profile); //trocar o drawable
        }
        else{
            new LoadImageTask(ViewHolder.photoPerfilRanking).execute(userTully.getFoto_url());
        }
    }

    //===================================================================================================================================================================================
    //                                                                          onCreateViewHolder
    //===================================================================================================================================================================================
    @Override
    public int getItemCount() {
        return arrayListUserTully.size();
    }

    @Override
    public SearchArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_item, viewGroup, false);
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.ranking_recycleView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        new IsFollowTask(arrayListUserTully.get(position)).execute();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return new ViewHolder(v);
    }
    //===================================================================================================================================================================================
    //                                                                              ASYNC TASK
    //===================================================================================================================================================================================
    private class LoadImageTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return new ProfileController().loadProfileImage(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ASYNC TASK
    //===================================================================================================================================================================================
    private class IsFollowTask extends AsyncTask<String, Void, String>{
        private UserTully userTully;

        public IsFollowTask(UserTully userTully){
            this.userTully = userTully;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                return new SearchController().isFollow(idString, userTully.getId(), tokenString);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(context, ProfileAnotherActivity.class);
            intent.putExtra("id", userTully.getId());
            intent.putExtra("experience", userTully.getExperiencia());
            intent.putExtra("city", userTully.getPais() +" - " +userTully.getCidade());
            intent.putExtra("url", userTully.getFoto_url());
            intent.putExtra("name", userTully.getName());
            intent.putExtra("follow", s);
            context.startActivity(intent);

        }
    }
}