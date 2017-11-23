package com.example.edu.menutb.model.ranking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ChallengeController;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.service.CalculateLevel;
import com.example.edu.menutb.model.service.RecyclerItemClickListener;
import com.example.edu.menutb.view.profile.ProfileActivity;
import com.example.edu.menutb.view.profile.ProfileAnotherActivity;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 11/22/17.
 */

public class RankingArrayAdapter extends RecyclerView.Adapter<RankingArrayAdapter.ViewHolder>{

    private ArrayList<UserTully> arrayListUserTully = new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private String idString;
    private String tokenString;
    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public RankingArrayAdapter(ArrayList<UserTully> arrayListUserTully, Context context, String idString, String tokenString){
        this.arrayListUserTully = arrayListUserTully;
        this.context = context;
        this.idString = idString;
        this.tokenString = tokenString;
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public static TextView textViewNameRanking;
        public static ImageView imageViewPerfilRanking;
        public static TextView textViewUserNameRanking;
        public static TextView textViewUserLevelRanking;
        public static TextView textViewUserXP;
        public static Context context;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewNameRanking = (TextView) itemView.findViewById(R.id.textViewNameRanking);
            textViewUserNameRanking = (TextView) itemView.findViewById(R.id.textViewUserNameRanking);
            imageViewPerfilRanking = (ImageView) itemView.findViewById(R.id.imageViewPerfilRanking);
            textViewUserLevelRanking = (TextView) itemView.findViewById(R.id.textViewUserLevelRanking);
            textViewUserXP = (TextView) itemView.findViewById(R.id.textViewUserXP);
            context = itemView.getContext();
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ONBIND VIEW HOLDER
    //===================================================================================================================================================================================
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final UserTully userTully = arrayListUserTully.get(position);
        ViewHolder.textViewNameRanking.setText(userTully.getName());
        ViewHolder.textViewUserNameRanking.setText(userTully.getUserName());
        ViewHolder.textViewUserLevelRanking.setText(ViewHolder.context.getString(R.string.profileLevel) + ": " +
                new CalculateLevel().calculateLevelToPerfil(userTully.getExperiencia()));
        ViewHolder.textViewUserXP.setText(ViewHolder.context.getString(R.string.profileExperience) +
                ": "+userTully.getExperiencia());

        if (userTully.getFoto_url().equals(""))
            ViewHolder.imageViewPerfilRanking.setImageResource(R.mipmap.ic_nav_profile); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.imageViewPerfilRanking).execute(userTully.getFoto_url());

    }

    //===================================================================================================================================================================================
    //                                                                          onCreateViewHolder
    //===================================================================================================================================================================================
    @Override
    public int getItemCount() {
        return arrayListUserTully.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ranking_list_item, viewGroup, false);
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.rankingRecycleView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        UserTully userTully = arrayListUserTully.get(position);

                        if(new ProfileController().isSameId(idString, userTully.getId())){
                            Intent intent = new Intent(context, ProfileActivity.class);
                            context.startActivity(intent);
                        } else {
                            new IsFollowTask(userTully).execute();
                        }
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
                return new ChallengeController().loadPhotoChallenge(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

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
