package com.example.edu.menutb.model.asynchronous.profile;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jeffkenichi on 10/17/17.
 */

public class AsyncAddFollow {

    public int addFollow(String idUsuario, String idSeguindo, String tokenString){
        HttpsURLConnection connection;
        int httpResult = 0;
        try{
            URL url = new URL("https://tully-api.herokuapp.com/api/usuarios/" + idUsuario + "/seguindo");
            Log.d(null, "addFollow: " + url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);

            JSONObject object = new JSONObject();
            object.put("usuarioId", idUsuario);
            object.put("seguidoId", idSeguindo);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(object.toString());
            out.close();

            httpResult = connection.getResponseCode();
            Log.d(null, "[DEBUG] HTTP Response do post para adicionar relacionamento: " + httpResult);
        } catch (Exception e){
            e.printStackTrace();
        }
        return httpResult;
    }
}
