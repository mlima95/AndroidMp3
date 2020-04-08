package com.example.androidmp3.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidmp3.R;
import com.example.androidmp3.models.Music;

import java.io.IOException;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    Fragment infoMusic;
    Button previous;
    Button play;
    Button next;
    Music currentMusic;
    SeekBar seekBar;
    TextView textViewAlbum;
    TextView textViewArtiste;
    TextView textViewTitre;
    ImageView imageView;
    TextView duration;
    MediaPlayer player = new MediaPlayer();
    IMusicSelected listener;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        previous = (Button) v.findViewById(R.id.buttonPrevious);
        play = (Button) v.findViewById(R.id.buttonPlay);
        next = (Button) v.findViewById(R.id.buttonNext);
        textViewAlbum=(TextView) v.findViewById(R.id.textViewAlbum);
        textViewArtiste=(TextView) v.findViewById(R.id.textViewArtiste);
        textViewTitre=(TextView) v.findViewById(R.id.textViewTitre);
        duration=(TextView) v.findViewById(R.id.textViewDuration);
        imageView=(ImageView) v.findViewById(R.id.song_cover);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View v) {
        if(v.equals(play) && !player.isPlaying()){
            try{
                player.reset();
                player.setDataSource(getContext(), Uri.parse(currentMusic.getPath()));
                player.prepare();
                player.start();
            }catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.stop();
        }
    }

    public void select(Music m) {
        currentMusic = m;
        if (textViewAlbum != null) {
            refresh();
        };
    }

    public void refresh() {
       textViewAlbum.setText(currentMusic.getAlbum());
        textViewTitre.setText(currentMusic.getTitre());
        textViewArtiste.setText(currentMusic.getArtist());
    }



}
