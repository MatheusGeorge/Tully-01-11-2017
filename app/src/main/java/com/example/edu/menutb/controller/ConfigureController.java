package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.asynchronous.configure.AsyncInativateAccount;

/**
 * Created by jeffkenichi on 10/15/17.
 * Classe para o controle da view Configure
 */

public class ConfigureController {

    public void inativaAccount(String idString, String tokenString){
        new AsyncInativateAccount().inativateAccount(idString, tokenString);
    }
}
