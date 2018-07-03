package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Fragments.Songs;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by suneel on 13/3/18.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    private MediaMetadataRetriever metaRetriver;
    private byte[] art;
    private Bitmap songImage;
    //we are storing all the products in a list
    private ArrayList<SongModel> songList;
    private Fragment mFragment;

    //getting the context and product list with constructor

    public SongAdapter(Context mCtx, ArrayList<SongModel> songList, Songs songs) {
        this.mCtx = mCtx;
        this.songList = songList;
        mFragment = songs;

    }

    @Override
    public SongAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardlist, null);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongAdapter.SongViewHolder holder, final int position) {
        final SongModel smodel = songList.get(position);
        //bind data to holder
        Glide.with(mCtx).load(songList.get(position).getImage()).into(holder.imageView);
        holder.songtitle.setText(songList.get(position).getTitle().toString());
        holder.songartist.setText(songList.get(position).getArtist().toString());
        holder.overflowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCtx != null && mCtx instanceof MainActivity) {
                    ((MainActivity) mCtx).openpopup(smodel, position, holder.overflowClick,mFragment);
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mFragment != null && mFragment instanceof Songs) {
                    ((MainActivity)mCtx).open(songList, position);
                    ((MainActivity)mCtx).OpenSliding();
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void removeSongs(int position) {
        songList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songtitle, songartist;
        ImageView imageView;
        CheckBox checkBox;
        ImageView overflowClick;

        public SongViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            songtitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            songartist = (TextView) itemView.findViewById(R.id.textViewShortDesc);
            overflowClick = (ImageView) itemView.findViewById(R.id.ImageView1);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }

    public void addSongs(List<SongModel> songs) {
        for (SongModel sm : songs) {
            songList.add(sm);
        }
        notifyDataSetChanged();

    }
}

