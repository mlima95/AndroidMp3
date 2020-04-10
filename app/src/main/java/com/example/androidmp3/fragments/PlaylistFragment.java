package com.example.androidmp3.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidmp3.R;
import com.example.androidmp3.dataloader.SongLoader;
import com.example.androidmp3.models.Music;
import com.example.androidmp3.models.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    MusicAdapter adapter;
    IMusicSelected listener;
    SearchView searchView;
    EditText editSearch;
    Button buttonSearch;

    public void setListener(IMusicSelected listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSearch) {
            SongLoader.getSongList(getContext());
            String search = editSearch.getText().toString().toLowerCase();
            if (!("".equals(search))) {
                List<Music> musics = new ArrayList<Music>();
                for (Music music : SongLoader.songList) {
                    if (music.getTitre().toLowerCase().contains(search) ||
                            music.getAlbum().toLowerCase().contains(search) ||
                            music.getArtist().toLowerCase().contains(search)) {
                        musics.add(music);
                    }
                }
                SongLoader.songList = musics;
            }
            initList();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, null);
        listView = (ListView) v.findViewById(R.id.playlist);
        editSearch = (EditText) v.findViewById(R.id.editTextSearch);
        buttonSearch = (Button) v.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(this);
        listener = (IMusicSelected) getActivity();
        initList();
        return v;
    }

    public void initList() {
        adapter = new MusicAdapter(getContext());
        adapter.setMusics(SongLoader.songList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = (Music) parent.getItemAtPosition(position);
                if (music != null) listener.onMusicSelected(music);
            }
        });
    }
}
