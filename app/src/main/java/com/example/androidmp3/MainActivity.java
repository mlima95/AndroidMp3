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
import com.example.androidmp3.fragments.DownloadFragment;
import com.example.androidmp3.fragments.IMusicSelected;
import com.example.androidmp3.fragments.PlayerFragment;
import com.example.androidmp3.fragments.PlaylistFragment;
import com.example.androidmp3.models.Music;

import java.util.List;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;

public class MainActivity extends AppCompatActivity {

    PlayerFragment playerFragment;
    PlaylistFragment playlistFragment;
    DownloadFragment downloadFragment;
    private Toolbar toolbar;
    private Thread getSongs;

    public static MainActivity getInstance() {
        return instance;
    }

    private static MainActivity instance;

    public List<Music> getSongList() {
        return songList;
    }

    private List<Music> songList;

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
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, downloadFragment).commit();
                }
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

    private void getData() {
        songList = SongLoader.getSongList(this);
    }
}
