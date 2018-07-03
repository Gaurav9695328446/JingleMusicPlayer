package com.example.suneel.musicapp.Activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Services.MusicService;
import com.example.suneel.musicapp.models.PlayListStore;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;
import com.example.suneel.musicapp.Utils.Utilities;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.itangqi.waveloadingview.WaveLoadingView;

import static java.lang.Thread.sleep;

/**
 * Created by suneel on 13/3/18.
 */

public class SongPlay extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    TextView stitle, sartist, starttime, endtime;
    CircleImageView simage;
    Button play, backbtn, fwdbtn, btnRepeat, btnShuffle;
    SeekBar updater;
    Handler hand;
    Utilities utils;
    DatabaseHelper helper;
    private int position;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    public static SongPlay instance;
    public Context context;
    private MusicService player;
    boolean serviceBound = false;
    private String s;
    private String category;
    private ArrayList<SongModel> sList;
    private WaveLoadingView waveLoadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songplay);
        if (getIntent() != null) {
            category=getIntent().getStringExtra("category");
            s = getIntent().getStringExtra("name");
            position = getIntent().getIntExtra("value", 0);
        }
        instance = this;
        context = MainActivity.getActivityContext();
        sList = new ArrayList<>();
        waveLoadingView = (WaveLoadingView) findViewById(R.id.waveloading);
        updater = (SeekBar) findViewById(R.id.Songrun);
        stitle = (TextView) findViewById(R.id.dname);
        sartist = (TextView) findViewById(R.id.dartist);
        simage = (CircleImageView) findViewById(R.id.SongImage);
        starttime = (TextView) findViewById(R.id.start);
        backbtn = (Button) findViewById(R.id.BackBtn);
        fwdbtn = (Button) findViewById(R.id.FwdBtn);
        endtime = (TextView) findViewById(R.id.end);
        play = (Button) findViewById(R.id.PlayBtn);
        updater.setOnSeekBarChangeListener(this);
        play.setBackgroundResource(R.drawable.ic_pause);
        btnRepeat = (Button) findViewById(R.id.Repeat);
        btnShuffle = (Button) findViewById(R.id.Shuffle);
       /* btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                player.getRepeat();
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                }
            }*/
//        });


       /* btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                player.getShuffle();
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                }
            }
        });*/


        fwdbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                nextSong();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                playpause();
            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        backbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                previousSong();
            }
        });
    }

    private void getPlaylistItems(String s) {
        sList = new ArrayList<>();
        helper = new DatabaseHelper(this);
        // Select All Query
        String selectQuery = "SELECT * FROM " + PlayListStore.TABLE_NAME + " WHERE " +
                PlayListStore.PLAYLIST_ID + "='" + s + "'";
        Log.e("QUERY_____", selectQuery);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(5);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    sList.add(new SongModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), Uri.parse(cursor.getString(4)), bitmap));
                } while (cursor.moveToNext());
            }
            // close db connection
            sList.size();
        }
    }

    public void getPlaylist(ArrayList<SongModel> sList, int location) {
        this.sList = sList;
        this.position = location;
        player.setPlaylist(sList, position);
    }

    public void previousSong() {
        player.getPreviousSong();
        setSongView();

    }

    public void nextSong() {
        player.getNextSong();
        setSongView();
    }


    public void playpause() {
        if (player.mp.isPlaying()) {
            if (player.mp != null) {
                player.getPause();
                // Changing button image to play button
                play.setBackgroundResource(R.drawable.ic_play);
            }
        } else {
            // Resume song
            if (player.mp != null) {
                player.getPlay();
                // Changing button image to pause button
                play.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }

    public void stopSong(){
        if(player.mp!=null){
            player.stopSong();
            play.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        position = getIntent().getIntExtra("position", position);
       // sList = getIntent().getParcelableArrayListExtra("list");
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtra("value", position);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
            if (s != null) {
                if(category!=null){
                    getCategorydata(category,s);
                    getPlaylist(sList, position);
                }
                else {
                    getPlaylistItems(s);
                    getPlaylist(sList, position);
                }
            }
            songPlay();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void getCategorydata(String category,String name) {
        sList = new ArrayList<>();
        Getmusic helper = new Getmusic(this);
        // Select All Query
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME + " WHERE " +
                category + "='" + name + "'";
        Log.e("QUERY_____", selectQuery);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(4);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    sList.add(new SongModel(cursor.getString(1), s, cursor.getString(2), Uri.parse(cursor.getString(3)), bitmap));
                } while (cursor.moveToNext());
            }
            // close db connection
            sList.size();
        }
    }

    private void setSongView() {
        stitle.setText(player.sList.get(player.position).getTitle());
        sartist.setText(player.sList.get(player.position).getArtist());
        simage.setImageBitmap(player.sList.get(player.position).getImage());
        ((MainActivity) context).setdataToView(player.sList.get(player.position).getTitle(), player.sList.get(player.position).getImage());

    }

    private void songPlay() {
        utils = new Utilities();
        hand = new Handler();
        try {
            // set Progress bar values
            updater.setProgress(0);
            waveLoadingView.setProgressValue(0);
            updater.setMax(100);
            player.getStart(position
            );
            setSongView();
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void updateProgressBar() {
        hand.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = player.getDuration();
                long currentDuration = player.getCurrentPosition();
                // Displaying Total Duration time
                endtime.setText("" + utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                starttime.setText("" + utils.milliSecondsToTimer(currentDuration));
                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                updater.setProgress(progress);
                waveLoadingView.setProgressValue(progress);
                // Running this thread after 100 milliseconds
                hand.postDelayed(this, 100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        hand.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        hand.removeCallbacks(mUpdateTimeTask);
        int totalDuration = (int) player.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
        // forward or backward to certain seconds
       // player.getSeekBardata(currentPosition);
        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onClick(View view) {

    }
}









