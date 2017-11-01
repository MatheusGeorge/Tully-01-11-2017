package com.example.edu.menutb.model.asynchronous.profile;

import android.util.Log;

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
 * Created by jeffkenichi on 10/15/17.
 * Classe para carregar desafios já feitos pelo usuário
 */

public class AsyncLoadAccomplishedChallenge {
    public ArrayList<UserChallenge> loadAccomplishedChallenge(String tokenString, String idString){
        URL url = createURL(idString);

        HttpURLConnection connection;
        try {
            assert url != null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "bearer " + tokenString);


            int response = connection.getResponseCode();
            Log.d(null, "doInBackground: RESPONSE" + response);
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
                Log.d(null, "STRINGBUILDER " + builder.toString());
                return convertJSONToArrayList(new JSONArray(builder.toString()));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private URL createURL(String idString) {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + idString +"/fotos";
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private ArrayList<UserChallenge> convertJSONToArrayList (JSONArray photoJSON){

        ArrayList<UserChallenge> arrayListChallenge = new ArrayList<>();

        try{
            for (int i = 0; i < photoJSON.length(); i++){
                JSONObject dados = photoJSON.getJSONObject(i);
                //experience = Integer.parseInt(dados.getJSONObject("usuario").getString("experiencia"));
                String photoPerfil = dados.getJSONObject("usuario").getString("fotoPerfil");
                String data = "11/10/2017";//dados.getString("dataPublicacao");
                String nome = dados.getJSONObject("usuario").getString("nome");
                String local = dados.getJSONObject("desafio").getString("nome");
                String photoChallenge = dados.getString("fotoUrl");
                String like =  "0";//dados.getJSONObject("desafio").getString("q tdLike");
                String dislike = "0"; //dados.getJSONObject("desafio").getString("qtdDislike");
                arrayListChallenge.add(new UserChallenge(nome, local, data, photoPerfil, photoChallenge, like, dislike));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return arrayListChallenge;
    }
}
