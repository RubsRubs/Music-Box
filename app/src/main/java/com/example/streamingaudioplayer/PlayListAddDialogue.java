package com.example.streamingaudioplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PlayListAddDialogue extends AppCompatDialogFragment {

    EditText editTitle, editDescription;
    private PlayListAddDialogueListener playListAddDialogueListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_playlist_dialog_layout, null);

        builder.setView(view).setTitle("Añadir Playlist").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();
                playListAddDialogueListener.applyTexts(title, description);
            }
        });

        editTitle = view.findViewById(R.id.edit_playlist_title_ID);
        editDescription = view.findViewById(R.id.edit_playlist_description_ID);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            playListAddDialogueListener = (PlayListAddDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PlayListAddDialogueListener");
        }
    }

    public interface PlayListAddDialogueListener {
        void applyTexts(String title, String description);
    }
}
