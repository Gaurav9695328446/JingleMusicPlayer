package com.example.suneel.musicapp.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.suneel.musicapp.Adapters.VoiceAdapter;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Adapters.SongAdapter;
import com.example.suneel.musicapp.models.SongModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 31/3/18.
 */

public class VoiceSearchClass extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST = 1;
    List<SongModel> sList;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    VoiceAdapter adapter;
    SearchView searchView;
    private boolean checkstate = false;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int count = 0;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicelayout);
        recyclerView = (RecyclerView) findViewById(R.id.voiceview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sList = new ArrayList<>();
        String voice = getIntent().getStringExtra("voice").toString().trim();
        // Toast.makeText(this,voice,Toast.LENGTH_SHORT).show();
        dostuff();
        searchSong(voice);

    }

    private void dostuff() {
        getMusic();
        adapter = new VoiceAdapter(this, sList, checkstate);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    private void searchSong(String voice) {


    }

    public void getMusic() {

        sList = new ArrayList<>();
        sList = new splashscreen().musicList;


    }

    public void open(SongModel smodel, int position) {
        for (int i = 0; i < sList.size(); i++) {
            if (smodel.getTitle() == sList.get(i).getTitle()) {
                position = i;
            }
        }
        Intent intent = new Intent(this, SongPlay.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

}

