package com.example.androidmp3.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.androidmp3.MainActivity;
import com.example.androidmp3.R;
import com.example.androidmp3.dataloader.SongLoader;
import com.example.androidmp3.models.Music;
import com.example.androidmp3.models.MusicAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment implements View.OnClickListener {

    Fragment infoMusic;
    ImageButton previous;
    ImageButton play;
    ImageButton next;
    SeekBar seekBar;
    TextView textViewAlbum;
    TextView textViewArtiste;
    TextView textViewTitre;
    ImageView imageView;
    TextView duration;
    Thread thread;
    MusicAdapter adapter;
    Runnable runnable;

    public MediaPlayer getPlayer() {
        return player;
    }

    MediaPlayer player = new MediaPlayer();
    IMusicSelected listener;
    Boolean isLaunch=true;
    private long currentId;
    private long newId;
    private List<Music> songList= MainActivity.getInstance().getSongList();
    Handler handler = new Handler();




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        previous = (ImageButton) v.findViewById(R.id.buttonPrevious);
        play = (ImageButton) v.findViewById(R.id.buttonPlay);
        next = (ImageButton) v.findViewById(R.id.buttonNext);

        textViewAlbum=(TextView) v.findViewById(R.id.textViewAlbum);
        textViewArtiste=(TextView) v.findViewById(R.id.textViewArtiste);
        textViewTitre=(TextView) v.findViewById(R.id.textViewTitre);
        duration=(TextView) v.findViewById(R.id.songbar_time);
        imageView=(ImageView) v.findViewById(R.id.song_cover);
        seekBar = (SeekBar) v.findViewById(R.id.songbar_progress);
        play.setImageResource(android.R.drawable.ic_media_pause);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        play();
        return v;
    }


    private void play() {
        player.reset();
        try {
            final Music currentMusic = MainActivity.getInstance().getMusicSelected();
            if (currentMusic != null) {
                player.setDataSource(getContext(), Uri.parse(currentMusic.getPath()));
                player.prepare();
                player.start();
                play.setImageResource(android.R.drawable.ic_media_pause);
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                runnable = new Runnable() {
                    @Override
                    public void run () {
                        int CurrentPosition = player.getCurrentPosition() + 1000;
                        int songDuration = currentMusic.getDuration();
                        seekBar.setMax(songDuration);
                        seekBar.setProgress(CurrentPosition);
                        String songPositionTime = String.format(
                                Locale.US, "%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(CurrentPosition),
                                TimeUnit.MILLISECONDS.toSeconds(CurrentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(CurrentPosition))
                        );

                        String songDurationTime = String.format(
                                Locale.US, "%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(songDuration),
                                TimeUnit.MILLISECONDS.toSeconds(songDuration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(songDuration))
                        );

                        String totalSongTime = songPositionTime + "/" + songDurationTime;
                        duration.setText(totalSongTime);
                        if (songPositionTime.equals(songDurationTime)) {
                            next.performClick();
                        }
                        handler.postDelayed(this, 1000);


                    }


                };
                getActivity().runOnUiThread(runnable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(play)){
            if (!player.isPlaying()) {
                play.setImageResource(android.R.drawable.ic_media_pause);
                player.start();
            } else {
                play.setImageResource(android.R.drawable.ic_media_play);
                player.pause();
            }
        } else if (v.equals(next)){
            Music currentMusic = MainActivity.getInstance().getMusicSelected();
            Music trueMusic = SongLoader.findSongById(currentMusic.getId());
            int position = SongLoader.songList.indexOf(trueMusic);

            if (songList.size() > position + 1) {
                MainActivity.getInstance().setMusicSelected(SongLoader.songList.get(position + 1));
            } else {
                MainActivity.getInstance().setMusicSelected(SongLoader.songList.get(0));
            }
            play();
        } else if (v.equals(previous)){
            Music currentMusic = MainActivity.getInstance().getMusicSelected();
            Music trueMusic = SongLoader.findSongById(currentMusic.getId());
            int position = SongLoader.songList.indexOf(trueMusic);

            if (position > 0) {
                MainActivity.getInstance().setMusicSelected(SongLoader.songList.get(position - 1));
            } else {
                MainActivity.getInstance().setMusicSelected(SongLoader.songList.get(SongLoader.songList.size() - 1));
            }
            play();
        }
    }



    public void select(Music m) {
        if (textViewAlbum != null) {
            play();
        }
    }

    public void refresh() {
        Music currentMusic = MainActivity.getInstance().getMusicSelected();
        if (currentMusic == null) return;
        textViewAlbum.setText(currentMusic.getAlbum());
        textViewTitre.setText(currentMusic.getTitre());
        textViewArtiste.setText(currentMusic.getArtist());
        getAlbum(currentMusic.getPath());
    }
    public static String convertDuration(long duration){
        long minutes = (duration/1000) / 60;
        long seconds = ( duration /1000) % 60;

        String converted = String.format("%d:%02d",minutes,seconds);
        return converted;
    }


        public void getAlbum(String path){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

        byte [] data = mmr.getEmbeddedPicture();

        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            imageView.setImageResource(R.drawable.channels4_profile);
        }
    }

}
