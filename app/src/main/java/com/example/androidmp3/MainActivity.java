package com.example.androidmp3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidmp3.dataloader.SongLoader;
import com.example.androidmp3.fragments.DownloadFragment;
import com.example.androidmp3.fragments.IMusicSelected;
import com.example.androidmp3.fragments.PlayerFragment;
import com.example.androidmp3.fragments.PlaylistFragment;
import com.example.androidmp3.models.Music;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMusicSelected, AudioManager.OnAudioFocusChangeListener {

    PlayerFragment playerFragment;
    PlaylistFragment playlistFragment;
    SearchView searchView;

    public Music getMusicSelected() {
        return musicSelected;
    }

    public void setMusicSelected(Music musicSelected) {
        this.musicSelected = musicSelected;
    }

    Music musicSelected = null;
    DownloadFragment downloadFragment;
    private AudioManager mAudioManager;
    private Toolbar toolbar;
    private List<Music> songList;

    public static MainActivity getInstance() {
        return instance;
    }

    private static MainActivity instance;

    public List<Music> getSongList() {
        return songList;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        instance = this;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        getData(0);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.frameLayout) != null) {
            playlistFragment = new PlaylistFragment();
            playlistFragment.setListener(this);
            playerFragment = new PlayerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, playlistFragment)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioManager.abandonAudioFocus(this);
    }

    public void onMusicSelected(Music music) {
        musicSelected = music;
        toolbar.getMenu().findItem(R.id.sort).setVisible(false);
        if (!playerFragment.isAdded()) {

                getSupportFragmentManager().beginTransaction()
                        .hide(playlistFragment)
                        .add(R.id.frameLayout, playerFragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(playlistFragment)
                        .show(playerFragment)
                        .commit();
            }

        playerFragment.select(music);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.first:
                if (findViewById(R.id.frameLayout) != null) {
                    downloadFragment = new DownloadFragment();
                    toolbar.getMenu().findItem(R.id.sort).setVisible(false);
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, downloadFragment).commit();
                }
                break;
            case R.id.second:
                if (findViewById(R.id.frameLayout) != null) {
                    playerFragment = new PlayerFragment();
                    toolbar.getMenu().findItem(R.id.sort).setVisible(false);
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playerFragment).commit();
                }
                break;
            case R.id.third:
                if (findViewById(R.id.frameLayout) != null) {
                    getData(0);
                    playlistFragment = new PlaylistFragment();
                    toolbar.getMenu().findItem(R.id.sort).setVisible(true);
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playlistFragment).commit();
                }
                break;
            case R.id.a_to_z:
                Collections.sort(SongLoader.songList, new AlphabeticalComparator());
                playlistFragment.initList();
                break;
            case R.id.z_to_a:
                Collections.sort(SongLoader.songList, new ReverseAlphabeticalComparator());
                playlistFragment.initList();
                break;
            case R.id.dateAdded:
                Collections.sort(SongLoader.songList, new DateComparator());
                playlistFragment.initList();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (playerFragment.isAdded() && playerFragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .hide(playerFragment)
                    .show(playlistFragment)
                    .commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange <= 0) {
            playerFragment.getPlayer().pause();
        } else {
            playerFragment.getPlayer().start();
        }
    }

    private class AlphabeticalComparator implements Comparator<Music> {
        @Override
        public int compare(Music music1, Music music2) {
            return music1.getTitre().toLowerCase().compareTo(music2.getTitre().toLowerCase());
        }
    }

    private class ReverseAlphabeticalComparator implements Comparator<Music> {
        @Override
        public int compare(Music music1, Music music2) {
            return music2.getTitre().toLowerCase().compareTo(music1.getTitre().toLowerCase());
        }
    }

    private class DateComparator implements Comparator<Music> {
        @Override
        public int compare(Music music1, Music music2) {
            long result = music1.getDate() - music2.getDate();
            return (int) result;
        }
    }

    private void getData(int option) {
        songList = SongLoader.getSongList(this);
    }
}
