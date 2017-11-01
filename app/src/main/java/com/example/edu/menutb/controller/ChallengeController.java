package com.example.edu.menutb.controller;

import android.graphics.Bitmap;

import com.example.edu.menutb.model.asynchronous.challenge.AsyncLoadAllChallenge;
import com.example.edu.menutb.model.asynchronous.challenge.AsyncLoadPhotoChallenge;
import com.example.edu.menutb.model.asynchronous.challenge.AsyncPostChallengePhoto;
import com.example.edu.menutb.model.challenge.Challenge;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/15/17.
 * Classe controladora do desafio
 */

public class ChallengeController {

    public Bitmap loadPhotoChallenge(String urlFoto){
        String urlPhoto;
        if(urlFoto.contains("default")){
            urlPhoto = "http://tully-api.herokuapp.com/" + urlFoto;
        } else {
            urlPhoto = urlFoto;
        }
        return new AsyncLoadPhotoChallenge().loadPhotoChallenge(urlPhoto);
    }

    public void postPhotoChallenge(String idString, String tokenString, String urlFoto, String idDesafio){
        new AsyncPostChallengePhoto().postChallengePhoto(idString, tokenString, urlFoto, idDesafio);
    }

    public String postPhotoToFirebase(String imagePath){
        return new AsyncPostChallengePhoto().postImageToFirebase(imagePath);
    }

    public ArrayList<Challenge> loadAllChallenges(String tokenString){
        return new AsyncLoadAllChallenge().loadAllChallenges(tokenString);
    }

    public String convertDistanceToString(float distance){
        String result = "";
        if(distance >= 100000){
            result = String.valueOf((int) distance/1000) + " km";
        }
        else if(distance >= 1000){
            result = String.format("%.2f",(distance/1000)) + " km";
        } else {
            result = String.valueOf((int)distance) + " m";
        }
        return result;
    }

    public Boolean outOfRange(float distance){
        if(distance >= 20000)
            return true;
        return false;
    }
}
