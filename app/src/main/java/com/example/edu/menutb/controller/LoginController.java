package com.example.edu.menutb.controller;

import android.content.Context;

import com.example.edu.menutb.dao.ProfileDAO;
import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.asynchronous.login.AsyncLoginTask;

/**
 * Created by jeffkenichi on 10/20/17.
 */

public class LoginController {

    public Boolean isTokenValid(String tokenString){
        int response = new AsyncLoginTask().isTokenValid(tokenString);

        if(response == 200)
            return true;
        return false;
    }

    public UserTully attemptLogin(String username, String password){
        return new AsyncLoginTask().attemptLogin(username, password);
    }

    public void insertUserLogin(Context context, UserTully userTully){
        new ProfileDAO(context).insertUserLogin(userTully.getToken(), userTully.getId(), userTully.getName(), userTully.getUserName(), userTully.getEmail(), userTully.getFoto_url(),
                userTully.getExperiencia(), userTully.getCidade(), userTully.getPais());
    }
}
