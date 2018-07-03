package com.example.suneel.musicapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.suneel.musicapp.models.SongDataModel;
import com.example.suneel.musicapp.models.SongList;

import java.io.File;

/**
 * Created by suneel on 1/6/18.
 */

public class GetSongData extends SQLiteOpenHelper {
    private Context context;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "songdb";


    public GetSongData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       /* super(context, Environment.getExternalStorageDirectory()
                + File.separator
                + File.separator + DATABASE_NAME, null, DATABASE_VERSION);*/
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SongDataModel.CREATE_TABLE);
        Log.e("DATA_______________", SongDataModel.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SongDataModel.TABLE_NAME);
        // Create tables again
        onCreate(db);

    }
}
