package com.example.edu.menutb.model.asynchronous.profile;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jeffkenichi on 10/18/17.
 * Classe para deixar de seguir a pessoa
 */

public class AsyncRemoveFollow {

    public int removeFollow(String idUsuario, String idSeguindo, String tokenString){
        int response = 0;
        HttpURLConnection httpCon;
        try {
            URL url = new URL("https://tully-api.herokuapp.com/api/usuarios/"+idUsuario+"/seguindo/"+idSeguindo);
            Log.d(null, "[DEBUG] url: " + url);
            httpCon = (HttpURLConnection) url.openConnection();

            httpCon.setRequestMethod("DELETE");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Authorization", "bearer " + tokenString);

            response = httpCon.getResponseCode();
            Log.d(null, "[DEBUG] DELETE httpResponse: " + response);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
