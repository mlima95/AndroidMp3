package com.example.androidmp3.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidmp3.R;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    Fragment infoMusic;
    Button previous;
    Button play;
    Button next;
    SeekBar seekBar;
    TextView duration;
    MediaPlayer player = new MediaPlayer();
    IMusicSelected listener;

    public void setListener(IMusicSelected listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        previous = (Button) v.findViewById(R.id.buttonPrevious);
        play = (Button) v.findViewById(R.id.buttonPlay);
        next = (Button) v.findViewById(R.id.buttonNext);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);

        return v;
    }
}
