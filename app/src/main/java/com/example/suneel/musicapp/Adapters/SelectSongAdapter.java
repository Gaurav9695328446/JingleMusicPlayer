package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;
import com.example.suneel.musicapp.Fragments.Songs;

import java.util.List;

/**
 * Created by suneel on 3/4/18.
 */

public class SelectSongAdapter extends RecyclerView.Adapter<SelectSongAdapter.SelectViewHolder> {
    private Context mCtx;
    //we are storing all the products in a list
    private List<SongModel> songList;
    //getting the context and product list with constructor

    public SelectSongAdapter(Context mCtx, List<SongModel> songList) {
        this.mCtx = mCtx;
        this.songList = songList;
    }

    @Override
    public SelectSongAdapter.SelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.selectsongitemcard, null);
        return new SelectSongAdapter.SelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectSongAdapter.SelectViewHolder holder, final int position) {
        final SongModel smodel = songList.get(position);
        //bind data to holder
        holder.imageView.setImageBitmap(songList.get(position).getImage());
        holder.songtitle.setText(songList.get(position).getTitle().toString());
        holder.songartist.setText(songList.get(position).getArtist().toString());
        holder.checkBox.setChecked(songList.get(position).isSelected());
        holder.checkBox.setTag(position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.checkBox.getTag();
                if (songList.get(pos).isSelected()) {
                    if (mCtx != null && mCtx instanceof MainActivity) {
                        songList.get(pos).setSelected(false);
                        ((MainActivity) mCtx).setCheck(songList.get(pos).isSelected(), smodel);
                    }
                } else {
                    if (mCtx != null && mCtx instanceof MainActivity) {
                        songList.get(pos).setSelected(true);
                        ((MainActivity) mCtx).setCheck(songList.get(pos).isSelected(), smodel);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SelectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView songtitle, songartist, songLocation;
        ImageView imageView;
        CheckBox checkBox;

        public SelectViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            songtitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            songartist = (TextView) itemView.findViewById(R.id.textViewShortDesc);
            checkBox = (CheckBox) itemView.findViewById(R.id.songSelect);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }
}
