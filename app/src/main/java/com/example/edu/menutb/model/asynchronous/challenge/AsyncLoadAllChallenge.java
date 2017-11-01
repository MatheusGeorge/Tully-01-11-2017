package com.example.edu.menutb.model.asynchronous.challenge;

import android.util.Log;

import com.example.edu.menutb.model.challenge.Challenge;
import com.example.edu.menutb.model.profile.UserChallenge;

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
 * Created by jeffkenichi on 10/17/17.
 */

public class AsyncLoadAllChallenge {

    public ArrayList<Challenge> loadAllChallenges(String tokenString){
        URL url = createUrl();
        HttpURLConnection connection = null;
        ArrayList<Challenge> arrayListChallenges = new ArrayList<>();
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
                arrayListChallenges = convertJSONToArrayList(new JSONArray(builder.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return arrayListChallenges;
    }

    private URL createUrl() {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/desafios";
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Challenge> convertJSONToArrayList(JSONArray challengeJSONArray) {
        ArrayList<Challenge> arrayListChallenge = new ArrayList<>();

        try {
            JSONArray challengeArray = challengeJSONArray;
            for (int i = 0; i < challengeArray.length(); i++) {
                JSONObject dados = challengeArray.getJSONObject(i);
                String id = dados.getString("id");
                String nome = dados.getString("nome");
                String telefone = dados.getString("telefone");
                String endereco = dados.getString("endereco");
                String latitude = dados.getString("latitude");
                String longitude = dados.getString("longitude");
                String cidade = dados.getString("cidade");
                String estado = dados.getString("estado");
                String pais = dados.getString("pais");
                String site = dados.getString("url");
                String descricao = dados.getString("descricao");
                String foto = dados.getString("foto");
                String realizado = dados.getString("realizado");

                arrayListChallenge.add(new Challenge(id, nome, telefone, endereco, latitude, longitude, cidade,
                        estado, pais, site, descricao, foto, realizado));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayListChallenge;
    }
}
