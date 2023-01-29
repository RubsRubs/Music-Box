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

public class EditProfileNameDialogue extends AppCompatDialogFragment {

    EditText editUserName;
    private ChangeProfileNameDialogueListener changeProfileNameDialogueListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.change_profile_name_dialogue_layout, null);

        builder.setView(view).setTitle("Cambiar nombe de usuario").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String name = editUserName.getText().toString();
                changeProfileNameDialogueListener.changeNameApplyText(name);
            }
        });

        editUserName = view.findViewById(R.id.edit_playlist_title_ID);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            changeProfileNameDialogueListener = (ChangeProfileNameDialogueListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PlayListAddDialogueListener");
        }
    }

    public interface ChangeProfileNameDialogueListener {
        void changeNameApplyText(String name);
    }
}
