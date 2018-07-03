package com.example.suneel.musicapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.suneel.musicapp.models.PlayListStore;

/**
 * Created by suneel on 3/4/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "playlist_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(PlayListStore.CREATE_TABLE);
        Log.e("DATA_______________", PlayListStore.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + PlayListStore.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }
}
