package com.example.androidmp3.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidmp3.R;
import com.example.androidmp3.models.Music;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    Fragment infoMusic;
    Button previous;
    ImageButton play;
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
    Timer timer = new Timer();
    Handler  handler = new Handler();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        previous = (Button) v.findViewById(R.id.buttonPrevious);
        play = (ImageButton) v.findViewById(R.id.buttonPlay);
        next = (Button) v.findViewById(R.id.buttonNext);
        textViewAlbum=(TextView) v.findViewById(R.id.textViewAlbum);
        textViewArtiste=(TextView) v.findViewById(R.id.textViewArtiste);
        textViewTitre=(TextView) v.findViewById(R.id.textViewTitre);
        duration=(TextView) v.findViewById(R.id.textViewDuration);
        imageView=(ImageView) v.findViewById(R.id.song_cover);
        seekBar = (SeekBar) v.findViewById(R.id.songbar_progress);
        play.setImageResource(android.R.drawable.ic_media_pause);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        player.reset();
        try {
            player.setDataSource(getContext(), Uri.parse(currentMusic.getPath()));
            player.prepare();
            player.start();
            handlerSeekbar();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    seekBar.setMax((int) currentMusic.getDuration() /1000 );
                    int CurrentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setProgress(CurrentPosition);
                    String songPositionTime = String.format(
                            Locale.US, "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(CurrentPosition),
                            TimeUnit.MILLISECONDS.toSeconds(CurrentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(CurrentPosition))
                    );

                    String totalSongTime = songPositionTime + "/" + currentMusic.getDuration() /1000;
                    //duration.setText(totalSongTime);
                    int songDuration = currentMusic.getDuration();
                    //duration.setText(convertDuration(player.getCurrentPosition()));
                    handler.postDelayed(this,1000);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return v;
    }

    private void handlerSeekbar(){
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null){
                    player.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(player.isPlaying()){
            play.setImageResource(android.R.drawable.ic_media_play);
            player.pause();

        } else {
            play.setImageResource(android.R.drawable.ic_media_pause);
            player.start();
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
    public static String convertDuration(long duration){
        long minutes = (duration/1000) / 60;
        long seconds = ( duration /1000) % 60;

        String converted = String.format("%d:%02d",minutes,seconds);
        return converted;
    }



}
