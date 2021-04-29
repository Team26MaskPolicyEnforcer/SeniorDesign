package com.example.maskenforcerpolicyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.longtailvideo.jwplayer.*;
import com.longtailvideo.jwplayer.configuration.PlayerConfig;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;

import java.util.*;

public class FootageActivity extends AppCompatActivity {


    JWPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footage);

        mPlayerView = findViewById(R.id.jwplayer);
        PlaylistItem playlistItem = new PlaylistItem.Builder()
                .file("https://cdn.jwplayer.com/manifests/{MqEae6xw}.m3u8")
                .build();

        List<PlaylistItem> playlist = new ArrayList<>();
        playlist.add(playlistItem);
        PlayerConfig config = new PlayerConfig.Builder()
                .playlist(playlist)
                .build();
        mPlayerView.setup(config);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mPlayerView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayerView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerView.onDestroy();
    }
}
