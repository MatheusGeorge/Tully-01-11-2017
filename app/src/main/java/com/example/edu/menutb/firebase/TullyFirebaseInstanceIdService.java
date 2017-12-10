package com.example.edu.menutb.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.edu.menutb.model.timeline.TimelinePhoto;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeffkenichi on 11/8/17.
 * Metodo para gravar o device ID
 */

public class TullyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "TullyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        HttpURLConnection connection;
        SharedPreferences idBusca = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        String idString = idBusca.getString("id", "");
        SharedPreferences tokenS = this.getSharedPreferences("token", Context.MODE_PRIVATE);
        String tokenString = tokenS.getString("token", "");
        if(!tokenString.isEmpty()){
            try {
                URL urlConnection = new URL("https://tully-api.herokuapp.com/api/usuarios/" + idString);
                Log.d(null, "SendRegistrationToServer URL: " + urlConnection);

                connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setRequestMethod("PATCH");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "bearer " + tokenString);
                connection.connect();

                JSONArray jsonArray = new JSONArray();
                JSONObject body = new JSONObject();
                body.put("op", "replace");
                body.put("path", "/deviceId");
                body.put("value", token);

                jsonArray.put(body);

                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(jsonArray.toString());
                out.close();

                int responseHttp = connection.getResponseCode();
                Log.d(null, "SendRegistrationToServer refreshed token response: " + responseHttp);

                if (responseHttp == 204) {

                }else{
                    StringBuilder builder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(null, "sendRegistrationToServer: " + builder.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
