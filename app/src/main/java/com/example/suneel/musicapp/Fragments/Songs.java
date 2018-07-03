package com.example.suneel.musicapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.SongPlay;
import com.example.suneel.musicapp.Activities.splashscreen;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Adapters.SongAdapter;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 27/3/18.
 */

public class Songs extends BaseFragment {

    private static final int MY_PERMISSION_REQUEST = 1;
    ArrayList<SongModel> sList;
    ArrayList<SongModel> dblist;
    RecyclerView recyclerView;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    SongAdapter adapter;
    SearchView searchView;
    private boolean checkstate;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String playlistname;
    public final String MY_PREFS = "MY_PREFS";
    Getmusic help;
    SQLiteDatabase db;
    private boolean isLoading = true;
    private int pastVisibleItems, VisibleItemCount, total_itemCount, Prevvious_total;
    private int view_threshold = 6;
    private LinearLayoutManager layout;
    private int OFFSET = 0;
    private int LIMIT = 6;

    public Songs() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs, container, false);

        // Inflate the layout for this fragment
        layout = new LinearLayoutManager(getContext());
        help = new Getmusic(getContext());
        db = help.getWritableDatabase();
        pastVisibleItems = 0;
        VisibleItemCount = 0;
        total_itemCount = 0;
        Prevvious_total = 0;
        OFFSET = 0;
        preferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        if (savedInstanceState != null) {
            playlistname = savedInstanceState.getString("name");
            checkstate = savedInstanceState.getBoolean("state", false);
        } else {
            Intent i = getActivity().getIntent();
            checkstate = i.getBooleanExtra("state", false);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layout);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                VisibleItemCount = layout.getChildCount();
                total_itemCount = layout.getItemCount();
                pastVisibleItems = layout.findFirstVisibleItemPosition();

                if (isLoading) {
                    if (total_itemCount > Prevvious_total) {
                        isLoading = false;
                        Prevvious_total = total_itemCount;

                    }
                }
                if (!isLoading && (total_itemCount - VisibleItemCount) <= (pastVisibleItems + view_threshold)) {
                    isLoading = true;
                    performPagination();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {

            doStuff();
        }
        return view;
    }

    private void performPagination() {
        List<SongModel> smodel = databasemusic(LIMIT, OFFSET);
        adapter.addSongs(smodel);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                        doStuff();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Permission Granted", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            }
            return;
        }
    }

    public ArrayList<SongModel> databasemusic(int limit, int offset) {
        sList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME + " LIMIT " + limit + " OFFSET " + offset;

        Log.e("QUERY_____", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
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
                    sList.add(new SongModel(id, title, location, uri, bitmap, artistid, artist, albumid, album, genres, false));
                } while (cursor.moveToNext());
            }
            OFFSET = OFFSET + LIMIT;
            cursor.close();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return sList;
    }

    private void doStuff() {
        sList = databasemusic(LIMIT, OFFSET);
        adapter = new SongAdapter(getActivity(), sList, this);
        recyclerView.setAdapter(adapter);
        //setting adapter to recyclerview
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_sliding_down_animation));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.getAdapter().notifyDataSetChanged();

    }

    public void notifyDb(int position) {
      adapter.removeSongs(position);
    }
}
