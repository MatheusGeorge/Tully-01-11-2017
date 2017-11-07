package com.example.edu.menutb.model.asynchronous.challenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.edu.menutb.model.asynchronous.MultpartUtility;
import com.example.edu.menutb.model.photo.BitmapResizer;

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
 * Created by jeffkenichi on 10/17/17.
 */

public class AsyncPostChallengePhoto {

    public String postImageToFirebase(String imagePath){
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

            String url = "https://tully-api.herokuapp.com/api/files/fotos";

            multpartUtility = new MultpartUtility(url, f.getName());
            multpartUtility.addFilePart("image", f);
            JSONObject object = multpartUtility.finish();

            response = object.getString("fileUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public void postChallengePhoto(String idString, String tokenString, String urlFoto, String idDesafio){
        HttpURLConnection httpCon = null;
        Log.d(null, "[DEBUG] Salvando a foto do desafio na API");
        try {
            URL url = new URL("https://tully-api.herokuapp.com/api/usuarios/" + idString + "/fotos");

            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Authorization", "bearer " + tokenString);
            httpCon.connect();

            JSONObject body = new JSONObject();
            body.put("usuarioId", idString);
            body.put("desafioId", idDesafio);
            body.put("fotoUrl", urlFoto);

            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write(body.toString());
            out.close();

            int httpResult = httpCon.getResponseCode();
            Log.d(null, "[DEBUG] HTTP Response do post para salvar a foto do desafio: " + httpResult);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
