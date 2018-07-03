package com.example.suneel.musicapp.Fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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

import com.example.suneel.musicapp.Activities.SongPlay;
import com.example.suneel.musicapp.Activities.splashscreen;
import com.example.suneel.musicapp.Adapters.SearchAdapter;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 2/4/18.
 */

public class SearchFragment extends Fragment {
    private static final int MY_PERMISSION_REQUEST = 1;
    List<SongModel> sList;
    RecyclerView recyclerView;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    SearchAdapter adapter;
    SearchView searchView;
    private boolean checkstate;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int count = 0;
    Getmusic help;
    SQLiteDatabase db;
    public static int songcount = 10;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rsongView);
        Intent i = getActivity().getIntent();
        checkstate = i.getBooleanExtra("state", false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        //initializing the productlist
        help = new Getmusic(getContext());
        db = help.getWritableDatabase();
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
        // Inflate the layout for this fragment
    }

    public List<SongModel> databasemusic() {
        sList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SongList.TABLE_NAME;
        Log.e("QUERY_____", selectQuery);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor == null)
                        Toast.makeText(getContext(), cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
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
                                sList.add(new SongModel(id, title, location, uri, bitmap, artistid, artist, albumid, album, genres, false));
                            } while (cursor.moveToNext());
                        }
                        // close db connection
                        db.close();
                    }
                } while (cursor.moveToNext());
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return sList;
    }

    @Override
    public void onAttach(Context context) {
        getActivity().onAttachFragment(new SearchFragment());
        super.onAttach(context);
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

    private void doStuff() {
        sList = databasemusic();
        adapter = new SearchAdapter(getActivity(), sList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_sliding_down_animation));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.getAdapter().notifyDataSetChanged();
    }


    public void open(SongModel smodel, int position) {
        Intent intent = new Intent(getContext(), SongPlay.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        smodel.getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.putExtra("title", smodel.getTitle());
        intent.putExtra("artist", smodel.getArtist());
        intent.putExtra("location", smodel.getLocation());
        intent.putExtra("position", position);
        intent.putExtra("image", byteArray);
        startActivity(intent);

    }

}
