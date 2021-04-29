package com.example.maskenforcerpolicyapp;

import androidx.appcompat.app.*;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class RecordedActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
    VideoView vw;
    ArrayList<Integer> videoList = new ArrayList<>();
    ArrayAdapter adapter;
    ListView videoListView;
    int currvideo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded);
        vw = (VideoView)findViewById(R.id.videoView);
        videoListView = (ListView)findViewById(R.id.videoListView);
        vw.setMediaController(new MediaController(this));
        vw.setOnCompletionListener(this);
        videoList.add(R.raw.output_video);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,videoList);
        videoListView.setAdapter(adapter);
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        setVideo(videoList.get(0));
                        break;
                    case 1:
                        setVideo(videoList.get(1));
                        break;
                    case 2:
                        setVideo(videoList.get(2));
                        break;
                    case 3:
                        setVideo(videoList.get(3));
                        break;
                    case 4:
                        setVideo(videoList.get(4));
                        break;
                    case 5:
                        setVideo(videoList.get(5));
                        break;
                    case 6:
                        setVideo(videoList.get(6));
                        break;
                    case 7:
                        setVideo(videoList.get(7));
                        break;
                    case 8:
                        setVideo(videoList.get(8));
                        break;
                    case 9:
                        setVideo(videoList.get(9));
                        break;
                    case 10:
                        setVideo(videoList.get(10));
                        break;//add more cases as needed
                }
            }
        });
    }
    public void setVideo(int id)
    {
        String uriPath
                = "android.resource://"
                + getPackageName() + "/" + id;
        Uri uri = Uri.parse(uriPath);
        vw.setVideoURI(uri);
        vw.start();
    }

    public void onCompletion(MediaPlayer mediaplayer)
    {
        AlertDialog.Builder obj = new AlertDialog.Builder(this);
        obj.setTitle("Playback Finished!");
        obj.setIcon(R.mipmap.ic_launcher);
        MyListener m = new MyListener();
        obj.setPositiveButton("Replay", m);
        obj.setNegativeButton("Next", m);
        obj.setMessage("Want to replay or play next video?");
        obj.show();
    }
    class MyListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which)
        {
            if (which == -1) {
                vw.seekTo(0);
                vw.start();
            }
            else {
                ++currvideo;
                if (currvideo == videoList.size())
                    currvideo = 0;
                setVideo(videoList.get(currvideo));
            }
        }
    }

}