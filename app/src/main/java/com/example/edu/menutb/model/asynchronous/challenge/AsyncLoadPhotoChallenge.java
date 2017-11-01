package com.example.edu.menutb.model.asynchronous.challenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeffkenichi on 10/15/17.
 */

public class AsyncLoadPhotoChallenge {

    public Bitmap loadPhotoChallenge(String urlFoto){
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlFoto);
            connection = (HttpURLConnection) url.openConnection();
            try (InputStream inputStream = connection.getInputStream()) {
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
