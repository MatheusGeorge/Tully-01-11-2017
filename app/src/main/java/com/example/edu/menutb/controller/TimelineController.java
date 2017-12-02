package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.asynchronous.timeline.AsyncTimelineTask;
import com.example.edu.menutb.model.asynchronous.timeline.AvaliacaoEnum;
import com.example.edu.menutb.model.timeline.TimelinePhoto;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/27/17.
 */

public class TimelineController {

    public ArrayList<TimelinePhoto> getTimeline(String idString, String tokenString){
        return new AsyncTimelineTask().getTimeline(idString, tokenString);
    }

    public String evaluatePhotoPerfil(String idDesafio, String idString, String tokenString, String tipo, String verbo){
        StringBuilder url = new StringBuilder();
        url.append("https://tully-api.herokuapp.com");

        switch (verbo){
            case "POST":
                url.append("/api/fotos/");
                url.append(idDesafio);
                url.append("/avaliacoes");
                break;
            case "PATCH": case "DELETE":
                url.append("/api/avaliacoes/");
                url.append(idDesafio);
                break;
        }
        return new AsyncTimelineTask().evaluatePhotoTimline(idDesafio, idString, tokenString, tipo, url.toString(), verbo);
    }
}
