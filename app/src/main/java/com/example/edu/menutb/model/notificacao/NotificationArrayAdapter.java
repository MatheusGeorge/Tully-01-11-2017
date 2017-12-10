package com.example.edu.menutb.model.notificacao;

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
import com.example.edu.menutb.controller.NotificationController;
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

public class NotificationArrayAdapter extends RecyclerView.Adapter<NotificationArrayAdapter.ViewHolder>{

    private ArrayList<Notifications> notificationsArrayList= new ArrayList<>();
    private Context context;
    private RecyclerView recyclerView;
    private String idString;
    private String tokenString;
    //===================================================================================================================================================================================
    //                                                                                  CONSTRUTOR
    //===================================================================================================================================================================================
    public NotificationArrayAdapter(ArrayList<Notifications> notificationsArrayList, Context context, String idString, String tokenString){
        this.notificationsArrayList = notificationsArrayList;
        this.context = context;
        this.idString = idString;
        this.tokenString = tokenString;
    }

    //===================================================================================================================================================================================
    //                                                                                  VIEW HOLDER
    //===================================================================================================================================================================================
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public static ImageView imageViewNotifications;
        public static TextView textViewUserNameSearch;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageViewNotifications = (ImageView) itemView.findViewById(R.id.imageViewNotifications);
            textViewUserNameSearch = (TextView) itemView.findViewById(R.id.textViewUserNameSearch);
        }
    }
    //===================================================================================================================================================================================
    //                                                                              ONBIND VIEW HOLDER
    //===================================================================================================================================================================================
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notifications notifications = notificationsArrayList.get(position);
        ViewHolder.textViewUserNameSearch.setText(notifications.getTexto());

        if(notifications.getUrlFotoPerfilUsuario().equals("")){
           ViewHolder.imageViewNotifications.setImageResource(R.mipmap.ic_nav_profile);
        } else {
            new LoadImageTask(ViewHolder.imageViewNotifications).execute(notifications.getUrlFotoPerfilUsuario());
        }
    }

    //===================================================================================================================================================================================
    //                                                                          onCreateViewHolder
    //===================================================================================================================================================================================
    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notifications_list_item, viewGroup, false);
        /*recyclerView = (RecyclerView) viewGroup.findViewById(R.id.rankingRecycleView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Notifications notifications = notificationsArrayList.get(position);

                        if(new ProfileController().isSameId(idString, notifications.getIdUsuario())){
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
        );*/
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
