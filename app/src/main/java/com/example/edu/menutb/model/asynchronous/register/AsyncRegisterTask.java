package com.example.edu.menutb.model.asynchronous.register;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeffkenichi on 10/24/17.
 * Classe para fazer os metodos asynchronous do registro
 */

public class AsyncRegisterTask {

    public String verifyUserOrEmailExists(String username, String email) {
        HttpURLConnection connection;

        String response = "";
        try {
            URL url = createURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("email", email);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body.toString());
            out.close();

            int httpResult = connection.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_BAD_REQUEST) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                response = convertJSONToString(new JSONObject(builder.toString()));
            } else if (httpResult == HttpURLConnection.HTTP_OK) {
                response = "200";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private String convertJSONToString(JSONObject userObject) {
        String message = "";
        try {
            message = userObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    private URL createURL() {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/usernames";
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
