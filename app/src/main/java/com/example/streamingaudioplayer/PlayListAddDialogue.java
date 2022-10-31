package com.example.streamingaudioplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

public class PlayListAddDialogue extends AppCompatDialogFragment {

    EditText editTitle, editDescription;
    SwitchCompat switchCompat;
    private PlayListAddDialogueListener playListAddDialogueListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_playlist_dialog_layout, null);

        builder.setView(view).setTitle("Crear lista de reproducción").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();
                boolean publica = false;
                if (switchCompat.isChecked()) {
                    publica = true;
                }
                if (title.isEmpty()) {
                    Toast.makeText(getContext(), "El título es obligatorio", Toast.LENGTH_SHORT).show();
                } else {
                    playListAddDialogueListener.applyTexts(title, description, publica);
                }
            }
        });

        editTitle = view.findViewById(R.id.edit_playlist_title_ID);
        editDescription = view.findViewById(R.id.edit_playlist_description_ID);
        switchCompat = view.findViewById(R.id.public_private_switch_ID);

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
        void applyTexts(String title, String description, boolean publica);
    }
}
