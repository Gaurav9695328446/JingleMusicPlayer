package com.example.suneel.musicapp.Adapters;

import android.app.Activity;
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

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.R;
import com.example.suneel.musicapp.models.SongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suneel on 2/4/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchListHolder> {

    private Context mCtx;
    //we are storing all the products in a list
    private List<SongModel> songList;
    private List<SongModel> filteredList;

    //getting the context and product list with constructor

    public SearchAdapter(Context context, List<SongModel> songList) {
        this.mCtx = context;
        this.songList = songList;
        this.filteredList = songList;
    }

    public SearchAdapter(Context context) {
        this.mCtx = context;
    }

    @Override
    public SearchAdapter.SearchListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  LayoutInflater inflater = LayoutInflater.from(mCtx);
        LayoutInflater layoutInflater = (LayoutInflater) mCtx
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.cardlist, null, true);

        // View view = inflater.inflate(R.layout.cardlist, null);
        return new SearchAdapter.SearchListHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchListHolder holder, final int position) {
        final SongModel smodel = filteredList.get(position);
        //bind data to holder
        holder.imageView.setImageBitmap(filteredList.get(position).getImage());
        holder.songtitle.setText(filteredList.get(position).getTitle().toString());
        holder.songartist.setText(filteredList.get(position).getArtist().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mCtx).openSong(smodel);
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
        return filteredList.size();
    }


    public class SearchListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView songtitle, songartist, songLocation;
        ImageView imageView;

        public SearchListHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            songtitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            songartist = (TextView) itemView.findViewById(R.id.textViewShortDesc);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mCtx, "Itemclicked", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFilter(List<SongModel> songModels) {
        filteredList = new ArrayList<>();
        filteredList.addAll(songModels);
        notifyDataSetChanged();
    }
}
