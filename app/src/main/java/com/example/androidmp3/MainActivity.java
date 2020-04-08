package com.example.androidmp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.androidmp3.dataloader.SongLoader;
import com.example.androidmp3.fragments.IMusicSelected;
import com.example.androidmp3.fragments.PlayerFragment;
import com.example.androidmp3.fragments.PlaylistFragment;
import com.example.androidmp3.models.Music;

import java.util.List;

public class MainActivity extends AppCompatActivity implements IMusicSelected {

    PlayerFragment playerFragment;
    PlaylistFragment playlistFragment;
    private Toolbar toolbar;
    private Thread getSongs;
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
        getData();
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


    public void onMusicSelected(Music music) {

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

                break;
            case R.id.second:
                if (findViewById(R.id.frameLayout) != null) {
                    playerFragment = new PlayerFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playerFragment).commit();
                }
                break;
            case R.id.third:
                if (findViewById(R.id.frameLayout) != null) {
                    getData();
                    playlistFragment = new PlaylistFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playlistFragment).commit();
                }
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

    private void getData() {
        songList = SongLoader.getSongList(this);
    }
}
