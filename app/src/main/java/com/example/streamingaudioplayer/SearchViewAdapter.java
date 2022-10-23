package com.example.streamingaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<Artist> list = new ArrayList<>();
    int queryCombination;
    int albumPosition;
    int songPosition;

    public SearchViewAdapter(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<Artist> queryList, int queryCombination, int albumPosition, int songPosition) {
        this.list.addAll(queryList);
        this.queryCombination = queryCombination;
        this.albumPosition = albumPosition;
        this.songPosition = songPosition;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view_item, parent, false);
        return new SearchViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SearchViewAdapterViewHolder searchViewAlbumHolder = (SearchViewAdapterViewHolder) holder;
        Artist artist = list.get(position);

        switch (queryCombination) {
            case 1: {
                searchViewAlbumHolder.queryText.setText(artist.getName());
                Glide.with(searchViewAlbumHolder.itemView).load(artist.getArtistImageURL()).fitCenter().into(searchViewAlbumHolder.imageViewPicture);
            }
            /*case 2: {
                searchViewAlbumHolder.queryText.setText(artist.getAlbums().get(albumPosition).getAlbumTitle() + " " + artist.getName());
                break;
            }
            case 3: {
                searchViewAlbumHolder.queryText.setText(artist.getAlbums().get(albumPosition).getSongs().get(songPosition).getSongTitle() + " " + artist.getName());
            }*/
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("artist", artist);
                //bundle.putInt("queryCombination", queryCombination);
                Intent intent = new Intent(context, ArtistActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// addFlags para que no me de error al pasar a la nueva activity
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}