package com.example.androidmp3.fragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.androidmp3.R;
import java.io.File;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import android.view.LayoutInflater;

public class DownloadFragment extends Fragment implements View.OnClickListener {
    private static String youtubeLink;
    private EditText EditDownload;
    private Button btnDownload;
    private LinearLayout DownloadLayout;
    private String val;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, null);
        AndroidAudioConverter.load(getContext(), new ILoadCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText((getContext()), "Library AudioConverter loaded successfully", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Exception error) {
                Toast.makeText((getContext()), "Library failed to load", Toast.LENGTH_LONG).show();
            }
        });
         getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        DownloadLayout = (LinearLayout) v.findViewById(R.id.LinearLayoutDownload);
        EditDownload = (EditText) v.findViewById(R.id.EditDownload);
        btnDownload = (Button) v.findViewById(R.id.BtnDownload);
        btnDownload.setOnClickListener(this);
        return v;
    }

    private void getYoutubeDownloadUrl (String youtubeLink){
        new YouTubeExtractor(getContext()) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {

                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    return;
                }
                // Iterate over itags

                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1) {
                        String filename;
                        String videoTitle = vMeta.getTitle();
                        if (videoTitle.length() > 55) {
                            filename = videoTitle.substring(0, 55) + "." + ytFile.getFormat().getExt();
                        } else {
                            filename = videoTitle + "." + ytFile.getFormat().getExt();
                        }
                        filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
//                            Log.e("Video", videoTitle);
//                            Log.e("Format", ytFile.getFormat().getExt());
                        val=filename;
                        downloadFromUrl(ytFile.getUrl(), videoTitle, filename);
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }


    private void downloadFromUrl (String youtubeDlUrl, String downloadTitle, String fileName){

        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName);

        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            final File flacFile = new File("/storage/emulated/0/Music/", val);
            String file = "mon fichier"+flacFile;

            IConvertCallback callback = new IConvertCallback() {
                @Override
                public void onSuccess(File convertedFile) {
                    // So fast? Love it!
                    flacFile.delete();
                    Toast.makeText((getContext()), convertedFile+" converted and dowloaded successfully", Toast.LENGTH_LONG).show();
                    String test = "un test"+ convertedFile;
                    Log.e("essaie", test);
                }
                @Override
                public void onFailure(Exception error) {
                    // Oops! Something went wrong
                    String error1 = "error "+ error;
                    Log.e("error",error1);
                }

            };
            AndroidAudioConverter.with(ctxt)
                    // Your current audio file
                    .setFile(flacFile)

                    // Your desired audio format
                    .setFormat(AudioFormat.MP3)

                    // An callback to know when conversion is finished
                    .setCallback(callback)

                    // Start conversion
                    .convert();
        }
    };

    @Override
    public void onClick (View v){
        youtubeLink = EditDownload.getText().toString();
        getYoutubeDownloadUrl(youtubeLink);
    }
}
