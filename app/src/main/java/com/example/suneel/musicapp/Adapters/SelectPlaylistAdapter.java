package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.PlalistItem;
import com.example.suneel.musicapp.Fragments.SelectPlaylist;
import com.example.suneel.musicapp.R;

import java.util.List;

/**
 * Created by suneel on 25/5/18.
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.PlayListHolder> {
    private Context mCtx;
    private List<String> songList;
    private PlalistItem item;
    private int positionofsong;
    private SelectPlaylist playlist;

    public SelectPlaylistAdapter(Context mCtx, List<String> songList, int position, SelectPlaylist playlist) {
        this.mCtx = mCtx;
        this.songList = songList;
        this.positionofsong = position;
        this.playlist = playlist;
    }

    @Override
    public SelectPlaylistAdapter.PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.selectplaylistrow, null);
        return new SelectPlaylistAdapter.PlayListHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectPlaylistAdapter.PlayListHolder holder, final int position) {
        holder.showplaylistname.setText(songList.get(position).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCtx != null && mCtx instanceof MainActivity) {
                    ((MainActivity) mCtx).addSongintonewplaylist(positionofsong, songList.get(position).toString());
                    if (playlist != null && playlist instanceof SelectPlaylist) {
                        ((SelectPlaylist) playlist).dismiss();
                    }
                }
                // ((mCtx).open(smodel,position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class PlayListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView showplaylistname;

        public PlayListHolder(View itemView) {
            super(itemView);
            showplaylistname = (TextView) itemView.findViewById(R.id.showplaylistname);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }
}





