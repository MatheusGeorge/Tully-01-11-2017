package com.example.edu.menutb.model.timeline;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.controller.TimelineController;
import com.example.edu.menutb.model.service.CalculateLevel;
import com.example.edu.menutb.view.profile.ProfileAnotherActivity;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zBrito on 25/07/2017.
 */



public class TimelineArrayAdapter extends RecyclerView.Adapter<TimelineArrayAdapter.ViewHolder> {

    private String tokenString;
    private String idString;
    private ArrayList<TimelinePhoto> arrayListTimeline;
    Context context;

    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public TimelineArrayAdapter(ArrayList<TimelinePhoto> myPhotos, Context context){
        this.arrayListTimeline = myPhotos;
        this.context = context;
    }

    public TimelineArrayAdapter(ArrayList<TimelinePhoto> myPhotos, Context context, String idString, String tokenString) {
        this.arrayListTimeline = myPhotos;
        this.context = context;
        this.idString = idString;
        this.tokenString = tokenString;
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public static ImageView photoPerfil;
        public static TextView name;
        public static TextView location;
        public static ImageButton buttonLike;
        public static ImageButton buttonDislike;
        public static TextView like;
        public static TextView dislike;
        public static TextView date;
        public static ImageView photoTimeline;
        public static TextView level;
        public static TextView city;
        public static Context context;
        public static RelativeLayout relativeLayout;

        public ViewHolder(final View itemView) {
            super(itemView);

            photoPerfil = (ImageView) itemView.findViewById(R.id.imageViewPhotoPerfil);
            name = (TextView) itemView.findViewById(R.id.textViewName);
            location = (TextView) itemView.findViewById(R.id.textViewLocation);
            buttonLike = (ImageButton) itemView.findViewById(R.id.imageButtonLike);
            buttonDislike = (ImageButton) itemView.findViewById(R.id.imageButtonDislike);
            like = (TextView) itemView.findViewById(R.id.textViewLike);
            dislike = (TextView) itemView.findViewById(R.id.textViewDislike);
            date = (TextView) itemView.findViewById(R.id.textViewDate);
            photoTimeline = (ImageView) itemView.findViewById(R.id.imageViewPhoto);
            city = (TextView) itemView.findViewById(R.id.textViewCountryCity);
            level = (TextView) itemView.findViewById(R.id.textViewLevelXP);
            context = itemView.getContext();
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.timeline_top_recycler_view);
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ONBIND VIEW HOLDER
    //===================================================================================================================================================================================
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TimelinePhoto timelinePhoto = arrayListTimeline.get(position);

        ViewHolder.name.setText(timelinePhoto.getName());
        ViewHolder.location.setText(timelinePhoto.getLocation());
        ViewHolder.date.setText(timelinePhoto.getDate());
        ViewHolder.dislike.setText(timelinePhoto.getDislike());
        ViewHolder.like.setText(timelinePhoto.getLike());
        ViewHolder.city.setText(timelinePhoto.getCidade() + " - " + timelinePhoto.getPais());
        ViewHolder.level.setText(ViewHolder.context.getString(R.string.profileLevel) + new CalculateLevel().calculateLevelToPerfil(timelinePhoto.getExperiencia()));
        if (timelinePhoto.getPhotoTimeline().equals(""))
            ViewHolder.photoTimeline.setImageResource(R.drawable.ic_menu_ranking); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.photoTimeline, timelinePhoto).execute(timelinePhoto.getPhotoTimeline(), "timeline");

        if(timelinePhoto.getPhotoPerfil().equals(""))
            ViewHolder.photoPerfil.setImageResource(R.drawable.ic_menu_ranking); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.photoPerfil, timelinePhoto).execute(timelinePhoto.getPhotoPerfil(), "perfil");

        ViewHolder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new EvaluatePhotoTask(timelinePhoto).execute(AvaliacaoEnum.LIKE);
            }
        });

        ViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new goToProfile(arrayListTimeline.get(position)).execute();
            }
        });

        ViewHolder.photoTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimeline(timelinePhoto);

            }

        });

        ViewHolder.buttonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FAZ ALGUMA COISA QUANDO CLICKA NO COMMENT
            }
        });
    }

    public void showDialogTimeline(TimelinePhoto timelinePhoto) {
        final Dialog dialogPhotoTimeline = new Dialog(context);
        dialogPhotoTimeline.setContentView(R.layout.photo_zoom);
        try{
            ImageView photoTimeline = (ImageView) dialogPhotoTimeline.findViewById(R.id.imageViewPhotoZoom);
            photoTimeline.setImageBitmap(timelinePhoto.getFotoTimelineBitmap());
            //new LoadImageTask(photoTimeline).execute(timelinePhoto.getPhotoTimeline());
            ImageView photoProfile = (ImageView) dialogPhotoTimeline.findViewById(R.id.imageViewPhotoPerfil);
            photoProfile.setImageBitmap(timelinePhoto.getFotoPerfilBitmap());
            //new LoadImageTask(photoProfile).execute(timelinePhoto.getPhotoPerfil());

            TextView name = (TextView) dialogPhotoTimeline.findViewById(R.id.textViewName);
            name.setText(timelinePhoto.getName());
            TextView local = (TextView) dialogPhotoTimeline.findViewById(R.id.textViewLocation);
            local.setText(timelinePhoto.getLocation());
        } catch (Exception e){
            e.printStackTrace();
        }
        dialogPhotoTimeline.show();
    }


    //===================================================================================================================================================================================
    //                                                                          onCreateViewHolder
    //===================================================================================================================================================================================
    @Override
    public int getItemCount() {
        return arrayListTimeline.size();
    }

    @Override
    public TimelineArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timeline_list_item, viewGroup, false);
        return new ViewHolder(v);
    }
    //===================================================================================================================================================================================
    //                                                                              ASYNC TASK
    //===================================================================================================================================================================================
    private class LoadImageTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView imageView;
        private TimelinePhoto timelinePhoto;

        public LoadImageTask(ImageView imageView, TimelinePhoto timelinePhoto) {
            this.imageView = imageView;
            this.timelinePhoto = timelinePhoto;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;

            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                try (InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if(params[1].equals("timeline"))
                        timelinePhoto.setFotoTimelineBitmap(bitmap);
                    else
                        timelinePhoto.setFotoPerfilBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class goToProfile extends AsyncTask<String, Void, String>{
        private TimelinePhoto timeLinePhoto;

        public goToProfile(TimelinePhoto timeLinePhoto){
            this.timeLinePhoto = timeLinePhoto;
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                return new SearchController().isFollow(idString, timeLinePhoto.getId(), tokenString);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(context, ProfileAnotherActivity.class);
            intent.putExtra("id", timeLinePhoto.getId());
            intent.putExtra("experience", timeLinePhoto.getExperiencia());
            intent.putExtra("city", timeLinePhoto.getPais() +" - " +timeLinePhoto.getCidade());
            intent.putExtra("url", timeLinePhoto.getPhotoPerfil());
            intent.putExtra("name", timeLinePhoto.getName());
            intent.putExtra("follow", s);
            context.startActivity(intent);

        }
    }

    /*private class EvaluatePhotoTask extends AsyncTask<AvaliacaoEnum,Void,String> {
        private TimelinePhoto timelinePhoto;

        public EvaluatePhotoTask(TimelinePhoto timelinePhoto) {
            this.timelinePhoto = timelinePhoto;
        }

        @Override
        protected String doInBackground(AvaliacaoEnum... params) {
            String result = "";
            try {
                result = new TimelineController().evaluatePhotoPerfil(timelinePhoto.getId(), idString, tokenString, params[0].toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return result;
        }

        protected void onPostExecute(Bitmap bitmap) {

        }
    }*/

    //===================================================================================================================================================================================
    //                                                                          METODO LIKE
    //===================================================================================================================================================================================
    private void like(){
        Boolean like = false;
        if(/*ja curtiu??? ent\E3o descurti*/ like){

        } else {

        }
    }
    //===================================================================================================================================================================================
    //                                                                          METODO COMMENT
    //===================================================================================================================================================================================
    private void comment(){
        //seila
    }
}