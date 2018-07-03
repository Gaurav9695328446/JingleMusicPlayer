package com.example.suneel.musicapp.models;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by suneel on 1/6/18.
 */

public class SongDataModel {
    public static final String TABLE_NAME = "Songdatatable";
    public static final String SONG_NAME = "song_name";
    public static final String SONG_ARTIST = "song_artist";
    public static final String SONG_LOCATION = "song_location";
    public static final String SONG_URI = "song_uri";
    public static final String SONG_IMAGE = "song_image";
    public static final String CURRENT_SONG_DURATION = "song_current";
    public static final String TOTAL_SONG_DURATION = "song_total";
    public static final String CATEGORY_TYPE="category_type";
    public static final String CATEGORY_NAME="category_name";

    private String id;
    private String title;
    private String artist;
    private String location;
    private Uri uri;
    private Bitmap image;
    private String currentsongduration;
    private String totalsongduration;
    private String categorytype;
    private String categoryname;
    private int status;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + SONG_NAME + " TEXT NOT NULL ,"
                    + SONG_ARTIST + " TEXT NOT NULL  ,"
                    + SONG_LOCATION + " TEXT NOT NULL ,"
                    + SONG_URI + " TEXT NOT NULL ,"
                    + SONG_IMAGE + " BLOB ,"
                    + CURRENT_SONG_DURATION + " TEXT NOT NULL ,"
                    + TOTAL_SONG_DURATION + " TEXT NOT NULL ,"
                    + CATEGORY_TYPE + " TEXT ,"
                    + CATEGORY_NAME + " TEXT "
                    + ");";



    public SongDataModel(int status, String id, String title, String artist, String location, Uri uri, Bitmap image, String currentsongduration, String totalsongduration, String categorytype, String categoryname) {
        this.status=status;
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.location = location;
        this.uri = uri;
        this.image = image;
        this.currentsongduration = currentsongduration;
        this.totalsongduration = totalsongduration;
        this.categorytype = categorytype;
        this.categoryname = categoryname;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getCurrentsongduration() {
        return currentsongduration;
    }

    public void setCurrentsongduration(String currentsongduration) {
        this.currentsongduration = currentsongduration;
    }

    public String getTotalsongduration() {
        return totalsongduration;
    }

    public void setTotalsongduration(String totalsongduration) {
        this.totalsongduration = totalsongduration;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

}
