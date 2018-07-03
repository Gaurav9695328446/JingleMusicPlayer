package com.example.suneel.musicapp.Fragments;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.AlbumItem;
import com.example.suneel.musicapp.Activities.ArtistItem;
import com.example.suneel.musicapp.Activities.GenresItem;
import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.SongPlay;
import com.example.suneel.musicapp.Activities.splashscreen;
import com.example.suneel.musicapp.Adapters.AlbumAdapter;
import com.example.suneel.musicapp.Adapters.GridAdapter;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 27/3/18.
 */

public class Artist extends BaseFragment {

    RecyclerView recyclerView;
    GridAdapter adapter;
    List<SongModel> sList;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    Getmusic help;
    SQLiteDatabase db;
    private boolean isLoading = true;
    private int pastVisibleItems, VisibleItemCount, total_itemCount, Prevvious_total;
    private int view_threshold = 10;
    private int OFFSET = 0;
    private int LIMIT = 10;
    private GridLayoutManager layout;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.artist, container, false);
        layout = new GridLayoutManager(getContext(), 2);
        help = new Getmusic(getContext());
        db = help.getWritableDatabase();
        pastVisibleItems = 0;
        VisibleItemCount = 0;
        total_itemCount = 0;
        Prevvious_total = 0;
        OFFSET = 0;
        sList = databasemusic(LIMIT, OFFSET);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layout);
        adapter = new GridAdapter(getContext(), sList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_sliding_down_animation));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.getAdapter().notifyDataSetChanged();
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
                    performPagination();
                    isLoading = true;
                }
            }
          /*  }*/
        });
        return view;
    }

    private void performPagination() {
        List<SongModel> smodel = databasemusic(LIMIT, OFFSET);
        adapter.addSongs(smodel);
    }

    public List<SongModel> databasemusic(int limit, int offset) {
        sList = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT artist" +
                " FROM " + SongList.TABLE_NAME + " LIMIT " + limit + " OFFSET " + offset;

        Log.e("QUERY_____", selectQuery);
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String genres = cursor.getString(cursor.getColumnIndex(SongList.ARTIST));
                    Bitmap image = albumImage(genres);
                    sList.add(new SongModel(genres, image));
                } while (cursor.moveToNext());
            }
            OFFSET = OFFSET + LIMIT;

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        // return sList;
        return sList;
    }

    public Bitmap albumImage(String s) {
        ByteArrayInputStream inputStream = null;
        Bitmap bitmap = null;
        String selectQuery = "SELECT song_image FROM " + SongList.TABLE_NAME + " WHERE artist ='" + s + "'";
        Log.e("QUERY_____", selectQuery);
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                byte[] blob = cursor.getBlob(cursor.getColumnIndex(SongList.SONG_IMAGE));
                inputStream = new ByteArrayInputStream(blob);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    public void setSongList(int position) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("value", position);
        startActivity(intent);
    }

    public void setGenresList(String genres) {
        Intent intent = new Intent(getContext(), ArtistItem.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("album", genres);
        startActivity(intent);
    }
}
