package com.example.edu.menutb.controller;

import com.example.edu.menutb.model.asynchronous.notification.AsyncNotificationTask;
import com.example.edu.menutb.model.notificacao.Notifications;

import java.util.ArrayList;

/**
 * Created by jeffkenichi on 12/10/17.
 */

public class NotificationController {
    public ArrayList<Notifications> loadNotifications(String idString, String tokenString){
        ArrayList<Notifications> notificationsArrayList = new ArrayList<>();
        notificationsArrayList = new AsyncNotificationTask().loadNotifications(idString, tokenString);
        return notificationsArrayList;
    }
}
