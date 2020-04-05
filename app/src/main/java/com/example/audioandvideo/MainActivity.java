package com.example.audioandvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderClient;
import android.icu.text.CaseMap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlayVideo;
    private Button btnPlayMusic;
    private Button btnPauseMusic;
    private Uri uri;
    private VideoView myVideo;

    private MediaController mediaController;
    private MediaPlayer mediaPLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVideo=findViewById(R.id.myVideoView);
        btnPlayMusic=findViewById(R.id.btnPlayMusic);
        btnPauseMusic=findViewById(R.id.btnPauseMusic);
        btnPlayVideo=findViewById(R.id.btnPlayVideo);

        mediaPLayer= MediaPlayer.create(MainActivity.this,R.raw.rain);
        mediaController=new MediaController(MainActivity.this);

        btnPauseMusic.setOnClickListener(this);
        btnPlayMusic.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnPlayVideo:
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
                myVideo.setVideoURI(uri);
                myVideo.setMediaController(mediaController);
                mediaController.setAnchorView(myVideo);
                myVideo.start();
                break;

            case R.id.btnPlayMusic:
                mediaPLayer.start();
                break;

            case R.id.btnPauseMusic:
//                mediaPLayer.stop();
                mediaPLayer.pause();
                break;

        }
    }
}
