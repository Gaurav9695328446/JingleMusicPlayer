package com.example.suneel.musicapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.PlayListStore;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class splashscreen extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000;
    public Getmusic helper;
    public SQLiteDatabase db;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    private ProgressBar progress;
    public List<SongModel> musicList = new ArrayList<>();
    private List<SongModel> dblist = new ArrayList<>();
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private Bitmap compressedImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        progress = (ProgressBar) findViewById(R.id.progress);
        helper = new Getmusic(this);
        db = helper.getReadableDatabase();
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            new Handler().postDelayed(new Runnable() {

			/**
             Showing splash screen with a timer. This will be useful when you
                want to show case your app logo / company*/

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    new PlaylistData().execute();
                   /* getMusicData();
                    databasemusic();
                    checkData();*/

                    // close this activity
                  /*  finish();*/
                }
            }, SPLASH_TIME_OUT);
        }

    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void insertSong(int value) {
        // get writable database as we want to write data
        Getmusic help = new Getmusic(this);
        SQLiteDatabase db = help.getWritableDatabase();
        int countsongs = 0;
        for (int i = (value - 1); i < musicList.size(); i++) {
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(SongList.SONG_ID, musicList.get(i).getId());
            values.put(SongList.SONG_NAME, musicList.get(i).getTitle());
            values.put(SongList.SONG_LOCATION, musicList.get(i).getLocation());
            values.put(SongList.SONG_URI, String.valueOf(musicList.get(i).getUri()));
            values.put(SongList.SONG_IMAGE, getBitmapAsByteArray(musicList.get(i).getImage()));
            values.put(SongList.ARTIST_ID, musicList.get(i).getArtistid());
            values.put(SongList.ARTIST, musicList.get(i).getArtist());
            values.put(SongList.ALBUM_ID, musicList.get(i).getAlbumid());
            values.put(SongList.ALBUM, musicList.get(i).getAlbum());
            values.put(SongList.GENRES, musicList.get(i).getGenres());
            // insert row
            db.insert(SongList.TABLE_NAME, null, values);
            countsongs++;
        }
        // close db connection
        db.close();
    }

    public void insertSong() {
        // get writable database as we want to write data
        Getmusic help = new Getmusic(this);
        SQLiteDatabase db = help.getWritableDatabase();
        for (int i = 0; i < musicList.size(); i++) {
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(SongList.SONG_ID, musicList.get(i).getId());
            values.put(SongList.SONG_NAME, musicList.get(i).getTitle());
            values.put(SongList.SONG_LOCATION, musicList.get(i).getLocation());
            values.put(SongList.SONG_URI, String.valueOf(musicList.get(i).getUri()));
            values.put(SongList.SONG_IMAGE, getBitmapAsByteArray(musicList.get(i).getImage()));
            values.put(SongList.ARTIST_ID, musicList.get(i).getArtistid());
            values.put(SongList.ARTIST, musicList.get(i).getArtist());
            values.put(SongList.ALBUM_ID, musicList.get(i).getAlbumid());
            values.put(SongList.ALBUM, musicList.get(i).getAlbum());
            values.put(SongList.GENRES, musicList.get(i).getGenres());
            // insert row
            db.insert(SongList.TABLE_NAME, null, values);
        }
        // close db connection
        db.close();
    }

    public void checkData() {
        if (musicList.size() > dblist.size()) {
            if (dblist.size() == 0) {
                insertSong();
                transferActivity();
            } else {
                insertSong(dblist.size());
                transferActivity();
            }
        } else {
            transferActivity();
        }
    }

    private void transferActivity() {
        Intent intent = new Intent(splashscreen.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private byte[] getBitmapAsByteArray(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void getMusicData() {
        if (musicList.size() > 0)
            musicList.clear();
        try {
            ContentResolver cr = getContentResolver();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor songCursor = cr.query(uri, null, null, null, null);
            if (uri != null && songCursor.moveToFirst() /*&& genresCursor.moveToFirst()*/) {
                int songid = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int Artist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int Artistid = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int Albumid = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                do {
                    String currentid = songCursor.getString(songid);
                    String currentTitle = songCursor.getString(songTitle).replace("_", " ");
                    String currentLocation = songCursor.getString(songLocation);
                    Uri uriSongs = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", new File(currentLocation));
                    String currentArtistid = songCursor.getString(Artistid);
                    String currentAlbumid = songCursor.getString(Albumid);
                    metaRetriver = new MediaMetadataRetriever();
                    metaRetriver.setDataSource(currentLocation);
                    String currentAlbum = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    if (currentAlbum == null) {
                        currentAlbum = "Download";
                    }
                    String currentGenres = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    if (currentGenres == null) {
                        currentGenres = "Other";
                    }
                    String currentArtist = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    if (currentArtist == null) {
                        currentArtist = "Unknown Artist";
                    }
                    try {
                        art = metaRetriver.getEmbeddedPicture();
                        if (art == null) {
                           /* String filePath = SiliCompressor.with(this).compress(R.mipmap.song);*/
                            songImage = BitmapFactory.decodeResource(this.getResources(),
                                    R.mipmap.song);
                            //songImage = SiliCompressor.with(this).getCompressBitmap(filePath);

                        } else {
                            songImage = BitmapFactory
                                    .decodeByteArray(art, 0, art.length);
                           // songImage = SiliCompressor.with(this).getCompressBitmap(String.valueOf(art));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    musicList.add(new SongModel(currentid, currentTitle, currentLocation, uriSongs, songImage, currentArtistid, currentArtist, currentAlbumid, currentAlbum, currentGenres, false));
                } while (songCursor.moveToNext());
                musicList.size();
            }
        } catch (IllegalArgumentException e) {
        }
    }

    public void databasemusic() {
        if (dblist.size() > 0)
            dblist.clear();
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME;
        Log.e("QUERY_____", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor == null)
                        Toast.makeText(splashscreen.this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
                        // looping through all rows and adding to list
                    else {
                        if (cursor.moveToFirst()) {
                            do {
                                String id = cursor.getString(cursor.getColumnIndex(SongList.SONG_ID));
                                String title = cursor.getString(cursor.getColumnIndex(SongList.SONG_NAME));
                                String location = cursor.getString(cursor.getColumnIndex(SongList.SONG_LOCATION));
                                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(SongList.SONG_URI)));
                                byte[] blob = cursor.getBlob(cursor.getColumnIndex(SongList.SONG_IMAGE));
                                ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                String artistid = cursor.getString(cursor.getColumnIndex(SongList.ARTIST_ID));
                                String artist = cursor.getString(cursor.getColumnIndex(SongList.ARTIST));
                                String albumid = cursor.getString(cursor.getColumnIndex(SongList.ALBUM_ID));
                                String album = cursor.getString(cursor.getColumnIndex(SongList.ALBUM));
                                String genres = cursor.getString(cursor.getColumnIndex(SongList.GENRES));
                                dblist.add(new SongModel(id, title, location, uri, bitmap, artistid, artist, albumid, album, genres, false));
                            } while (cursor.moveToNext());
                        }
                        // close db connection
                        //
                    }
                } while (cursor.moveToNext());
                dblist.size();
                db.close();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            getMusicData();
                            databasemusic();
                            checkData();
                            startActivity(intent);
                            finish();
                        }
                    }).run();
                } else {
                    Toast.makeText(splashscreen.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class PlaylistData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setIndeterminate(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getMusicData();
            databasemusic();
            checkData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setIndeterminate(false);
            Intent i = new Intent(splashscreen.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(i);
        }
    }
}
