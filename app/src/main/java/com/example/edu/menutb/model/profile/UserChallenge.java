package com.example.edu.menutb.model.profile;

/**
 * Created by jeffkenichi on 10/6/17.
 */

public class UserChallenge {

    private String nome;
    private String local;
    private String data;
    private String fotoUrlPerfil;
    private String fotoUrlChallenge;
    private String like;
    private String dislike;

    public UserChallenge(String nome, String local, String data, String fotoUrlPerfil, String fotoUrlChallenge, String like, String dislike) {
        this.nome = nome;
        this.local = local;
        this.data = data;
        this.fotoUrlPerfil = fotoUrlPerfil;
        this.fotoUrlChallenge = fotoUrlChallenge;
        this.like = like;
        this.dislike = dislike;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFotoUrlPerfil() {
        return fotoUrlPerfil;
    }

    public void setFotoUrlPerfil(String fotoUrlPerfil) {
        this.fotoUrlPerfil = fotoUrlPerfil;
    }

    public String getFotoUrlChallenge() {
        return fotoUrlChallenge;
    }

    public void setFotoUrlChallenge(String fotoUrlChallenge) {
        this.fotoUrlChallenge = fotoUrlChallenge;
    }
}
