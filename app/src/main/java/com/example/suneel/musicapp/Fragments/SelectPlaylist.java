package com.example.suneel.musicapp.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Adapters.SelectPlaylistAdapter;
import com.example.suneel.musicapp.Database.DatabaseHelper;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.PlayListStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 25/5/18.
 */

public class SelectPlaylist extends DialogFragment {

    public RelativeLayout relativeLayout;
    public RecyclerView recyclerView;
    private Context context;
    private SelectPlaylistAdapter selectPlaylistAdapter;
    private List<String> sList;
    private DatabaseHelper helper;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selectplaylist, null);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.layout1);
        bundle = savedInstanceState;
        final int strtext = getArguments().getInt("songuri");
        setListValues();
        recyclerView = (RecyclerView) view.findViewById(R.id.selectplaylist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        selectPlaylistAdapter = new SelectPlaylistAdapter(getActivity(), sList, strtext, this);
        recyclerView.setAdapter(selectPlaylistAdapter);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_sliding_down_animation));
        recyclerView.scheduleLayoutAnimation();
        recyclerView.getAdapter().notifyDataSetChanged();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).dismisspopup();
                ((MainActivity) getActivity()).showDailog(strtext);
            }
        });
        return view;
    }

    private void setListValues() {
        sList = new ArrayList();
        helper = new DatabaseHelper(getActivity());
        // Select All Query
        String selectQuery = "SELECT DISTINCT playlist_name FROM " + PlayListStore.TABLE_NAME;
        Log.e("QUERY_____", selectQuery);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor == null)
            Toast.makeText(getActivity(), cursor.getCount() + "Cursor values", Toast.LENGTH_SHORT).show();
            // looping through all rows and adding to list
        else {
            if (cursor.moveToFirst()) {
                do {
                    sList.add(cursor.getString(cursor.getColumnIndex("playlist_name")));
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();
        }
    }

}
