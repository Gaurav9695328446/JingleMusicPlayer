package com.example.suneel.musicapp.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suneel.musicapp.Activities.MainActivity;
import com.example.suneel.musicapp.R;

/**
 * Created by suneel on 24/3/18.
 */

public class MyDialog extends DialogFragment {
    TextView message;
    Button create, cancel;
    EditText playlistName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alertplaylist, null);
        final int strtext = getArguments().getInt("songuri", -1);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        message = (TextView) view.findViewById(R.id.Message);
        create = (Button) view.findViewById(R.id.yes);
        cancel = (Button) view.findViewById(R.id.cancel);
        playlistName = (EditText) view.findViewById(R.id.playlistname);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strtext != -1) {
                    if ((TextUtils.isEmpty(playlistName.getText().toString().trim()))) {
                        message.setText("Please enter the playlist name");
                        return;
                    } else {
                        ((MainActivity) getActivity()).addSongintonewplaylist(strtext, playlistName.getText().toString().trim());
                    }
                } else {
                    if ((TextUtils.isEmpty(playlistName.getText().toString().trim()))) {
                        message.setText("Please enter the playlist name");
                        return;
                    } else {
                        ((MainActivity) getActivity()).changelayout(playlistName.getText().toString().trim());
                    }

                }
                ((MainActivity) getActivity()).mydialogdismisspopup();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).mydialogdismisspopup();
            }
        });

        return view;
    }

    public static MyDialog newInstance() {
        MyDialog f = new MyDialog();
        return f;
    }
}
