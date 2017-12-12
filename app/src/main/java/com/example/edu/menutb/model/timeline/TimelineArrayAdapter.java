package com.example.edu.menutb.model.timeline;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.controller.SearchController;
import com.example.edu.menutb.controller.TimelineController;
import com.example.edu.menutb.model.service.CalculateLevel;
import com.example.edu.menutb.view.profile.ProfileActivity;
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
    private final Handler handler;

    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public TimelineArrayAdapter(ArrayList<TimelinePhoto> myPhotos, Context context) {
        this.arrayListTimeline = myPhotos;
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
    }

    public TimelineArrayAdapter(ArrayList<TimelinePhoto> myPhotos, Context context, String idString, String tokenString) {
        this.arrayListTimeline = myPhotos;
        this.context = context;
        this.idString = idString;
        this.tokenString = tokenString;
        this.handler = new Handler(context.getMainLooper());
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_like);
        ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_dislike);
        if(timelinePhoto.getType() != null){
            if(timelinePhoto.getType().equalsIgnoreCase("Positivo")){
                ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_liked);
                ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_dislike);
            } else if(timelinePhoto.getType().equalsIgnoreCase("Negativo")){
                ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_like);
                ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_disliked);
            }
        }
        ViewHolder.city.setText(timelinePhoto.getCidade() + " - " + timelinePhoto.getPais());
        ViewHolder.level.setText(ViewHolder.context.getString(R.string.profileLevel) + new CalculateLevel().calculateLevelToPerfil(timelinePhoto.getExperiencia()));
        if (timelinePhoto.getPhotoTimeline().equals(""))
            ViewHolder.photoTimeline.setImageResource(R.drawable.ic_menu_ranking); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.photoTimeline, timelinePhoto).execute(timelinePhoto.getPhotoTimeline(), "timeline");

        if (timelinePhoto.getPhotoPerfil().equals(""))
            ViewHolder.photoPerfil.setImageResource(R.drawable.ic_menu_ranking); //trocar o drawable
        else
            new LoadImageTask(ViewHolder.photoPerfil, timelinePhoto).execute(timelinePhoto.getPhotoPerfil(), "perfil");


        ViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(new ProfileController().isSameId(idString, timelinePhoto.getIdUsuario())){
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                } else {
                    new goToProfile(arrayListTimeline.get(position)).execute();
                }
            }
        });

        ViewHolder.photoTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimeline(timelinePhoto);
            }

        });

        ViewHolder.buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timelinePhoto.getType().equalsIgnoreCase("none"))
                    new EvaluatePhotoTask(timelinePhoto).execute("Positivo", "POST");
                else if (timelinePhoto.getType().equalsIgnoreCase("Positivo"))
                    new EvaluatePhotoTask(timelinePhoto).execute("none", "DELETE", "Positivo");
                else if (timelinePhoto.getType().equalsIgnoreCase("Negativo"))
                    new EvaluatePhotoTask(timelinePhoto).execute("Positivo", "PATCH", "Negativo");
            }
        });

        ViewHolder.buttonDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timelinePhoto.getType().equalsIgnoreCase("none"))
                    new EvaluatePhotoTask(timelinePhoto).execute("Negativo", "POST");
                else if (timelinePhoto.getType().equalsIgnoreCase("Negativo"))
                    new EvaluatePhotoTask(timelinePhoto).execute("none", "DELETE", "Negativo");
                else if (timelinePhoto.getType().equalsIgnoreCase("Positivo"))
                    new EvaluatePhotoTask(timelinePhoto).execute("Negativo", "PATCH", "Positivo");
            }
        });
    }

    public void showDialogTimeline(TimelinePhoto timelinePhoto) {
        final Dialog dialogPhotoTimeline = new Dialog(context);
        dialogPhotoTimeline.setContentView(R.layout.photo_zoom);
        try {
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
        } catch (Exception e) {
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
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
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
                    if (params[1].equals("timeline"))
                        timelinePhoto.setFotoTimelineBitmap(bitmap);
                    else
                        timelinePhoto.setFotoPerfilBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class goToProfile extends AsyncTask<String, Void, String> {
        private TimelinePhoto timeLinePhoto;

        public goToProfile(TimelinePhoto timeLinePhoto) {
            this.timeLinePhoto = timeLinePhoto;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return new SearchController().isFollow(idString, timeLinePhoto.getIdUsuario(), tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(context, ProfileAnotherActivity.class);
            intent.putExtra("id", timeLinePhoto.getIdUsuario());
            intent.putExtra("experience", timeLinePhoto.getExperiencia());
            intent.putExtra("city", timeLinePhoto.getPais() + " - " + timeLinePhoto.getCidade());
            intent.putExtra("url", timeLinePhoto.getPhotoPerfil());
            intent.putExtra("name", timeLinePhoto.getName());
            intent.putExtra("follow", s);
            context.startActivity(intent);

        }
    }

    private class EvaluatePhotoTask extends AsyncTask<String, Void, TimelinePhoto> {
        private TimelinePhoto timelinePhoto;

        public EvaluatePhotoTask(TimelinePhoto timelinePhoto) {
            this.timelinePhoto = timelinePhoto;
        }

        @Override
        protected TimelinePhoto doInBackground(String... params) {
            TimelinePhoto result = null;
            try {
                if (params[1].toString().equalsIgnoreCase("DELETE")) {
                    Log.d(null, "ID AVALIACAO DELETE: " + timelinePhoto.getIdAvaliacao());
                    result = new TimelineController().evaluatePhotoPerfil(timelinePhoto.getIdAvaliacao(), tokenString, params[0].toString(), params[1].toString());
                } else if (params[1].toString().equalsIgnoreCase("POST")) {
                    result = new TimelineController().evaluatePhotoPerfil(timelinePhoto.getId(), idString, tokenString, params[0].toString(), params[1].toString());
                } else {
                    result = new TimelineController().evaluatePhotoPerfil(timelinePhoto.getIdAvaliacao(), tokenString, params[0].toString(), params[1].toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (result != null) {
                    timelinePhoto.setType(result.getType());
                    if (params[1].toString().equalsIgnoreCase("DELETE")) {
                        if (params[2].toString().equalsIgnoreCase("Positivo")) {
                            int like = Integer.parseInt(timelinePhoto.getLike());
                            like--;
                            String finallike = String.valueOf(like);
                            timelinePhoto.setLike(finallike);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.like.setText(timelinePhoto.getLike());
                                    ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_like);
                                }
                            });
                        } else if (params[2].toString().equalsIgnoreCase("Negativo")) {
                            int dislike = Integer.parseInt(timelinePhoto.getDislike());
                            dislike--;
                            String finaldislike = String.valueOf(dislike);
                            timelinePhoto.setDislike(finaldislike);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.dislike.setText(timelinePhoto.getDislike());
                                    ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_dislike);
                                }
                            });
                        }
                    } else if (params[1].toString().equalsIgnoreCase("PATCH")) {
                        if (params[2].toString().equalsIgnoreCase("Positivo")) {
                            int like = Integer.parseInt(timelinePhoto.getLike());
                            int dislike = Integer.parseInt(timelinePhoto.getDislike());
                            like--;
                            dislike++;
                            String finaldislike = String.valueOf(dislike);
                            String finallike = String.valueOf(like);
                            timelinePhoto.setDislike(finaldislike);
                            timelinePhoto.setLike(finallike);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.like.setText(timelinePhoto.getLike());
                                    ViewHolder.dislike.setText(timelinePhoto.getDislike());
                                    ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_disliked);
                                    ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_like);
                                }
                            });
                        } else if (params[2].toString().equalsIgnoreCase("Negativo")) {
                            int dislike = Integer.parseInt(timelinePhoto.getDislike());
                            int like = Integer.parseInt(timelinePhoto.getLike());
                            like++;
                            dislike--;
                            String finaldislike = String.valueOf(dislike);
                            String finallike = String.valueOf(like);
                            timelinePhoto.setDislike(finaldislike);
                            timelinePhoto.setLike(finallike);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.like.setText(timelinePhoto.getLike());
                                    ViewHolder.dislike.setText(timelinePhoto.getDislike());
                                    ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_dislike);
                                    ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_liked);
                                }
                            });
                        }
                    } else if (params[1].toString().equalsIgnoreCase("POST")) {
                        if (params[0].toString().equalsIgnoreCase("Positivo")) {
                            timelinePhoto.setIdAvaliacao(result.getIdAvaliacao());
                            Log.d(null, "ID avaliacao: " + timelinePhoto.getIdAvaliacao());
                            timelinePhoto.setLike(result.getLike());
                            timelinePhoto.setDislike(result.getDislike());
                            timelinePhoto.setType(result.getType());
                            Log.d(null, "VALOR DO LIKE: " + timelinePhoto.getLike());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.like.setText(timelinePhoto.getLike());
                                    ViewHolder.buttonLike.setImageResource(R.drawable.ic_photo_liked);
                                }
                            });
                        } else {
                            timelinePhoto.setIdAvaliacao(result.getIdAvaliacao());
                            Log.d(null, "ID avaliacao: " + timelinePhoto.getIdAvaliacao());
                            timelinePhoto.setLike(result.getLike());
                            timelinePhoto.setDislike(result.getDislike());
                            timelinePhoto.setType(result.getType());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewHolder.dislike.setText(timelinePhoto.getDislike());
                                    ViewHolder.buttonDislike.setImageResource(R.drawable.ic_photo_disliked);
                                }
                            });
                        }
                    }
                }
            }
            return result;
        }

        protected void onPostExecute(TimelinePhoto response) {

        }
    }

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    //===================================================================================================================================================================================
    //                                                                          METODO LIKE
    //===================================================================================================================================================================================
    private void like() {
        Boolean like = false;
        if (/*ja curtiu??? ent\E3o descurti*/ like) {

        } else {

        }
    }

    //===================================================================================================================================================================================
    //                                                                          METODO COMMENT
    //===================================================================================================================================================================================
    private void comment() {
        //seila
    }
}