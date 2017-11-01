package com.example.edu.menutb.data;

import android.provider.BaseColumns;

/**
 * Created by matheus on 11/09/2017.
 */

public final class TullyDatabaseContract {
    private TullyDatabaseContract() {}

    public static final class UsuarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_USUARIO_NOME = "usuario_nome";
        public static final String COLUMN_USUARIO_XP = "usuario_xp";
        public static final String COLUMN_USUARIO_CIDADE = "usuario_cidade";
        public static final String COLUMN_USUARIO_PAIS = "usuario_pais";
        public static final String COLUMN_USUARIO_EMAIL = "usuario_email";
        public static final String COLUMN_USUARIO_LOGIN = "usuario_login";
        public static final String COLUMN_USUARIO_FOTO_URL = "usuario_foto_url";
        public static final String COLUMN_USUARIO_TOKEN = "usuario_token";
        public static final String COLUMN_USUARIO_ID = "usuario_id";

        // CREATE TABLE usuario
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_USUARIO_NOME + " TEXT, " +
                        COLUMN_USUARIO_XP + " INTEGER, " +
                        COLUMN_USUARIO_CIDADE + " TEXT, " +
                        COLUMN_USUARIO_PAIS + " TEXT, " +
                        COLUMN_USUARIO_EMAIL + " TEXT, " +
                        COLUMN_USUARIO_LOGIN + " TEXT, " +
                        COLUMN_USUARIO_FOTO_URL + " TEXT, " +
                        COLUMN_USUARIO_ID + " INTEGER, " +
                        COLUMN_USUARIO_TOKEN + " TEXT)";
    }

}
