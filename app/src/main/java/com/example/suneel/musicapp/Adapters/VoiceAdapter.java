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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.VoiceSearchClass;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;

import java.util.List;

/**
 * Created by suneel on 31/3/18.
 */

public class VoiceAdapter extends RecyclerView.Adapter<VoiceAdapter.VoiceViewHolder> {

    private Context mCtx;
    MediaMetadataRetriever metaRetriver;
    byte[] art;
    Bitmap songImage;
    boolean aBoolean;
    CheckBox check;
    private SongModel smodel;

    //we are storing all the products in a list
    private List<SongModel> songList;
    private List<SongModel> filteredList;
    private Fragment mFragment;

    public VoiceAdapter(Context mCtx, List<SongModel> songList, boolean state) {
        this.mCtx = mCtx;
        this.songList = songList;
        this.aBoolean = state;
    }

    @Override
    public VoiceAdapter.VoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cardlist, parent);
        return new VoiceAdapter.VoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VoiceAdapter.VoiceViewHolder holder, final int position) {
        smodel = songList.get(position);
        holder.imageView.setImageBitmap(songList.get(position).getImage());
        holder.songtitle.setText(songList.get(position).getTitle().toString());
        holder.songartist.setText(songList.get(position).getArtist().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VoiceSearchClass) mCtx).open(smodel, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mCtx, "hello", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //getting the context and product list with constructor
    public class VoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songtitle, songartist, songLocation;
        ImageView imageView;
        CheckBox checkBox;

        public VoiceViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            songtitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            songartist = (TextView) itemView.findViewById(R.id.textViewShortDesc);
            checkBox = (CheckBox) itemView.findViewById(R.id.songSelect);

            // songLocation=(TextView)itemView.findViewById(R.id.location);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }

}
