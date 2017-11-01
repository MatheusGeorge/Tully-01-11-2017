package com.example.edu.menutb.model.asynchronous.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.edu.menutb.model.asynchronous.MultpartUtility;
import com.example.edu.menutb.model.photo.BitmapResizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by jeffkenichi on 10/13/17.
 */

public class AsyncChangePictureProfile {

    private String tokenString;
    private String idString;
    private String urlFoto;

    public AsyncChangePictureProfile(){}

    public AsyncChangePictureProfile(String tokenString, String idString, String urlFoto){
        this.tokenString = tokenString;
        this.idString = idString;
        this.urlFoto = urlFoto;
    }

    public void asyncChangePicture(){
        URL url = createUrl();
        HttpURLConnection httpCon = null;
        try {
            httpCon = (HttpURLConnection) url.openConnection();

            httpCon.setRequestMethod("PATCH");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Authorization", "bearer " + tokenString);
            httpCon.connect();

            JSONArray jsonArray = new JSONArray();
            JSONObject body = new JSONObject();
            body.put("op", "replace");
            body.put("path", "/fotoPerfil");
            body.put("value", urlFoto);

            jsonArray.put(body);

            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(jsonArray.toString());
            out.close();

            int httpResult = httpCon.getResponseCode();
            if (httpResult == HttpURLConnection.HTTP_NO_CONTENT) {
                //Toast.makeText(getContext(), "Alterou a foto de perfil", Toast.LENGTH_SHORT).show();
            }
            Log.d(null, "doInBackground: " + httpResult);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private URL createUrl() {
        try {
            String urlString = "https://tully-api.herokuapp.com/api/usuarios/" + idString;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String asyncPostImageToFirebase(String imagePath){
        String response = "";
        OutputStream outputStream = null;
        MultpartUtility multpartUtility;
        try {

            Log.d(null, "Começou a transformar o bitmap");

            //Transformar bitmap em uma resolução e qualidade menor
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            outputStream = new FileOutputStream(imagePath);

            bitmap = new BitmapResizer().bitmapResizer(bitmap, 512, 512);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            File f = new File(imagePath);

            String url = "https://tully-api.herokuapp.com/api/files/foto_perfil";

            multpartUtility = new MultpartUtility(url, f.getName());
            multpartUtility.addFilePart("image", f);
            JSONObject object = multpartUtility.finish();

            response = object.getString("fileUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
