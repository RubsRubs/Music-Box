package com.example.streamingaudioplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PruebaFirebaseQueryListViewAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    ArrayList<User> list = new ArrayList<>();

    public PruebaFirebaseQueryListViewAdaper(Context ctx) {
        this.context = ctx;
    }

    public void setItems(ArrayList<User> queryList) {
        this.list.addAll(queryList);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prueba_list_view_item, parent, false);
        return new PruebaQueryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PruebaQueryAdapterViewHolder pruebaQueryAdapterViewHolder = (PruebaQueryAdapterViewHolder) holder;
        User user = list.get(position);

        pruebaQueryAdapterViewHolder.pruebaText.setText(user.getEmail());
        //Glide.with(pruebaQueryAdapterViewHolder.itemView).load(artist.getArtistImageURL()).fitCenter().into(pruebaQueryAdapterViewHolder.imageViewPicture);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
