package com.example.androidmp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.androidmp3.fragments.IMusicSelected;
import com.example.androidmp3.fragments.PlayerFragment;
import com.example.androidmp3.fragments.PlaylistFragment;
import com.example.androidmp3.models.Music;

public class MainActivity extends AppCompatActivity {

    PlayerFragment playerFragment;
    PlaylistFragment playlistFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                break;
            case R.id.second:
                if (findViewById(R.id.frameLayout) != null) {
                    playerFragment = new PlayerFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playerFragment).commit();
                }
                break;
            case R.id.third:
                if (findViewById(R.id.frameLayout) != null) {
                    playlistFragment = new PlaylistFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playlistFragment).commit();
                }
                break;
            case R.id.third:
                if (findViewById(R.id.frameLayout) != null) {
                    playlistFragment = new PlaylistFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playlistFragment).commit();
                }
                break;



        }

        return super.onOptionsItemSelected(item);
    }
}
