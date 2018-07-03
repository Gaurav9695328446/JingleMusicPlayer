package com.example.suneel.musicapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.Activities.PlalistItem;
import com.example.suneel.musicapp.Fragments.Playlist;
import com.example.suneel.musicapp.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by suneel on 30/3/18.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlayListHolder> {
    private Context mCtx;
    private List<String> songList;
    private PlalistItem item;
    private List<String> count;
    private Playlist mfragment;

    public PlaylistAdapter(Context mCtx, List<String> songList, List<String> count,Playlist mfragment) {
        this.mCtx = mCtx;
        this.songList = songList;
        this.count = count;
        this.mfragment=mfragment;
    }

    @Override
    public PlaylistAdapter.PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.playlistcard, null);
        return new PlayListHolder(view);
    }

    public void removeSongs(int position) {
        songList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlaylistAdapter.PlayListHolder holder, final int position) {
        final String item = songList.get(position);
        holder.text.setText(item);
        holder.Songs.setText(count.get(position) + " songs");
        holder.image.setImageResource(R.mipmap.song);
        holder.overflowClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mfragment != null && mfragment instanceof Playlist) {
                    ((Playlist) mfragment).openpopup(item,position,holder.overflowClick);
                }

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(v.getResources(), R.mipmap.song); // your bitmap
                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs);
                if (mCtx != null && mCtx instanceof MainActivity) {

                    ((MainActivity) mCtx).setSelectedSong(holder.text.getText().toString(), holder.Songs.getText().toString(), bitmap);

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
        TextView text, Songs;
        ImageView image;
        private ImageView overflowClick;

        public PlayListHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.cardTitle);
            Songs = (TextView) itemView.findViewById(R.id.countsongs);
            image = (ImageView) itemView.findViewById(R.id.cardsong);
            overflowClick=(ImageView)itemView.findViewById(R.id.overflow);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }


}
