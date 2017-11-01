package com.example.edu.menutb.view.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.model.profile.ChallengePagerAdapter;

/**
 * Created by jeffkenichi on 10/13/17.
 * Instanciar a View de outro usuário
 */

public class ProfileAnotherActivity extends AppCompatActivity{

    TextView textViewNameProfile;
    TextView textViewCity;
    TextView textViewNumberExperience;
    TextView textViewNumberLevel;
    ImageView imageViewProfileFrente;
    ImageButton imageButtonAddRelation;
    String resultId;
    String tokenString;
    //Tablayout
    ChallengePagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    CharSequence titles[];
    String id;
    String nome;
    Boolean isFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_other_user);
        loadThings();
        loadBundle();
        loadSharedPreferences();
        loadToolbar();
        setUpTabs();


        imageButtonAddRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFollowing){
                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ProfileAnotherActivity.this);
                    final View alterarView = layoutInflaterAndroid.inflate(R.layout.remove_follow_dialog, null);
                    TextView textView = (TextView) alterarView.findViewById(R.id.dialogTitle);
                    textView.setTextColor(getResources().getColor(R.color.colorBranco));
                    textView.setText(ProfileAnotherActivity.this.getString(R.string.followMessage) + " " + nome + " ?");
                    AlertDialog.Builder dialogName = new AlertDialog.Builder(ProfileAnotherActivity.this, R.style.MyDialogTheme);
                    dialogName.setView(alterarView);
                    dialogName.setCancelable(false).setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            new RemoveFollowTask().execute();
                        }
                    }).setNegativeButton(getText(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogBox, int id) {
                            dialogBox.cancel();
                        }
                    });
                    AlertDialog dialogFinalName = dialogName.create();
                    dialogFinalName.show();
                    dialogFinalName.getButton(dialogFinalName.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBranco));
                    dialogFinalName.getButton(dialogFinalName.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBranco));
                } else {
                    AddFollowTask addFollowTask = new AddFollowTask();
                    addFollowTask.execute();
                    Snackbar.make(findViewById(R.id.coordinatorLayoutProfile), getString(R.string.loginAccept), Snackbar.LENGTH_SHORT)
                            .setAction("", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                }
            }
        });
    }

    private class RemoveFollowTask extends  AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            int response = 0;
            try {
                response = new ProfileController().deleteFollow(resultId, id, tokenString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(Integer result){
            if(result == 204){
                imageButtonAddRelation.setImageResource(R.drawable.ic_add_follower);
                isFollowing = false;
                setUpTabs();
            } else {

            }
        }
    }

    private class AddFollowTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            int response = 0;
            try{
                response = new ProfileController().addFollow(resultId, id, tokenString);
            } catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 201){
                isFollowing = true;
                Snackbar.make(findViewById(R.id.coordinatorLayoutProfile), getString(R.string.followOK) + " " + nome, Snackbar.LENGTH_LONG)
                        .setAction("", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
                imageButtonAddRelation.setImageResource(R.drawable.ic_follow_check);
                setUpTabs();
            } else {
                Snackbar.make(findViewById(R.id.coordinatorLayoutProfile), getString(R.string.connectionError), Snackbar.LENGTH_SHORT)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setActionTextColor(getResources().getColor(android.R.color.white)).show();
            }
        }
    }

    private void loadBundle(){
        Bundle bundle = getIntent().getExtras();
        nome = bundle.getString("name");
        String city = bundle.getString("city");
        String experience = bundle.getString("experience");
        String urlProfile = bundle.getString("url");
        id = bundle.getString("id");
        isFollowing = bundle.getString("follow").equals("true")?true:false;
        if(isFollowing)
            imageButtonAddRelation.setImageResource(R.drawable.ic_follow_check);
        setThings(nome, city, experience, urlProfile);
    }

    private void setThings(String nome, String city, String experience, String urlProfile){
        textViewNameProfile.setText(nome);
        textViewCity.setText(city);
        int experiencia = Integer.parseInt(experience);
        String level = String.valueOf(experiencia < 20? 1 + " (0)":2 + " (20)");
        experience = level.contains("1")?experience+"/20": (String.valueOf(experiencia-20) + "/40");
        textViewNumberExperience.setText(experience);
        textViewNumberLevel.setText(level);
        new LoadImageTask(imageViewProfileFrente).execute(urlProfile);
    }

    private void loadThings(){
        textViewNameProfile = (TextView) findViewById(R.id.textViewNameAnotherProfile);
        textViewCity = (TextView) findViewById(R.id.textViewAnotherCity);
        textViewNumberExperience = (TextView) findViewById(R.id.textViewNumberAnotherExperience);
        textViewNumberLevel = (TextView) findViewById(R.id.textViewNumberAnotherLevel);
        imageButtonAddRelation = (ImageButton) findViewById(R.id.imageButtonAddRelation);
        if(isFollowing){
            imageButtonAddRelation.setImageResource(R.drawable.ic_follow_check);
        }
        imageViewProfileFrente = (ImageView) findViewById(R.id.imageViewAnotherProfileFrente);
        // Tablayout.
        mViewPager = (ViewPager) findViewById(R.id.container_another);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutAnotherProfile);
        titles = new CharSequence[]{getString(R.string.profileChallenges), getString(R.string.profileFollowers), getString(R.string.profileFollowing)};
    }

    private void loadSharedPreferences(){
        SharedPreferences idBusca = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        resultId = idBusca.getString("id", "");

        SharedPreferences token = this.getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
    }

    private void setUpTabs(){
        mSectionsPagerAdapter = new ChallengePagerAdapter(getSupportFragmentManager(), titles, titles.length, id);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void loadToolbar(){
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        //adiciona o ícone na action bar
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(getResources().getColor(R.color.colorBranco), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //adiciona botao voltar
        getSupportActionBar().setHomeButtonEnabled(true);
        //TROCAR A FONTE
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/amaticscbold.ttf");
        TextView textToolBarTitle = (TextView) findViewById(R.id.toolbarTitleProfile);
        textToolBarTitle.setTypeface(type);
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        //private RoundedImageView imageViewProfileFrente;
        //private ImageView imageViewProfileFundo;
        private ImageView imageViewProfileFrente;
        public LoadImageTask(ImageView imageViewProfileFrente) {
            this.imageViewProfileFrente = imageViewProfileFrente;
            //this.imageViewProfileFundo = imageViewProfileFundo;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = new ProfileController().loadProfileImage(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap bitmap) {
            imageViewProfileFrente.setImageBitmap(bitmap);
            imageViewProfileFrente.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageViewProfileFundo.setImageBitmap(bitmap);
            //imageViewProfileFundo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

}