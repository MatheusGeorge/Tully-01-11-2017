package com.example.edu.menutb.view.profile;

import android.Manifest;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.edu.menutb.R;
import com.example.edu.menutb.controller.ProfileController;
import com.example.edu.menutb.model.profile.ChallengePagerAdapter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by Edu on 18/07/2017.
 * Acitivity do Profile
 */

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    private static final int PICK_FROM_GALLERY = 12345;

    String imagePath;
    TextView textViewNameProfile;
    TextView textViewCity;
    TextView textViewNumberExperience;
    TextView textViewNumberLevel;
    ImageView imageViewProfileFrente;
    ImageView imageViewProfileFundo;
    String resultId;
    Bitmap photoPerfil;
    ImageButton imageButtonChangePhoto;
    PostImageTask mPostImageTask;
    String tokenString;
    //Tablayout
    ChallengePagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    TabLayout tabLayout;
    CharSequence titles[];
    String idString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        SharedPreferences id = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        idString = id.getString("id", "");
        loadThings();
        loadSharedPreferences();
        loadProfileData();
        loadToolbar();
        setUpTabs();
    }

    private void loadThings(){
        textViewNameProfile = (TextView) findViewById(R.id.textViewNameProfile);
        textViewCity = (TextView) findViewById(R.id.textViewCity);
        textViewNumberExperience = (TextView) findViewById(R.id.textViewNumberExperience);
        textViewNumberLevel = (TextView) findViewById(R.id.textViewNumberLevel);

        imageViewProfileFrente = (ImageView) findViewById(R.id.imageViewProfileFrente);

        imageButtonChangePhoto = (ImageButton) findViewById(R.id.imageButtonChangePhoto);
        imageButtonChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCameraPermission();
            }
        });

        // Tablayout.
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabLayoutProfile);
        titles = new CharSequence[]{getString(R.string.profileChallenges), getString(R.string.profileFollowers), getString(R.string.profileFollowing)};
    }

    private void loadSharedPreferences(){
        SharedPreferences idBusca = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        resultId = idBusca.getString("id", "");

        SharedPreferences token = this.getSharedPreferences("token", Context.MODE_PRIVATE);
        tokenString = token.getString("token", "");
    }

    private void setUpTabs(){
        mSectionsPagerAdapter = new ChallengePagerAdapter(getSupportFragmentManager(), titles, titles.length, idString);
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



    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Precisamos da sua câmera ", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);

        } /*else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);

        } */else {

            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao criar o arquivo", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.edu.menutb", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd:HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        imagePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            photoPerfil = (Bitmap) extras.get("data");
            Log.d(null, "onActivityResult: ENTROU NO REQUEST CAMERA");
            imageViewProfileFrente.setImageBitmap(photoPerfil);
            //imageViewProfileFundo.setImageBitmap(photoPerfil);
            mPostImageTask = new PostImageTask(imagePath);
            mPostImageTask.execute();
            //riv.setScaleType(ImageView.ScaleType.CENTER_CROP); //nao pode usar scaleType.FIT
        } else {
            Log.d(null, "onActivityResult: DATA NULOO");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        dispatchTakePictureIntent();
                    }
                }
                break;
        }
    }

    private void loadProfileData() {
        AsyncTask task = new AsyncTask() {
            String[] valores;
            @Override
            protected Object doInBackground(Object[] objects) {
                valores = new ProfileController().loadSelectProfile(resultId, getBaseContext());
                //Para fazer com que a thread principal faça as mudanças no UI e não o AsyncTask
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setaAsCoisas(valores[0], valores[1], valores[2], valores[3], valores[4], valores[5]);
                    }
                });
                return null;
            }
        };
        task.execute();
    }

    private void setaAsCoisas(String name, String city, String country, String photo, String usuarioXP, String usuarioLevel) {
        textViewNameProfile.setText(String.valueOf(name));
        textViewCity.setText(String.valueOf(city) + " - " + String.valueOf(country));
        textViewNumberExperience.setText(String.valueOf(usuarioXP));
        textViewNumberLevel.setText(usuarioLevel);
        new LoadImageTask(imageViewProfileFrente).execute(String.valueOf(photo));
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
    private class PostImageTask extends AsyncTask<String, Void, String> {
        private String imagePath;

        public PostImageTask(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                response = new ProfileController().postToFirebase(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onCancelled() {
            mPostImageTask = null;
            //showProgress(false);
        }

        protected void onPostExecute(String response) {
            PathImageTask mPathImage = new PathImageTask(response);
            mPathImage.execute();
        }

        private class PathImageTask extends AsyncTask<String, Void, String> {
            private String urlFoto;

            public PathImageTask(String url) {
                this.urlFoto = url;
            }
            @Override
            protected String doInBackground(String... params) {
                try {
                    new ProfileController().changePicture(tokenString, resultId, urlFoto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(final String s) {
                AsyncTask task = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        new ProfileController().updatePictureProfile(resultId, getBaseContext(), urlFoto);
                        return null;
                    }
                };
                task.execute();
            }
        }
    }

}