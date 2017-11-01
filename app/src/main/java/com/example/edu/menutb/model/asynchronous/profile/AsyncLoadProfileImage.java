package com.example.edu.menutb.model.asynchronous.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeffkenichi on 10/13/17.
 * Classe criada para executar o bitmap e devolver ela carregada
 */

public class AsyncLoadProfileImage {

    public Bitmap loadProfileImage(String urlFoto) {
        Bitmap bitmap = null;
        HttpURLConnection connection;
        try {
            Log.d(null, "fotourl" + urlFoto);
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
