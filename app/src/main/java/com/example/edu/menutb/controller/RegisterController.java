package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.asynchronous.register.AsyncRegisterTask;

/**
 * Created by jeffkenichi on 10/24/17.
 */

public class RegisterController {

    public int verifyUserOrEmailExists(String username, String email){
        String response = new AsyncRegisterTask().verifyUserOrEmailExists(username, email);
        if(response.contains("Username"))
            return 1;
        else if(response.contains("Email"))
            return 2;
        else
            return 0;
    }
}
