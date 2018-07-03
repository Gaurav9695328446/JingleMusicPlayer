package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.PlalistItem;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 11/4/18.
 */

public class PlayListItemAdapter extends RecyclerView.Adapter<PlayListItemAdapter.SelectViewHolder> {
    private ArrayList<SongModel> plaList;
    private Context mCtx;
    private String playlistame;

    public PlayListItemAdapter(PlalistItem plalistItem, ArrayList<SongModel> songList, String plalistname) {
        this.mCtx = plalistItem;
        this.plaList = songList;
        this.playlistame = plalistname;
    }

    @Override
    public PlayListItemAdapter.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.playlistitemcard, null);
        return new SelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayListItemAdapter.SelectViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(plaList.get(position).getImage());
        holder.songtitle.setText(plaList.get(position).getTitle());
        holder.songartist.setText(plaList.get(position).getArtist());
        holder.overflowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCtx != null && mCtx instanceof PlalistItem) {
                    ((PlalistItem) mCtx).openpopup(playlistame,plaList.get(position).getTitle(),holder.overflowClick,position);
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCtx != null && mCtx instanceof PlalistItem)
                {
                    MainActivity.instance.getPlaylist(plaList,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plaList.size();
    }

    public void removeSongs(int position) {
        plaList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class SelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView songtitle, songartist;
        private ImageView imageView;
        private ImageView overflowClick;

        public SelectViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.newimage);
            songtitle = (TextView) itemView.findViewById(R.id.newtitle);
            songartist = (TextView) itemView.findViewById(R.id.newartist);
            overflowClick=(ImageView)itemView.findViewById(R.id.overflow);

        }

        public void onClick(View v) {

        }
    }
}
