package com.example.edu.menutb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.edu.menutb.data.TullyDatabaseContract.UsuarioEntry;

/**
 * Created by matheus on 11/09/2017.
 */

public class TullyOpenHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Tully.db";
    public static final int DATABASE_VERSION = 1;
    public TullyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsuarioEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
