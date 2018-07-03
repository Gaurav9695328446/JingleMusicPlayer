package com.example.suneel.musicapp.models;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by suneel on 13/3/18.
 */

public class SongModel implements Parcelable {
    private String id;
    private String title;
    private String location;
    private Uri uri;
    private Bitmap image;
    private String artist;
    private String artistkey;
    private String artistid;
    private String album;
    private String albumid;
    private String albumkey;
    private String genres;
    private String genresid;

    public SongModel(String string, Uri parse, Bitmap bitmap) {
        this.title = string;
        this.uri = parse;
        this.image = bitmap;
    }

    protected SongModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        location = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        image = in.readParcelable(Bitmap.class.getClassLoader());
        artist = in.readString();
        artistkey = in.readString();
        artistid = in.readString();
        album = in.readString();
        albumid = in.readString();
        albumkey = in.readString();
        genres = in.readString();
        genresid = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        @Override
        public SongModel createFromParcel(Parcel in) {
            return new SongModel(in);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArtistkey() {
        return artistkey;
    }

    public void setArtistkey(String artistkey) {
        this.artistkey = artistkey;
    }

    public String getArtistid() {
        return artistid;
    }

    public void setArtistid(String artistid) {
        this.artistid = artistid;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getAlbumkey() {
        return albumkey;
    }

    public void setAlbumkey(String albumkey) {
        this.albumkey = albumkey;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getGenresid() {
        return genresid;
    }

    public void setGenresid(String genresid) {
        this.genresid = genresid;
    }

    public SongModel(String id, String title, String location, Uri uri, Bitmap image, String artistid, String artist, String albumid, String album, String genres, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.uri = uri;
        this.image = image;
        this.artist = artist;
        this.artistid = artistid;
        this.album = album;
        this.albumid = albumid;
        this.genres = genres;
        this.isSelected = isSelected;
    }

    public SongModel(String categorytitle, Bitmap image) {
        this.title = categorytitle;
        this.image = image;
    }


    private boolean isSelected;

    public SongModel(Bitmap songImage, String currentArtist, String currentLocation, Uri uriSongs) {
        this.image = songImage;
        this.artist = currentArtist;
        this.location = currentLocation;
        this.uri = uriSongs;
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

    public SongModel(String title, String artist, String location, Uri uri, Bitmap image, boolean state) {
        this.image = image;
        this.title = title;
        this.artist = artist;
        this.location = location;
        this.uri = uri;
        this.isSelected = state;
    }

    public SongModel(String title, String artist, String location, Uri uri, Bitmap image) {
        this.image = image;
        this.title = title;
        this.artist = artist;
        this.location = location;
        this.uri = uri;
    }

    public SongModel(String title, String artist, String location, Bitmap image) {
        this.image = image;
        this.title = title;
        this.artist = artist;
        this.location = location;
    }

    public SongModel(String title, String artist, Uri uri, Bitmap image) {
        this.image = image;
        this.title = title;
        this.artist = artist;
        this.uri = uri;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(location);
        parcel.writeParcelable(uri, i);
        parcel.writeParcelable(image, i);
        parcel.writeString(artist);
        parcel.writeString(artistkey);
        parcel.writeString(artistid);
        parcel.writeString(album);
        parcel.writeString(albumid);
        parcel.writeString(albumkey);
        parcel.writeString(genres);
        parcel.writeString(genresid);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}