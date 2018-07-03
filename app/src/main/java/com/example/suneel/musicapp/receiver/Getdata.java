package com.example.suneel.musicapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.SongPlay;

/**
 * Created by suneel on 17/5/18.
 */

public class Getdata extends BroadcastReceiver {
    public static final String ACTION_PLAY = "com.example.suneel.musicapp.Services.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.example.suneel.musicapp.Services.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.example.suneel.musicapp.Services.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.example.suneel.musicapp.Services.ACTION_NEXT";
    public static final String ACTION_CANCEL = "com.example.suneel.musicapp.Services.ACTION_CANCEL";
    public static final String RECEIVER = "com.example.suneel.musicapp.Services.RECEIVER";
    public static String actionValue = "";
  //  public static int position = 0;

    @Override
    public void onReceive(Context can, Intent intent) {
       // Intent intent1 = new Intent(can, MainActivity.class);
       // intent1.putExtra("value", intent.getAction());
        actionValue = intent.getAction();
        switch (intent.getAction()) {
            case ACTION_PLAY:
                MainActivity.instance.playpause();
                break;
            case ACTION_PAUSE:
                MainActivity.instance.playpause();
                break;
            case ACTION_NEXT:
                MainActivity.instance.nextSong();
                break;
            case ACTION_PREVIOUS:
                MainActivity.instance.previousSong();
                break;
            case ACTION_CANCEL:
                MainActivity.instance.stopSong();
                break;
           /* case RECEIVER:
                MainActivity.instance.updateui( intent.getIntExtra("progress",0),intent.getStringExtra("currentduration"),intent.getStringExtra("totalduration"));
                break;*/
        }


    }
}
