package com.example.suneel.musicapp.models;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by suneel on 17/5/18.
 */

public class SongList {
    public static final String TABLE_NAME = "Songtable";

    public static final String SONG_ID = "song_id";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_LOCATION = "song_location";
    public static final String SONG_URI = "song_uri";
    public static final String SONG_IMAGE = "song_image";
    public static final String ARTIST_ID = "artist_id";
    public static final String ARTIST = "artist";
    public static final String ALBUM_ID = "album_id";
    public static final String ALBUM = "album";
    public static final String GENRES = "genres";

    private String id;
    private String title;
    private String artist;
    private String location;
    private Bitmap image;
    private File uri;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + SONG_ID + " TEXT NOT NULL ,"
                    + SONG_NAME + " TEXT NOT NULL ,"
                    + SONG_LOCATION + " TEXT NOT NULL ,"
                    + SONG_URI + " TEXT NOT NULL ,"
                    + SONG_IMAGE + " BLOB ,"
                    + ARTIST_ID + " TEXT NOT NULL ,"
                    + ARTIST + " TEXT NOT NULL ,"
                    + ALBUM_ID + " TEXT NOT NULL ,"
                    + ALBUM + " TEXT NOT NULL ,"
                    + GENRES + " TEXT NOT NULL ,"
                    + " PRIMARY KEY (" + SONG_NAME + ")"
                    + ");";

    public SongList(String id, String title, String artist, String location, Bitmap image, File uri) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.location = location;
        this.image = image;
        this.uri = uri;
    }

    public SongList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Bitmap getImage() {
        return image;
    }

    public File getUri() {
        return uri;
    }

    public void setUri(File uri) {
        this.uri = uri;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

}
