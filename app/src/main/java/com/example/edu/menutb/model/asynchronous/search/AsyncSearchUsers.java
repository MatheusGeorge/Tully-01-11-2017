package com.example.edu.menutb.model.asynchronous.search;

import android.util.Log;
import com.example.edu.menutb.model.UserTully;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/13/17.
 * Executa as chamadas da API e retorna para o SearchController
 */

public class AsyncSearchUsers {

    public ArrayList<UserTully> searchUsers(String tokenString, String idString, String finalContext){
        URL url = createURL(idString, finalContext);
        ArrayList<UserTully> userTullyArrayList = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            int httpResult = connection.getResponseCode();
            Log.d(null, "FOLLOWER/FOLLOWING: " + httpResult);
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
                userTullyArrayList = convertJSONToArrayList(new JSONArray(builder.toString()));
            }
            return userTullyArrayList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return userTullyArrayList;
    }

    private URL createURL(String idString, String finalContext) {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + idString + "/" + finalContext;
            Log.d(null, "context: " + urlString);
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<UserTully> searchUsers(String tokenString){
        URL url = createURL();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            int httpResult = connection.getResponseCode();
            Log.d(null, "doInBackground: " + httpResult);
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
                return convertJSONToArrayList(new JSONArray(builder.toString()));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private ArrayList<UserTully> convertJSONToArrayList(JSONArray userJSONArray) {
        ArrayList<UserTully> arrayListUsers = new ArrayList<>();

        try {
            for (int i = 0; i < userJSONArray.length(); i++) {
                JSONObject dados = userJSONArray.getJSONObject(i);
                String id = dados.getString("id");
                String name = dados.getString("nome");
                String userName = dados.getString("userName");
                String fotoPerfil = dados.getString("fotoPerfil");
                String experiencia = dados.getString("experiencia");
                String cidade = dados.getString("cidade");
                String pais = dados.getString("pais");
                arrayListUsers.add(new UserTully(id,name, userName, fotoPerfil, experiencia, cidade, pais));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayListUsers;
    }

    private URL createURL() {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios";
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int isFollow(String idString, String idSeguindo, String tokenString){
        int response = 404;
        HttpURLConnection connection;
        try {
            URL url = new URL("https://tully-api.herokuapp.com/api/relacionamentos?usuarioId="+idString+"&segueId="+idSeguindo);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            response = connection.getResponseCode();
            Log.d(null, "[DEBUG] isFollow: " + response);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
