package com.example.streamingaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    private Context context;
    ArrayList<String> list;

    public SliderAdapter(Context context, ArrayList<String> songIdsList) {
        this.context = context;
        this.list = songIdsList;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder sliderViewHolder, final int position) {

        String songId = list.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Artist artist = dataSnapshot.getValue(Artist.class);

                    ArrayList<Album> albums = artist.getAlbums();

                    for (Album album : albums) {

                        ArrayList<Song> songs = album.getSongs();

                        for (Song song : songs) {

                            for (int i = 0; i < list.size(); i++) {

                                if (Double.toString(song.getSongId()).equals(songId)) {
                                    Glide.with(sliderViewHolder.itemView).load(album.getImgURL()).fitCenter().into(sliderViewHolder.imgViewCover);
                                    sliderViewHolder.txtViewArtist.setText(artist.getName());
                                    sliderViewHolder.txtViewSongName.setText(song.getSongTitle());
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sliderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("songIdPosition", position);
                bundle.putSerializable("songIdsList", list);
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent); //me obliga a poner context delante del método startActivity(intent)...*/
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

// VIEW_HOLDER ----------------------------------VIEW_HOLDER---------------------------------------- VIEW_HOLDER

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView imgViewCover;
        TextView txtViewArtist, txtViewSongName;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imgViewCover = itemView.findViewById(R.id.imgView_Cover_ID);
            txtViewArtist = itemView.findViewById(R.id.slider_item_txt_artist_ID);
            txtViewSongName = itemView.findViewById(R.id.slider_item_txt_song_name_ID);
        }
    }
}
