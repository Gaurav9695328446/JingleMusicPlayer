package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suneel.musicapp.Activities.AlbumItem;
import com.example.suneel.musicapp.Activities.GenresItem;
import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Fragments.Album;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 22/5/18.
 */

public class AlbumItemAdapter extends RecyclerView.Adapter<AlbumItemAdapter.SongListHolder> {
    private Context context;
    private ArrayList<SongModel> songList;

    public AlbumItemAdapter(Context context, ArrayList<SongModel> songList) {
        this.context = context;
        this.songList = songList;

    }

    @Override
    public AlbumItemAdapter.SongListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardviewgenres, null);
        return new AlbumItemAdapter.SongListHolder(view);
    }


    @Override
    public void onBindViewHolder(AlbumItemAdapter.SongListHolder holder, final int position) {
        Glide.with(context).load(songList.get(position).getImage()).into(holder.gridImage);
        if (songList.get(position).getTitle() != "")
            holder.gridArtist.setText(songList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null && context instanceof AlbumItem) {
                    /*((AlbumItem) context).setSongList(position);*/
                    MainActivity.instance.getPlaylist(songList,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongListHolder extends RecyclerView.ViewHolder {
        ImageView gridImage;
        TextView gridArtist;

        public SongListHolder(View itemView) {
            super(itemView);
            gridImage = (ImageView) itemView.findViewById(R.id.genresImage);
            gridArtist = (TextView) itemView.findViewById(R.id.genresartist);
        }
    }
}
