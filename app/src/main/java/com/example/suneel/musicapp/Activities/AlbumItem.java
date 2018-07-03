package com.example.suneel.musicapp.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Adapters.AlbumItemAdapter;
import com.example.suneel.musicapp.Adapters.GenresItemAdapter;
import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.Database.Getmusic;
import com.example.suneel.musicapp.Fragments.Album;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongList;
import com.example.suneel.musicapp.models.SongModel;

import java.io.ByteArrayInputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 22/5/18.
 */

public class AlbumItem extends AppCompatActivity {
    private ArrayList<SongModel> songList;
    private RecyclerView recyclerView;
    private TextView categoryname;
    Getmusic helper;
    AlbumItemAdapter adapter;
    private Button back;
    private String songname;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albumitem);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewitem);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryname = (TextView) findViewById(R.id.categoryname);
        back = (Button) findViewById(R.id.back);
        songname = getIntent().getStringExtra("album");
        categoryname.setText(songname);
        showGenresList(songname);
        adapter = new AlbumItemAdapter(this, songList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this,R.anim.layout_sliding_down_animation));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.getAdapter().notifyDataSetChanged();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumItem.super.onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void showGenresList(String s) {
        songList = new ArrayList<>();
        helper = new Getmusic(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        // Select All Query
        String selectQuery = "SELECT song_name,song_uri,song_image FROM " + SongList.TABLE_NAME + " WHERE " +
                SongList.ALBUM + "='" + s + "'";
        Log.e("QUERY_____", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(this, cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    byte[] blob = cursor.getBlob(2);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    songList.add(new SongModel(cursor.getString(0), Uri.parse(cursor.getString(1)), bitmap));
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();
        }
    }

   /* public void setSongList( int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("category","album");
        intent.putExtra("name",songname);
        intent.putExtra("value", position);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }*/
}
