package com.example.suneel.musicapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.suneel.musicapp.models.SongList;

/**
 * Created by suneel on 17/5/18.
 */

public class Getmusic extends SQLiteOpenHelper {
    private Context context;
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "jingle_db";


    public Getmusic(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SongList.CREATE_TABLE);
        Log.e("DATA_______________", SongList.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SongList.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }
}
