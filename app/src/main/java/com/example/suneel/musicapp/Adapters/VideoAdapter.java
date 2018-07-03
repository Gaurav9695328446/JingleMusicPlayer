package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suneel.musicapp.Activities.VideoPlay;
import com.example.suneel.musicapp.Activities.YoutubePlay;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.Youtubemodel;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;

/**
 * Created by suneel on 16/5/18.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.PlaylistHolder> {
    private Context context;
    private ArrayList<Youtubemodel> songList;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    public VideoAdapter(Context context, ArrayList<Youtubemodel> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public VideoAdapter.PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.videoplaycard, null);
        return new VideoAdapter.PlaylistHolder(view);
    }


    @Override
    public void onBindViewHolder(VideoAdapter.PlaylistHolder holder, final int position) {
        Glide.with(context).load(songList.get(position).getUrlImage()).into(holder.songImage);
       /* holder.songImage.setImageBitmap(position);*/
       /* holder.songId.setText(songList.get(position).getSongid());*/
        holder.playlstId.setText(songList.get(position).getPublished_at());
        /*holder.videoid.setText(songList.get(position).getVideoid());*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null && context instanceof VideoPlay)
                    ((VideoPlay) context).videoSong(songList, songList.get(position).getVideoid());
                if (context != null && context instanceof YoutubePlay)
                    ((YoutubePlay) context).videoSong(songList, songList.get(position).getVideoid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class PlaylistHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songId, playlstId, videoid;

        public PlaylistHolder(View itemView) {
            super(itemView);
            songImage = (ImageView) itemView.findViewById(R.id.videoimage);
            songId = (TextView) itemView.findViewById(R.id.playlistid);
            playlstId = (TextView) itemView.findViewById(R.id.videoname);
            videoid = (TextView) itemView.findViewById(R.id.videoid);
        }
    }
}

