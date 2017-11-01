package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.UserTully;
import com.example.edu.menutb.model.asynchronous.search.AsyncSearchUsers;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 10/13/17.
 * Classe criada para fazer o controle dos search
 */

public class SearchController {

    public ArrayList<UserTully> searchUsersWithFilter(String idString, String tokenString, String mInputText){
        ArrayList<UserTully> userTullyArrayList = new AsyncSearchUsers().searchUsers(tokenString);
        ArrayList<UserTully> userTullyArrayListSearch = new ArrayList<>();

        if (userTullyArrayList.size() == 0) {
            return userTullyArrayList;
        } else {
            for (UserTully userTully : userTullyArrayList) {
                if (userTully.getUserName().contains(mInputText) && !userTully.getId().equals(idString))
                    userTullyArrayListSearch.add(userTully);
            }
            return  userTullyArrayListSearch;
        }
    }

    public ArrayList<UserTully> searchUsersNoFilter(String tokenString, String idString, String finalContext){
        return new AsyncSearchUsers().searchUsers(tokenString, idString, finalContext);
    }

    public String isFollow(String idString, String idSeguindo, String tokenString){
        int isFollow = new AsyncSearchUsers().isFollow(idString, idSeguindo, tokenString);
        return isFollow==200?"true":"false";
    }
}
