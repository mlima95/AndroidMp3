package com.example.androidmp3.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidmp3.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends BaseAdapter {

    private Context context;
    private List<Music> musics;

    public MusicAdapter(Context context) {
        this.context = context;
        this.musics = new ArrayList<Music>();
    }

    public void addMusic(Music music) {
        musics.add(music);
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    @Override
    public int getCount() {
        return this.musics.size();
    }

    @Override
    public Object getItem(int position) {
        return this.musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_sons, null);
        TextView textArtiste = (TextView) rowView.findViewById(R.id.textViewArtiste);
        textArtiste.setText(musics.get(position).getArtist());
        TextView textTitre =(TextView) rowView.findViewById(R.id.textViewTitre);
        textTitre.setText(musics.get(position).getTitre());
        TextView textDuration =(TextView) rowView.findViewById(R.id.textViewDuration);
        int duration = musics.get(position).getDuration();
        String finalduration = String.format(
                Locale.FRANCE, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );

        textDuration.setText(finalduration);
        return rowView;
    }
}
