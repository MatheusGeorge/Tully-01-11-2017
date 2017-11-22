package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.asynchronous.ranking.AsyncRankingTask;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 11/22/17.
 */

public class RankingController {

    public ArrayList<UserTully> getRanking(String tokenString){
        return new AsyncRankingTask().getRanking(tokenString);
    }
}
