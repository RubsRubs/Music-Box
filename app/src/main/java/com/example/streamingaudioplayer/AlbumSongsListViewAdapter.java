package com.example.streamingaudioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AlbumSongsListViewAdapter extends ArrayAdapter {

    public AlbumSongsListViewAdapter(Context context, int resource, ArrayList<Song> songs) {
        super(context, resource, songs);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Song song = (Song) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_song_item, parent, false);
        }
        TextView songPositionTxtView = convertView.findViewById(R.id.album_song_position_ID);
        TextView songNameTxtView = convertView.findViewById(R.id.song_name_text_view_ID);
        TextView songDurationTxtView = convertView.findViewById(R.id.song_duration_text_view_ID);
        ImageView imageViewMenu = convertView.findViewById(R.id.song_item_menu_ID);

        int songPosition = position + 1;
        String positionToString = String.valueOf(songPosition);
        songPositionTxtView.setText(positionToString);
        songNameTxtView.setText(song.getSongTitle());
        songDurationTxtView.setText(song.getDuration());

        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.song_item_pop_up_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener((menuItem) -> {

                    switch (menuItem.getItemId()) {

                        case R.id.agregar_a_lista_ID: {

                            break;
                        }
                        case R.id.agregar_a_favoritos_ID: {
                            addToFavourites();
                        }
                    }
                    return true;
                });
            }

            private void addToFavourites() {

                String idNumber = Double.toString(song.getSongId());
                SongIDModel songIDModel = new SongIDModel(idNumber);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(); //este objeto hace referencia al nodo principal de la bd en real-time
                String userId = auth.getCurrentUser().getUid(); //obtenemos el id del usuario recién registrado para después utilizarlo para generar su mapa de valores correspondiente.

                //String favouritesKey = databaseReference.child("Users").child("favourites").push().getKey();

                databaseReference.child("Users").child(userId).child("favourites").push().setValue(songIDModel).addOnCompleteListener(new OnCompleteListener<Void>() { //.child crea un nuevo nodo
                    @Override
                    public void onComplete(@NonNull Task<Void> task1) {
                        Toast.makeText(getContext(), "Canción agregada a favoritos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return convertView;
    }
}