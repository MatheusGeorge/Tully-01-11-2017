package com.example.edu.menutb.model.profile;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/6/17.
 */

public class ChallengeArrayAdapter extends RecyclerView.Adapter<ChallengeArrayAdapter.ViewHolder>{
    private ArrayList<UserChallenge> arrayListUserChallenge;

    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public ChallengeArrayAdapter(ArrayList<UserChallenge> arrayListUserChallenge){
        this.arrayListUserChallenge = arrayListUserChallenge;
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public static TextView location;
        public static TextView like;
        public static TextView dislike;
        public static ImageView photoChallenge;
        public static ImageView photoPerfil;
        public static TextView data;
        public static TextView name;
        private final Context context;

        public ViewHolder(final View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.textViewLocation);
            like = (TextView) itemView.findViewById(R.id.textViewLike);
            dislike = (TextView) itemView.findViewById(R.id.textViewDislike);
            photoChallenge = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
            data = (TextView) itemView.findViewById(R.id.textViewDate);
            photoPerfil = (ImageView) itemView.findViewById(R.id.imageViewPhotoPerfil);
            name = (TextView) itemView.findViewById(R.id.textViewName);
            context = itemView.getContext();
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ONBIND VIEW HOLDER
    //===================================================================================================================================================================================
    @Override
    public void onBindViewHolder(ChallengeArrayAdapter.ViewHolder holder, int position) {

        final UserChallenge userChallenge = arrayListUserChallenge.get(position);
        ViewHolder.data.setText(userChallenge.getData());
        ViewHolder.location.setText(userChallenge.getLocal());
        ViewHolder.like.setText(userChallenge.getLike());
        ViewHolder.dislike.setText(userChallenge.getDislike());
        ViewHolder.name.setText(userChallenge.getNome());

        if (userChallenge.getFotoUrlChallenge().equals(""))
            ViewHolder.photoChallenge.setImageResource(R.mipmap.ic_nav_profile); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.photoChallenge).execute(userChallenge.getFotoUrlChallenge());
        if (userChallenge.getFotoUrlChallenge().equals(""))
            ViewHolder.photoPerfil.setImageResource(R.mipmap.ic_nav_profile); //trocar o drawable
        else
        new LoadImageTask(ViewHolder.photoPerfil).execute(userChallenge.getFotoUrlPerfil());

    }

    //===================================================================================================================================================================================
    //                                                                          onCreateViewHolder
    //===================================================================================================================================================================================
    @Override
    public int getItemCount() {
        return arrayListUserChallenge.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_list_challenge_item, viewGroup, false);
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

}
