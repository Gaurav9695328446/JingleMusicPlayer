package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suneel.musicapp.Activities.Youtube;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.Youtubemodel;

import java.util.List;

/**
 * Created by suneel on 15/5/18.
 */

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.PlaylistHolder> {
    private Context context;
    private List<Youtubemodel> songList;

    public YoutubeAdapter(Context context, List<Youtubemodel> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public YoutubeAdapter.PlaylistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.playlistcardview, null);
        return new YoutubeAdapter.PlaylistHolder(view);
    }


    @Override
    public void onBindViewHolder(YoutubeAdapter.PlaylistHolder holder, final int position) {
        Glide.with(context).load(songList.get(position).getUrlImage()).into(holder.songImage);
       /* holder.songImage.setImageBitmap(position);*/
        /*holder.songId.setText(songList.get(position).getSongid());*/
        holder.playlstId.setText(songList.get(position).getPublished_at());
        /*holder.videoid.setText("hello");*/
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context != null && context instanceof Youtube)
                    ((Youtube) context).playSong(songList.get(position).getPublished_at(), songList.get(position).getSongid());
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
        RelativeLayout relativeLayout;

        public PlaylistHolder(View itemView) {
            super(itemView);
            songImage = (ImageView) itemView.findViewById(R.id.songurlimage);
            songId = (TextView) itemView.findViewById(R.id.songid);
            playlstId = (TextView) itemView.findViewById(R.id.published_at);
            videoid = (TextView) itemView.findViewById(R.id.videoid);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.playlistlayout);
        }
    }
}
