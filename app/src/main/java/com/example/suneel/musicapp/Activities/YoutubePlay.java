package com.example.suneel.musicapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Adapters.VideoAdapter;
import com.example.suneel.musicapp.models.Youtubemodel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;

/**
 * Created by suneel on 15/5/18.
 */

public class YoutubePlay extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private String videoid;
    private ArrayList<Youtubemodel> sList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubeplay);
        sList = (ArrayList<Youtubemodel>) getIntent().getSerializableExtra("videoList");
        videoid = getIntent().getStringExtra("videoid");
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        recyclerView = (RecyclerView) findViewById(R.id.videolist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        videoAdapter = new VideoAdapter(YoutubePlay.this, sList);
        recyclerView.setAdapter(videoAdapter);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoid);

            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(YoutubePlay.this, "Sorry failed", Toast.LENGTH_LONG).show();
            }
        };
        youTubePlayerView.initialize("AIzaSyAbmdMgrJYqzAQZ6k5rGFBjOjFc6BLG8c4", onInitializedListener);
    }


    public void videoSong(ArrayList<Youtubemodel> songlist, final String videoid) {
        Intent intent = new Intent(YoutubePlay.this, YoutubePlay.class);
        intent.putExtra("videoList", songlist);
        intent.putExtra("videoid", videoid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}

