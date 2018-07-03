package com.example.suneel.musicapp.models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by suneel on 3/4/18.
 */

public class PlayListStore {

    public static final String TABLE_NAME = "Playlisttable";

    public static final String PLAYLIST_ID = "playlist_name";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_ARTIST = "song_artist";
    public static final String SONG_LOCATION = "song_location";
    public static final String SONG_URI = "song_uri";
    public static final String SONG_IMAGE = "song_image";

    private String id;
    private String title;
    private String artist;
    private String location;
    private Uri uri;
    private Bitmap image;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + PLAYLIST_ID + " TEXT NOT NULL ,"
                    + SONG_NAME + " TEXT NOT NULL ,"
                    + SONG_ARTIST + " TEXT NOT NULL  ,"
                    + SONG_LOCATION + " TEXT NOT NULL ,"
                    + SONG_URI + " TEXT NOT NULL ,"
                    + SONG_IMAGE + " BLOB "
                    + ");";


    public PlayListStore(String id, String title, String artist, String location, Uri uri, Bitmap image) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.location = location;
        this.uri = uri;
        this.image = image;
    }

    public PlayListStore() {
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

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

}
