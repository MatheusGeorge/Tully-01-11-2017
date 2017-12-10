package com.example.edu.menutb.model.asynchronous.timeline;

import android.util.Log;

import com.example.edu.menutb.model.timeline.TimelinePhoto;

import org.json.JSONArray;
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
 * Created by jeffkenichi on 10/27/17.
 */

public class AsyncTimelineTask {

    public ArrayList<TimelinePhoto> getTimeline(String idString, String tokenString) {
        ArrayList<TimelinePhoto> timelinePhotoArrayList = new ArrayList<>();
        HttpURLConnection connection;
        try {
            URL url = createURL(idString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(null, "Timeline Response: " + response + " token: " + tokenString);
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
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + idString + "/timeline";
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<TimelinePhoto> convertJSONToArrayList(JSONArray photoJSON) {

        ArrayList<TimelinePhoto> arrayListTimelinePhoto = new ArrayList<>();

        try {
            JSONArray timeline = photoJSON;
            for (int i = 0; i < timeline.length(); i++) {
                JSONObject dados = timeline.getJSONObject(i);
                String id = dados.getString("id");
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
                String type = "none";
                String idAvaliacao = "";
                try {
                    JSONObject object = dados.getJSONObject("avaliacao");
                    if (object != null) {
                        Log.d(null, "objeto nao nulo");
                        idAvaliacao = object.getString("id");
                        type = object.getString("tipo");
                    } else {
                        type = "none";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                data = data.substring(0, 10);
                Log.d(null, "data: " + data);
                String[] date = data.split("-");
                StringBuilder sb = new StringBuilder();
                sb.append(date[2]);
                sb.append("/" + date[1]);
                sb.append("/" + date[0]);
                arrayListTimelinePhoto.add(new TimelinePhoto(photoPerfil, nome, local,
                        photoTimeline, qtdLikes, qtdDislikes, sb.toString(), id,
                        experiencia, cidade, pais, type, idAvaliacao));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayListTimelinePhoto;
    }

    public TimelinePhoto evaluatePhotoTimline(String idDesafio, String idString, String tokenString, String tipo, String url, String verbo) {
        HttpURLConnection connection;
        try {
            URL urlConnection = new URL(url.toString());
            Log.d(null, "Avaliacao URL: " + url);

            connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setRequestMethod(verbo.toUpperCase());

            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            if (!verbo.toString().equalsIgnoreCase("delete")) {
                Log.d(null, "diferente de delete");
                connection.setRequestProperty("Content-Type", "application/json");
            }
            connection.connect();

            JSONObject body = new JSONObject();

            if (verbo.toString().equalsIgnoreCase("POST")) {
                Log.d(null, "evaluatePhotoTimline: Entrou no post");

                body.put("tipo", tipo);
                body.put("usuarioId", idString);
                body.put("fotoId", idDesafio);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(body.toString());
                Log.d(null, "BODY: " + body.toString());
                out.close();
            } else if (verbo.toString().equalsIgnoreCase("PATCH")) {
                Log.d(null, "evaluatePhotoTimline: Entrou no patch");
                body.put("tipo", tipo);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(body.toString());
                out.close();
            }

            int responseHttp = connection.getResponseCode();
            Log.d(null, "Avaliacao Response: " + responseHttp);

            if (responseHttp == 201) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(null, "Avaliacao " + builder.toString());
                return convertJSONToObject(new JSONObject(builder.toString()));
            } else if (responseHttp == 200) {
                return new TimelinePhoto(tipo, "DELETE");
            } else if (responseHttp == 204) {
                return new TimelinePhoto(tipo, "PATCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TimelinePhoto evaluatePhotoTimline(String tokenString, String tipo, String url, String verbo) {
        HttpURLConnection connection;
        try {
            URL urlConnection = new URL(url.toString());
            Log.d(null, "Avaliacao URL: " + url);

            connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setRequestMethod(verbo.toUpperCase());
            if (verbo.equalsIgnoreCase("patch"))
                connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);
            connection.connect();

            JSONObject body = new JSONObject();
            if (verbo.equalsIgnoreCase("patch")) {
                body.put("tipo", tipo);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(body.toString());
                out.close();
            }

            int responseHttp = connection.getResponseCode();
            Log.d(null, "Avaliacao Response: " + responseHttp);

            if (responseHttp == 200) {
                return new TimelinePhoto(tipo, "DELETE");
            } else if (responseHttp == 204) {
                return new TimelinePhoto(tipo, "PATCH");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private TimelinePhoto convertJSONToObject(JSONObject photoJSON) {

        ArrayList<TimelinePhoto> arrayListTimelinePhoto = new ArrayList<>();
        TimelinePhoto timelinePhoto = null;

        try {
            JSONObject dados = photoJSON;
            String id = dados.getString("id");
            Log.d(null, "SALVANDO ID: " + id);
            String qtdLikes = dados.getJSONObject("foto").getString("curtidas");
            String qtdDislikes = dados.getJSONObject("foto").getString("descurtidas");
            String type = dados.getString("tipo");
            timelinePhoto = new TimelinePhoto(id, qtdLikes, qtdDislikes, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timelinePhoto;
    }


}
