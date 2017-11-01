package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.asynchronous.timeline.AsyncTimelineTask;
import com.example.edu.menutb.model.timeline.TimelinePhoto;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/27/17.
 */

public class TimelineController {

    public ArrayList<TimelinePhoto> getTimeline(String idString, String tokenString){
        return new AsyncTimelineTask().getTimeline(idString, tokenString);
    }
}
