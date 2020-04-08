package com.example.androidmp3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;

import com.example.androidmp3.fragments.IMusicSelected;
import com.example.androidmp3.fragments.PlayerFragment;
import com.example.androidmp3.models.Music;

public class MainActivity extends AppCompatActivity implements IMusicSelected {

    PlayerFragment playerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (findViewById(R.id.frameLayout) != null) {
            playerFragment = new PlayerFragment();
            playerFragment.setListener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, playerFragment).commit();
        }
        playerFragment.setListener(this);
    }

    @Override
    public void onMusicSelected(Music music) {

    }

}
