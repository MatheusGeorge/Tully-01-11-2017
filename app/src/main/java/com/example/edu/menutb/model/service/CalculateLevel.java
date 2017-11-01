package com.example.edu.menutb.model.service;
/**
 * Created by jeffkenichi on 10/31/17.
 * */

public class CalculateLevel {

    public String calculateLevelToPerfil(String experiencia){
        int experienciaInteiro = Integer.parseInt(experiencia);
        String level="";

        if (experienciaInteiro < 20) {
            level = " 1";
        } else if (experienciaInteiro < 40) {
            level = " 2";
        } else if (experienciaInteiro < 80) {
            level = " 3";
        }
            return level;
    }
 }
