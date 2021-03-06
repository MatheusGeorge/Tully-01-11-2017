package com.example.edu.menutb.model.asynchronous.notification;

import android.util.Log;

import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.challenge.Challenge;
import com.example.edu.menutb.model.notificacao.Notifications;

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
 * Created by jeffkenichi on 12/10/17.
 */

public class AsyncNotificationTask {

    public ArrayList<Notifications> loadNotifications(String idString, String tokenString){
        ArrayList<Notifications> notificationsArrayList = new ArrayList<>();
        URL url = createURL(idString);
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
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
                notificationsArrayList = convertJSONToArrayList(new JSONArray(builder.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return notificationsArrayList;
    }

    private URL createURL(String id) {
        Log.d(null, "createURL: " + id);
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + id + "/notificacoes";
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Notifications> convertJSONToArrayList(JSONArray userJSONArray) {
        ArrayList<Notifications> notificationsArrayList = new ArrayList<>();

        try {
            for (int i = 0; i < userJSONArray.length(); i++) {
                JSONObject dados = userJSONArray.getJSONObject(i);
                String texto = dados.getString("mensagem");
                if(texto.contains("seguidor")){
                    String id = dados.getJSONObject("ator").getString("id");
                    String name = dados.getJSONObject("ator").getString("nome");
                    String fotoPerfil = dados.getJSONObject("ator").getString("fotoPerfil");
                    String experiencia = dados.getJSONObject("ator").getString("experiencia");
                    String cidade = dados.getJSONObject("ator").getString("cidade");
                    String pais = dados.getJSONObject("ator").getString("pais");
                    String cidadePais = cidade + " - " + pais;
                    notificationsArrayList.add(new Notifications(id, texto, fotoPerfil, name, experiencia, cidadePais));
                } else {
                    String fotoPerfil = dados.getJSONObject("usuario").getString("fotoPerfil");
                    String fotoUrl = dados.getJSONObject("foto").getString("fotoUrl");
                    String name = dados.getJSONObject("usuario").getString("nome");
                    String cidade = dados.getJSONObject("usuario").getString("cidade");
                    String pais = dados.getJSONObject("usuario").getString("pais");
                    String cidadePais = cidade + " - " + pais;
                    notificationsArrayList.add(new Notifications(texto, name, cidadePais, fotoUrl, fotoPerfil));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return notificationsArrayList;
    }
}
