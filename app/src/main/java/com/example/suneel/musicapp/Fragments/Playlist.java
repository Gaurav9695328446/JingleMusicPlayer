package com.example.suneel.musicapp.Fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.suneel.musicapp.Adapters.PlaylistAdapter;
import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.models.PlayListStore;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 27/3/18.
 */

public class Playlist extends Fragment {
    private List<String> playlistItems;
    private RecyclerView recyclerView;
    PlaylistAdapter adapter;
    DatabaseHelper helper;
    SQLiteDatabase db;
    private List<String> countList;

    public Playlist() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helper = new DatabaseHelper(getContext());
        countList = new ArrayList<>();
        getAllNotes();
        return view;
        // Inflate the layout for this fragment
    }

    private void getAllNotes() {
        playlistItems = new ArrayList<>();
        // Select All Query
        try {
            String selectQuery = "SELECT DISTINCT "
                    + PlayListStore.PLAYLIST_ID + " FROM " + PlayListStore.TABLE_NAME /*+ " ORDER BY " +
                    PlayListStore.PLAYLIST_ID + " DESC "*/;
            Log.e("QUERY_____", selectQuery);
            db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor == null)
                Toast.makeText(getContext(), cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
                // looping through all rows and adding to list
            else {
                if (cursor.moveToFirst()) {
                    do {
                        playlistItems.add(cursor.getString(cursor.getColumnIndex(PlayListStore.PLAYLIST_ID)));
                        songCount(cursor.getString(cursor.getColumnIndex(PlayListStore.PLAYLIST_ID)));
                    } while (cursor.moveToNext());
                }
                adapter = new PlaylistAdapter(getContext(), playlistItems, countList,this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_sliding_down_animation));
                recyclerView.scheduleLayoutAnimation();
                recyclerView.getAdapter().notifyDataSetChanged();
                // close db connection
                db.close();
            }
        } catch (SQLException e) {
        }
        // return notes list
    }

    private void songCount(String string) {
        String selectQuery = "SELECT " + PlayListStore.SONG_NAME + " FROM " + PlayListStore.TABLE_NAME + " WHERE " +
                PlayListStore.PLAYLIST_ID + "='" + string + "'";
        Log.e("QUERY_____", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        countList.add(String.valueOf(count));
    }

    public void openpopup(final String value ,final int position, View view) {
        final PopupMenu popup = new PopupMenu(getContext(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.removeplaylistpopup, popup.getMenu());
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.removeplaylist:
                        deleteSong(value,position);
                        popup.dismiss();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void deleteSong(String item,int position) {
        db = helper.getWritableDatabase();
        db.execSQL("delete from "+ PlayListStore.TABLE_NAME+" where playlist_name='"+item+"'");
        notifyDb(position);
        Toast.makeText(getContext(),"Playlist deleted",Toast.LENGTH_SHORT).show();
        db.close();
    }
    private void notifyDb(int position) {
        adapter.removeSongs(position);
    }
}
