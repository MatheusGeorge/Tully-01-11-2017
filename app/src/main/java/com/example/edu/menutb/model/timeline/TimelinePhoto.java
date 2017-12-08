package com.example.edu.menutb.model.timeline;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by zBrito on 25/07/2017.
 */

public class TimelinePhoto{

    private String photoPerfil;
    private String name;
    private String location;
    private String photoTimeline;
    private String like;
    private String dislike;
    private String date;
    private ArrayList<String> comments;
    private String id;
    private String experiencia;
    private String cidade;
    private String pais;
    private String level;
    private Bitmap fotoPerfilBitmap;
    private Bitmap fotoTimelineBitmap;
    private String type;
    private String idAvaliacao;

    public String getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(String idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public TimelinePhoto(String type, String verbo){
        this.type = type;
    }

    public TimelinePhoto(String idAvaliacao,String type, String like, String dislike) {
        this.like = like;
        this.dislike = dislike;
        this.type = type;
        this.idAvaliacao = idAvaliacao;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Bitmap getFotoPerfilBitmap() {
        return fotoPerfilBitmap;
    }

    public void setFotoPerfilBitmap(Bitmap fotoPerfilBitmap) {
        this.fotoPerfilBitmap = fotoPerfilBitmap;
    }

    public Bitmap getFotoTimelineBitmap() {
        return fotoTimelineBitmap;
    }

    public void setFotoTimelineBitmap(Bitmap fotoTimelineBitmap) {
        this.fotoTimelineBitmap = fotoTimelineBitmap;
    }

    public TimelinePhoto(String photoPerfil, String name, String location, String photoTimeline, String like, String userComment, String date, ArrayList<String> comments) {
        this.photoPerfil = photoPerfil;
        this.name = name;
        this.location = location;
        this.photoTimeline = photoTimeline;
        this.like = like;
        this.dislike = userComment;
        this.date = date;
        this.comments = comments;
    }

    public TimelinePhoto(String photoPerfil, String name, String location, String photoTimeline, String like, String dislike, String date,  String cidade, String level) {
        this.photoPerfil = photoPerfil;
        this.name = name;
        this.location = location;
        this.photoTimeline = photoTimeline;
        this.like = like;
        this.dislike = dislike;
        this.date = date;
        this.level = level;
    }

    public TimelinePhoto(String photoPerfil, String name, String location, String photoTimeline, String like, String dislike, String date, String id, String experiencia, String cidade, String pais) {
        this.photoPerfil = photoPerfil;
        this.name = name;
        this.location = location;
        this.photoTimeline = photoTimeline;
        this.like = like;
        this.dislike = dislike;
        this.date = date;
        this.id = id;
        this.experiencia = experiencia;
        this.cidade = cidade;
        this.pais = pais;
    }
    public TimelinePhoto(String photoPerfil, String name, String location, String photoTimeline, String like, String dislike, String date, String id, String experiencia, String cidade, String pais, String type) {
        this.photoPerfil = photoPerfil;
        this.name = name;
        this.location = location;
        this.photoTimeline = photoTimeline;
        this.like = like;
        this.dislike = dislike;
        this.date = date;
        this.id = id;
        this.experiencia = experiencia;
        this.cidade = cidade;
        this.pais = pais;
        this.type = type;
    }


    @Override
    public String toString() {
        return "TimelinePhoto{" +
                "photoPerfil='" + photoPerfil + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", photoTimeline='" + photoTimeline + '\'' +
                ", like='" + like + '\'' +
                ", dislikes='" + dislike + '\'' +
                ", date=" + date +
                ", comments=" + comments +
                '}';
    }

    public String getPhotoPerfil() {
        return photoPerfil;
    }

    public void setPhotoPerfil(String photoPerfil) {
        this.photoPerfil = photoPerfil;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoTimeline() {
        return photoTimeline;
    }

    public void setPhotoTimeline(String photoTimeline) {
        this.photoTimeline = photoTimeline;
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

    public void setUserComment(String userComment) {
        this.dislike = userComment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
