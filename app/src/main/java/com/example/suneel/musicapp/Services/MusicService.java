package com.example.suneel.musicapp.Services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.SongPlay;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Utils.Utilities;
import com.example.suneel.musicapp.models.SongModel;
import com.example.suneel.musicapp.receiver.Getdata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by suneel on 13/4/18.
 */

public class MusicService extends Service{
    public Getmusic helper;
    public SQLiteDatabase db;
    Bitmap bmp;
    MediaMetadataRetriever metaRetriver;
    public MediaPlayer mp=new MediaPlayer();
    public ArrayList<SongModel> sList ;
    byte[] art;
    Bitmap songImage;
    public int position;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private static final int NOTIFICATION_ID = 101;
    private final IBinder binder = new LocalBinder();
    private NotificationCompat.Builder notificationBuilder;
    public static final String ACTION_PLAY = "com.example.suneel.musicapp.Services.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.suneel.musicapp.Services.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.example.suneel.musicapp.Services.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.example.suneel.musicapp.Services.ACTION_NEXT";
    public static final String ACTION_CANCEL = "com.example.suneel.musicapp.Services.ACTION_CANCEL";
    public static final String CHANNEL_ID = "com.example.suneel.musicapp.Services.Notification";
    public static String str_receiver = "com.example.suneel.musicapp.Services.RECEIVER";
    public static final String CHANNEL_NAME = "NOTIFICATION CHANNEL";
    private PendingIntent pendingIntent;
    public PlaybackStatus playbackStatus = PlaybackStatus.PAUSED;
    private NotificationManager manager;
    private String category;
    private String playlistname;
    Utilities utils;
    public int progress;
    Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(this,Getdata.class);
        intent.setAction(str_receiver);
        this.sList=new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        helper = new Getmusic(this);
        db = helper.getReadableDatabase();
        return MusicService.START_STICKY;

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this,"service desttoyed",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setPlaylist(ArrayList<SongModel> sList, int location) {
        this.sList = sList;
        this.position = location;
        if (sList.size() > 0)
            getStart(position);
    }

    public ArrayList<SongModel> getPlaylistData(){
        return this.sList;
    }
    public int getPosition(){
        return this.position;
    }

    public int position(){
       return this.position;
    }

    public ArrayList<SongModel> songList(){
        return this.sList;
    }


    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public void getStart(final int pos) {
        try {
            if (sList.size() > 0) {
                buildNotification();
                mp.reset();
                mp.setDataSource(getApplicationContext(), sList.get(pos).getUri());
                mp.prepare();
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (isRepeat) {
                            // repeat is on play same song again
                            getStart(position);
                            if(MainActivity.instance!=null){
                                MainActivity.instance.getPlaylist(sList,position);
                            }
                        } else if (isShuffle) {
                            // shuffle is on - play a random song
                            Random rand = new Random();
                            position = rand.nextInt((sList.size() - 1) - 0 + 1) + 0;
                            getStart(position);
                            playbackStatus = PlaybackStatus.PLAYING;
                            buildNotification();
                            if(MainActivity.instance!=null){
                                MainActivity.instance.getPlaylist(sList,position);
                            }
                        } else {
                            // no repeat or shuffle ON - play next song
                            if (position < (sList.size() - 1)) {
                                getStart(position + 1);
                                position = position + 1;
                                playbackStatus = PlaybackStatus.PLAYING;
                                buildNotification();
                                if(MainActivity.instance!=null){
                                    MainActivity.instance.getPlaylist(sList,position);
                                }
                            } else {
                                // play first song
                                getStart(0);
                                position = 0;
                                playbackStatus = PlaybackStatus.PLAYING;
                                buildNotification();
                                if(MainActivity.instance!=null){
                                    MainActivity.instance.getPlaylist(sList,position);
                                }
                            }
                        }
                    }
                    });
                }
            } catch(IllegalArgumentException e){
                e.printStackTrace();
            } catch(IllegalStateException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();

        }
    }

    public int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    public long getDuration() {
       return mp.getDuration();
    }

    public long getCurrentPosition() {
       return mp.getCurrentPosition();
    }

    public int progress(){
        return (int)(progressToTimer(mp.getCurrentPosition(), mp.getDuration()));
    }

    public int progressdata(){
        return (int) (getProgressPercentage(mp.getCurrentPosition(), mp.getDuration()));
    }

    public void getRepeat(boolean isRepeat) {
        if (isRepeat) {
            this.isRepeat = true;
        } else {
            // make repeat to true
            this.isRepeat = false;
            // make shuffle to false
           /* this.isShuffle = false;*/
        }
    }

    public void getShuffle(boolean isShuffle) {
        if (isShuffle) {
            this.isShuffle = true;
        } else {
            // make repeat to true
            this.isShuffle = false;
            // make shuffle to false
           /* this.isRepeat = false;*/
        }
    }

    public void getNextSong() {
        if (isRepeat) {
            // repeat is on play same song again
            getStart(position);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            position = rand.nextInt((sList.size() - 1) - 0 + 1) + 0;
            getStart(position);
            playbackStatus = PlaybackStatus.PLAYING;
            buildNotification();
        } else {
            // no repeat or shuffle ON - play next song
            if (position < (sList.size() - 1)) {
                getStart(position + 1);
                position = position + 1;
                playbackStatus = PlaybackStatus.PLAYING;
                buildNotification();
            } else {
                // play first song
                getStart(0);
                position = 0;
                playbackStatus = PlaybackStatus.PLAYING;
                buildNotification();
            }
        }
    }

    public void getPreviousSong() {
        if (isRepeat) {
            // repeat is on play same song again
            getStart(position);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            position = rand.nextInt((sList.size() - 1) - 0 + 1) + 0;
            getStart(position);
            playbackStatus = PlaybackStatus.PLAYING;
            buildNotification();
        } else {
            // no repeat or shuffle ON - play next song
            if (position > 0) {
                getStart(position - 1);
                position = position - 1;
                playbackStatus = PlaybackStatus.PLAYING;
                buildNotification();
            } else {
                // play last song
                getStart(sList.size() - 1);
                position = sList.size() - 1;
                playbackStatus = PlaybackStatus.PLAYING;
                buildNotification();
            }
        }

    }


    public void getPlay() {
        if (mp != null) {
            mp.start();
            // Changing button image to play button
            playbackStatus = PlaybackStatus.PLAYING;
            buildNotification();
        }
    }

    public void getPause() {
        if (mp != null) {
            mp.pause();
            // Changing button image to play button
            buildNotification();
        }
    }

    public void stopSong() {
        if (mp != null) {
            mp.pause();
            // Changing button image to play button
        }
    }


    public void resetSong()
    {
        if(mp!=null)
            mp.reset();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void buildNotification() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            // Call some material design APIs here
            // Notification.Builder builder = notificationHelper.getJingleNotification(sList.get(position).getTitle(), sList.get(position).getArtist(),sList.get(position).getImage());
            NotificationChannel Chan1 = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            Chan1.setLightColor(Color.GREEN);
            Chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(Chan1);
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText(sList.get(position).getArtist())
                    .setContentTitle(sList.get(position).getTitle())
                    .setLargeIcon(sList.get(position).getImage())
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    .setAutoCancel(true);
            Intent notificationIntent = new Intent(this, SongPlay.class);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Intent previous = new Intent();
            previous.setAction(ACTION_PREVIOUS);
            PendingIntent pendingprevious = playbackAction(3);
            builder.addAction(R.drawable.ic__arrow_left_black_24dp, "", pendingprevious);

            if (playbackStatus == PlaybackStatus.PLAYING) {
                Intent yesReceive = new Intent();
                yesReceive.setAction(ACTION_PLAY);
                PendingIntent pendingIntentYes = playbackAction(0);
                builder.addAction(R.drawable.ic_pause_circle_outline_black_24dp, "", pendingIntentYes);
            } else if (playbackStatus == PlaybackStatus.PAUSED) {
                Intent maybeReceive = new Intent();
                maybeReceive.setAction(ACTION_PAUSE);
                PendingIntent pendingIntentMaybe = playbackAction(1);
                builder.addAction(R.drawable.ic_play_circle_outline_black_24dp, "", pendingIntentMaybe);
            }

            Intent next = new Intent();
            next.setAction(ACTION_NEXT);
            PendingIntent pendingnext = playbackAction(2);
            builder.addAction(R.drawable.ic__arrow_right_black_24dp, "", pendingnext);
            //getManager().notify(new Random().nextInt(), builder.build());
            getManager().notify(NOTIFICATION_ID,builder.build());
        } else {
            // Implement this feature without material design
            // Create a new Notification
            if (sList.size()>0){
                Intent myIntent = new Intent(this, MainActivity.class);
            myIntent.putExtra("requestcode", 1);
            myIntent.putExtra("value",position);
            @SuppressLint("WrongConstant") PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    myIntent,
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setShowWhen(false)
                    // Set the Notification style
                    // Set the Notification color
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    // Set the large and small icons
                    .setLargeIcon(sList.get(position).getImage())
                    .setSmallIcon(android.R.drawable.stat_sys_headset)
                    // Set Notification content information
                    .setContentText(sList.get(position).getTitle())
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setContentInfo(sList.get(position).getArtist())
                    .setContentIntent(pendingIntent);
            Intent cancel = new Intent();
            cancel.setAction(ACTION_CANCEL);
            PendingIntent cancelIntent = playbackAction(5);
            notificationBuilder.setDeleteIntent(cancelIntent);

            Intent previous = new Intent();
            previous.setAction(ACTION_PREVIOUS);
            PendingIntent pendingprevious = playbackAction(3);
            notificationBuilder.addAction(R.drawable.ic__arrow_left_black_24dp, "", pendingprevious);

            if (playbackStatus == PlaybackStatus.PAUSED) {
                playbackStatus = PlaybackStatus.PLAYING;
                Intent yesReceive = new Intent();
                yesReceive.setAction(ACTION_PAUSE);
                PendingIntent pendingIntentYes = playbackAction(0);
                notificationBuilder.addAction(R.drawable.ic_play_circle_outline_black_24dp, "", pendingIntentYes);

            } else{
                playbackStatus = PlaybackStatus.PAUSED;
                Intent maybeReceive = new Intent();
                maybeReceive.setAction(ACTION_PLAY);
                PendingIntent pendingIntentMaybe = playbackAction(1);
                notificationBuilder.addAction(R.drawable.ic_pause_circle_outline_black_24dp, "", pendingIntentMaybe);
            }

            Intent next = new Intent();
            next.setAction(ACTION_NEXT);
            PendingIntent pendingnext = playbackAction(2);
            notificationBuilder.addAction(R.drawable.ic__arrow_right_black_24dp, "", pendingnext);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        }
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent intent = new Intent(this, Getdata.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        switch (actionNumber) {
            case 0:
                // Play
                intent.setAction(ACTION_PLAY);
                break;
            case 1://Pause
                intent.setAction(ACTION_PAUSE);
                break;
            case 2: //next
                intent.setAction(ACTION_NEXT);
                break;
            case 3://previous
                intent.setAction(ACTION_PREVIOUS);
                break;
            case 5://cancel
                intent.setAction(ACTION_CANCEL);
        }
        return PendingIntent.getBroadcast(this, 12345, intent, 0);
    }

    public NotificationManager getManager() {
        if (manager == null)
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }
}



