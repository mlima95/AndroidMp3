package com.example.androidmp3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidmp3.MainActivity;
import com.example.androidmp3.R;
import com.example.androidmp3.models.Music;
import com.example.androidmp3.models.MusicAdapter;

public class PlaylistFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    MusicAdapter adapter;
    IMusicSelected listener;

    public void setListener(IMusicSelected listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, null);
        listView = (ListView) v.findViewById(R.id.playlist);
        adapter = new MusicAdapter(getContext());
        adapter.setMusics(MainActivity.getInstance().getSongList());
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = (Music) parent.getItemAtPosition(position);
                listener.onMusicSelected(music);
            }
        });
        return v;
    }
}
