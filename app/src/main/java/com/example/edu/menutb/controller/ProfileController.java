package com.example.edu.menutb.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.edu.menutb.dao.ProfileDAO;
import com.example.edu.menutb.model.asynchronous.profile.AsyncAddFollow;
import com.example.edu.menutb.model.asynchronous.profile.AsyncChangePictureProfile;
import com.example.edu.menutb.model.asynchronous.profile.AsyncLoadAccomplishedChallenge;
import com.example.edu.menutb.model.asynchronous.profile.AsyncLoadProfileImage;
import com.example.edu.menutb.model.asynchronous.profile.AsyncRemoveFollow;
import com.example.edu.menutb.model.profile.UserChallenge;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/13/17.
 * Classe para o controle do profile
 */

public class ProfileController {

    public void changePicture(String tokenString, String idString, String urlFoto) {
        new AsyncChangePictureProfile(tokenString, idString, urlFoto).asyncChangePicture();
    }

    public String postToFirebase(String imagePath) {
        return new AsyncChangePictureProfile().asyncPostImageToFirebase(imagePath);
    }

    public void updatePictureProfile(String idString, Context context, String urlFoto) {
        new ProfileDAO(idString, context).updatePhotoPerfil(urlFoto);
    }

    public String[] selectProfileToMenu(String idString, Context context){
        return new ProfileDAO(idString, context).selectProfileToMenu();
    }

    public Bitmap loadProfileImage(String urlFoto) {
        String urlPhoto;
        urlPhoto = urlFoto.contains("default") ? "http://tully-api.herokuapp.com/" + urlFoto : urlFoto;
        return new AsyncLoadProfileImage().loadProfileImage(urlPhoto);
    }

    public String[] loadSelectProfile(String idString, Context context) {
        String informations[] = new ProfileDAO(idString, context).selectProfile();
        String level = "";
        int experienciaInteiro = Integer.parseInt(informations[4]);
        if (experienciaInteiro < 20) {
            level = "1 (0)";
            informations[4] = (String.valueOf(experienciaInteiro) + "/20");
        } else if (experienciaInteiro < 40) {
            level = "2 (20)";
            informations[4] = (String.valueOf(experienciaInteiro - 20) + "/40");
        } else if (experienciaInteiro < 80) {
            level = "3 (40)";
            informations[4] = (String.valueOf(experienciaInteiro - 40) + "/80");
        }
        informations[5] = level;
        return informations;
    }

    public ArrayList<UserChallenge> loadChallengeAccomplish(String tokenString, String idString){
        return new AsyncLoadAccomplishedChallenge().loadAccomplishedChallenge(tokenString, idString);
    }


    public int addFollow(String idUsuario, String idSeguindo, String tokenString){
        return new AsyncAddFollow().addFollow(idUsuario, idSeguindo, tokenString);
    }

    public Boolean isSameId(String idUsuario, String idAnother){
        Log.d(null, "isSameId: " + idUsuario +" = "+idAnother);
        return idUsuario.equals(idAnother);
    }

    public int deleteFollow(String idUsuario, String idSeguindo, String tokenString){
        return new AsyncRemoveFollow().removeFollow(idUsuario, idSeguindo, tokenString);
    }
}
