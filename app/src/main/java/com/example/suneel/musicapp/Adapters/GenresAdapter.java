package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suneel.musicapp.Fragments.Genres;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.Utils.ImageNicer;
import com.example.suneel.musicapp.models.SongModel;

import java.util.List;

/**
 * Created by suneel on 12/4/18.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.SongListHolder> {

    private Context context;
    private List<SongModel> songList;
    private Genres mFragment;

    public GenresAdapter(Context context, List<SongModel> songList, Genres genres) {
        this.context = context;
        this.songList = songList;
        this.mFragment = genres;
    }

    @Override
    public GenresAdapter.SongListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardviewgenres, null);
        return new GenresAdapter.SongListHolder(view);
    }


    @Override
    public void onBindViewHolder(GenresAdapter.SongListHolder holder, final int position) {
        Glide.with(context).load(songList.get(position).getImage()).into(holder.gridImage);
        if (songList.get(position).getTitle() != "" && songList.get(position).getTitle() != null)
            holder.gridArtist.setText(songList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null && mFragment instanceof Genres)
                    ((Genres) mFragment).setGenresList(songList.get(position).getTitle());
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

    public void addSongs(List<SongModel> songs) {
        for (SongModel sm : songs) {
            songList.add(sm);
        }
        notifyDataSetChanged();
    }
}
