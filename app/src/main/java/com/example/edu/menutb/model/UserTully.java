package com.example.edu.menutb.model;

/**
 * Created by jeffkenichi on 8/24/17.
 */

public class UserTully {

    private String token;
    private String name;
    private String id;
    private String userName;
    private String email;
    private String experiencia;
    private String cidade;
    private String estado;
    private String pais;
    private String foto_url;

    public UserTully(){

    }

    public UserTully(String id,String name, String userName, String foto_url, String experiencia, String cidade, String pais){
        this.id = id;
        this.name = name;
        this.userName = userName;
        this.foto_url = foto_url;
        this.experiencia = experiencia;
        this.cidade = cidade;
        this.pais = pais;
    }

    public UserTully(String token, String name, String id) {
        this.token = token;
        this.name = name;
        this.id =id;
    }

    public UserTully(String token, String name, String id, String userName) {
        this.token = token;
        this.name = name;
        this.id = id;
        this.userName = userName;
    }



    public UserTully(String token, String name, String id, String userName, String email, String experiencia, String cidade, String pais, String foto_url) {
        this.token = token;
        this.name = name;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.experiencia = experiencia;
        this.cidade = cidade;;
        this.pais = pais;
        this.foto_url = foto_url;
    }

    public UserTully(String id, String name, String userName, String fotoPerfil, boolean b) {
    }


    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
