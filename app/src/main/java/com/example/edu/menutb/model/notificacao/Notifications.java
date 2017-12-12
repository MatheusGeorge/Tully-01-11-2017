package com.example.edu.menutb.model.notificacao;

import com.example.edu.menutb.model.UserTully;

/**
 * Created by jeffkenichi on 12/10/17.
 */

public class Notifications {


    //Para seguir
    private String idUsuario;
    private String urlFotoPerfilUsuario;
    private String nome;
    private String experiencia;
    private String cidade;
    //Para avaliacao
    private String urlFotoTimeline;
    private String fotoLike;
    private String fotoDislike;
    //pros dois
    private String texto;


    public Notifications(String idUsuario, String texto, String urlFotoPerfilUsuario, String nome, String experiencia, String cidade) {
        this.idUsuario = idUsuario;
        this.texto = texto;
        this.urlFotoPerfilUsuario = urlFotoPerfilUsuario;
        this.nome = nome;
        this.experiencia = experiencia;
        this.cidade = cidade;
    }

    public Notifications(String urlFotoTimeline, String fotoLike, String fotoDislike, String texto) {
        this.urlFotoTimeline = urlFotoTimeline;
        this.fotoLike = fotoLike;
        this.fotoDislike = fotoDislike;
        this.texto = texto;
    }

    public Notifications(String texto, String name, String cidadePais,String fotoUrl,String fotoPerfil){
        this.urlFotoTimeline = fotoUrl;
        this.cidade = cidadePais;
        this.nome = name;
        this.texto = texto;
        this.urlFotoPerfilUsuario = fotoPerfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getUrlFotoTimeline() {
        return urlFotoTimeline;
    }

    public void setUrlFotoTimeline(String urlFotoTimeline) {
        this.urlFotoTimeline = urlFotoTimeline;
    }

    public String getFotoLike() {
        return fotoLike;
    }

    public void setFotoLike(String fotoLike) {
        this.fotoLike = fotoLike;
    }

    public String getFotoDislike() {
        return fotoDislike;
    }

    public void setFotoDislike(String fotoDislike) {
        this.fotoDislike = fotoDislike;
    }

    public Notifications(){};

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrlFotoPerfilUsuario() {
        return urlFotoPerfilUsuario;
    }

    public void setUrlFotoPerfilUsuario(String urlFotoPerfilUsuario) {
        this.urlFotoPerfilUsuario = urlFotoPerfilUsuario;
    }


}
