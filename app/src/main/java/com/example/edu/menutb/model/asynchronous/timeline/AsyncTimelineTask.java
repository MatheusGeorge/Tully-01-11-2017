package com.example.edu.menutb.model.asynchronous.timeline;

import android.util.Log;

import com.example.edu.menutb.model.timeline.TimelinePhoto;

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
 * Created by jeffkenichi on 10/27/17.
 */

public class AsyncTimelineTask {

    public ArrayList<TimelinePhoto> getTimeline(String idString, String tokenString){
        ArrayList<TimelinePhoto> timelinePhotoArrayList = new ArrayList<>();
        HttpURLConnection connection;
        try{
            URL url = createURL(idString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(null, "Timeline Response: " + response + " token: "+ tokenString);
            if (response == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(null, "doInBackground: " + builder.toString());
                timelinePhotoArrayList = convertJSONToArrayList(new JSONArray(builder.toString()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timelinePhotoArrayList;
    }

    private URL createURL(String idString) {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/"+idString+"/timeline";
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<TimelinePhoto> convertJSONToArrayList (JSONArray photoJSON){

        ArrayList<TimelinePhoto> arrayListTimelinePhoto = new ArrayList<>();

        try{
            JSONArray timeline = photoJSON;
            for (int i = 0; i < timeline.length(); i++){
                JSONObject dados = timeline.getJSONObject(i);
                String id = dados.getJSONObject("usuario").getString("id");
                String nome = dados.getJSONObject("usuario").getString("nome");
                String experiencia = dados.getJSONObject("usuario").getString("experiencia");
                String cidade = dados.getJSONObject("usuario").getString("cidade");
                String pais = dados.getJSONObject("usuario").getString("pais");
                String photoPerfil = dados.getJSONObject("usuario").getString("fotoPerfil");
                String local = dados.getJSONObject("desafio").getString("nome");//dados.getString("titulo");
                String photoTimeline = dados.getString("fotoUrl");
                String qtdLikes = dados.getString("curtidas");
                String qtdDislikes = dados.getString("descurtidas");
                String data = dados.getString("criadoEm");
                data = data.substring(0,9);
                Log.d(null, "data: " +data);
                String[] date = data.split("-");
                StringBuilder sb = new StringBuilder();
                sb.append(date[2]);
                sb.append("/" +date[1]);
                sb.append("/" + date[0]);
                arrayListTimelinePhoto.add(new TimelinePhoto(photoPerfil, nome, local, photoTimeline, qtdLikes, qtdDislikes, sb.toString(), id, experiencia, cidade, pais));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return arrayListTimelinePhoto;
    }
}
