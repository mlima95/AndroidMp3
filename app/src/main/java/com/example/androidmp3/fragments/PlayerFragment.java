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
    MusicAdapter adapter;
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
//      seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
//      seekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorText), PorterDuff.Mode.SRC_IN);
        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
       // togglePlay(player);

        player.reset();
        try {
            final Music currentMusic = MainActivity.getInstance().getMusicSelected();
            if (currentMusic != null) {
                player.setDataSource(getContext(), Uri.parse(currentMusic.getPath()));
                player.prepare();
                player.start();
                handlerSeekbar();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int CurrentPosition = player.getCurrentPosition();
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
                        // int songDuration = currentMusic.getDuration();

                        //duration.setText(convertDuration(player.getCurrentPosition()));
                        handler.postDelayed(this, 1000);

                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        refresh();
        return v;
    }

    private void handlerSeekbar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(player != null){
                   // player.seekTo(progress * 1000);

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
        if(v.equals(play) && !player.isPlaying()){
                play.setImageResource(android.R.drawable.ic_media_pause);
                player.start();
              //  currentId=currentMusic.getId();
        } else {
            play.setImageResource(android.R.drawable.ic_media_play);
            player.pause();
        }
    }

//    public int getSongPosition(){
//        if(player == null){
//            Log.e("Error","MediaPlayer is null !!");
//            return -1;
//        }else{
//            return player.getCurrentPosition();
//        }
//    }

    public void select(Music m) {
        if (textViewAlbum != null) {
            refresh();
        };
    }


//    private void togglePlay(MediaPlayer mp){
//
//        if(mp.isPlaying()){
//            mp.stop();
//            mp.reset();
//        }else{
//            mp.start();
//            play.setImageResource(android.R.drawable.ic_media_pause);
//        }
//
//    }


    public void refresh() {
        Music currentMusic = MainActivity.getInstance().getMusicSelected();
        if (currentMusic == null) return;
        textViewAlbum.setText(currentMusic.getAlbum());
        textViewTitre.setText(currentMusic.getTitre());
        textViewArtiste.setText(currentMusic.getArtist());
        getAlbum(currentMusic.getPath());
//        newId=currentMusic.getId();
//        if(newId != currentId){
//            play.setImageResource(android.R.drawable.ic_media_pause);
//            player.stop();
//        }
       // prepareSong(currentMusic);
    }
    public static String convertDuration(long duration){
        long minutes = (duration/1000) / 60;
        long seconds = ( duration /1000) % 60;

        String converted = String.format("%d:%02d",minutes,seconds);
        return converted;
    }

//    private void prepareSong(Music m) {
//        player.reset();
//        try {
//            player.setDataSource(getContext(), Uri.parse(currentMusic.getPath()));
//            player.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private void changeSelectedSong(long index){
//        adapter.getSelectedPosition();
//        adapter.notifyDataSetChanged();
//        currentId = index;
//        adapter.setSelectedPosition(currentId);
//        adapter.notifyDataSetChanged();
//    }


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
