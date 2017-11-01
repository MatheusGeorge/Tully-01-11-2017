package com.example.edu.menutb.model.asynchronous.configure;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeffkenichi on 10/15/17.
 */

public class AsyncInativateAccount {


    public void inativateAccount(String idString, String tokenString){
        URL url = createURL(idString);
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();
            int httpResult = connection.getResponseCode();
            if(httpResult == HttpURLConnection.HTTP_NO_CONTENT){
                //fazer com que o usuário se deslogue
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private URL createURL(String id) {
        Log.d(null, "createURL: " + id);
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + id;
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
