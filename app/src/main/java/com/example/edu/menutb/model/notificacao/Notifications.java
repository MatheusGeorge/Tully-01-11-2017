package com.example.edu.menutb.model.notificacao;

/**
 * Created by jeffkenichi on 12/10/17.
 */

public class Notifications {
    private String idUsuario;
    private String texto;
    private String urlFotoPerfilUsuario;


    public Notifications(String idUsuario, String texto, String urlFotoPerfilUsuario) {
        this.idUsuario = idUsuario;
        this.texto = texto;
        this.urlFotoPerfilUsuario = urlFotoPerfilUsuario;
    }

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
