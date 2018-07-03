package com.example.suneel.musicapp.models;

import java.io.Serializable;

/**
 * Created by suneel on 15/5/18.
 */

public class Youtubemodel implements Serializable {

    private String urlImage;
    private String songid;
    private String playlistid;
    private String videoid;

    public Youtubemodel(String urlImage, String songid, String published_at, String videoid) {
        this.urlImage = urlImage;
        this.songid = songid;
        this.playlistid = published_at;
        this.videoid = videoid;
    }

    public Youtubemodel(String urlImage, String songid, String playlistid) {
        this.urlImage = urlImage;
        this.songid = songid;
        this.playlistid = playlistid;
        this.videoid = videoid;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getPublished_at() {
        return playlistid;
    }

    public void setPublished_at(String published_at) {
        this.playlistid = playlistid;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }
}
