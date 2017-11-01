package com.example.edu.menutb.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.edu.menutb.data.TullyDatabaseContract;
import com.example.edu.menutb.data.TullyOpenHelper;
import com.example.edu.menutb.model.UserTully;

import java.util.StringTokenizer;

/**
 * Created by jeffkenichi on 10/10/17.
 */

public class ProfileDAO {

    private String idString;
    private Context context;

    private TullyOpenHelper mDbOpenHelper;

    private String selection = TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_ID + " = ?";
    private String[] selectionArgs;

    private Cursor cursor;

    public ProfileDAO (String idString, Context context){
        this.idString = idString;
        this.context = context;
        mDbOpenHelper = new TullyOpenHelper(context);
        selectionArgs = new String[]{(idString)};
    }

    public ProfileDAO(Context context){
        mDbOpenHelper = new TullyOpenHelper(context);
    }

    public String updateExperience(int experience){
        ContentValues values = new ContentValues();

        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        String[] courseColumns = {TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP};
        cursor = db.query(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, courseColumns, selection, selectionArgs, null, null, null);
        int usuarioExperiencia = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP);
        cursor.moveToLast();
        int usuarioXP = Integer.parseInt(cursor.getString(usuarioExperiencia));

        db = mDbOpenHelper.getWritableDatabase();
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP, usuarioXP + experience);
        db.update(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, values, selection, selectionArgs);
        return null;
    }

    public String[] selectProfile(){
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        String[] courseColumns = {
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_NOME,
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_CIDADE,
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_PAIS,
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL,
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP
        };
        cursor = db.query(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, courseColumns, selection, selectionArgs, null, null, null);


        int usuarioNamePos = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_NOME);
        int usuarioCityPos = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_CIDADE);
        int usuarioCountryPos = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_PAIS);
        int usuarioFotoUrl = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL);
        int usuarioExperiencia = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP);
        cursor.moveToLast();
        String usuarioName = cursor.getString(usuarioNamePos);
        String usuarioCity = cursor.getString(usuarioCityPos);
        String usuarioCountry = cursor.getString(usuarioCountryPos);
        String usuarioFoto = cursor.getString(usuarioFotoUrl);
        String usuarioXP = cursor.getString(usuarioExperiencia);

        return new String[] {usuarioName, usuarioCity, usuarioCountry, usuarioFoto, usuarioXP, "0"};

    }

    public void updatePhotoPerfil(String urlFoto){
        ContentValues values = new ContentValues();
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL, urlFoto);
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.update(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public String[] selectProfileToMenu(){
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        String[] courseColumns = {
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_NOME,
                TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL
        };
        cursor = db.query(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, courseColumns, selection, selectionArgs, null, null, null);
        int usuarioFotoUrl = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL);
        int usuarioNamePos = cursor.getColumnIndex(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_NOME);
        cursor.moveToLast();
        String usuarioName = cursor.getString(usuarioNamePos);
        String usuarioFoto = cursor.getString(usuarioFotoUrl);
        return new String[] {usuarioName, usuarioFoto};
    }

    public void insertUserLogin(final String token, final String id, final String name, final String userName, final String email, final String foto_url,
                                final String experiencia, final String cidade, final String pais){

        ContentValues values = new ContentValues();
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_TOKEN, token);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_ID, id);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_NOME, name);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_LOGIN, userName);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_EMAIL, email);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_FOTO_URL, foto_url);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_XP, experiencia);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_CIDADE, cidade);
        values.put(TullyDatabaseContract.UsuarioEntry.COLUMN_USUARIO_PAIS, pais);
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        db.insert(TullyDatabaseContract.UsuarioEntry.TABLE_NAME, null, values);
    }
}
