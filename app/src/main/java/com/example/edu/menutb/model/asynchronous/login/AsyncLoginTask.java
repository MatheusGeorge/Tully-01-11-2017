package com.example.edu.menutb.model.asynchronous.login;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.example.edu.menutb.R;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.challenge.Challenge;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/20/17.
 */

public class AsyncLoginTask {

    public int isTokenValid(String tokenString){
        HttpURLConnection connection;
        int response = 0;
        try {
            URL url = new URL("https://tully-api.herokuapp.com/api/token/validar");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            response = connection.getResponseCode();
            Log.d(null, "[DEBUG] response do token valido: " + response);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public UserTully attemptLogin(String username, String password){
        URL url = createURL();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();


            JSONObject body = new JSONObject();
            body.put("usuario", username);
            body.put("senha", password);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body.toString());
            out.close();

            int httpResult = connection.getResponseCode();

            Log.d(null, "[DEBUG] Tentativa de login, reponse: " + httpResult);

            if (httpResult == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return convertJSONToUser(new JSONObject(builder.toString()));
            } else {
                System.out.println(connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    public URL createURL() {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/token";
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserTully convertJSONToUser(JSONObject userJSON) {
        try {
            String token = userJSON.getString("token");
            String id = userJSON.getJSONObject("usuario").getString("id");
            String nome = userJSON.getJSONObject("usuario").getString("nome");
            String nomeUser = userJSON.getJSONObject("usuario").getString("userName");
            String email = userJSON.getJSONObject("usuario").getString("email");
            String fotoPerfil = userJSON.getJSONObject("usuario").getString("fotoPerfil");
            String experiencia = userJSON.getJSONObject("usuario").getString("experiencia");
            String cidade = userJSON.getJSONObject("usuario").getString("cidade");
            String pais = userJSON.getJSONObject("usuario").getString("pais");
            return new UserTully(token, nome, id, nomeUser, email, experiencia, cidade, pais, fotoPerfil);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
