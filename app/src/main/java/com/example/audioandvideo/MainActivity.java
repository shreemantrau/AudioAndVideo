package com.example.audioandvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderClient;
import android.icu.text.CaseMap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {
//UI Comps
    private Button btnPlayVideo;
    private Button btnPlayMusic;
    private Button btnPauseMusic;
    private Uri uri;
    private VideoView myVideo;
    private SeekBar volumeSeekBar;
    private SeekBar moveSeekbar;
    private MediaController mediaController;
    private MediaPlayer mediaPLayer;
    private AudioManager audioManager;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myVideo=findViewById(R.id.myVideoView);
        volumeSeekBar=findViewById(R.id.seekBarVolume);
        moveSeekbar=findViewById(R.id.seekBarMove);
        btnPlayMusic=findViewById(R.id.btnPlayMusic);
        btnPauseMusic=findViewById(R.id.btnPauseMusic);
        btnPlayVideo=findViewById(R.id.btnPlayVideo);

        mediaPLayer= MediaPlayer.create(MainActivity.this,R.raw.rain);
        mediaController=new MediaController(MainActivity.this);
        audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);

        int maxVolume=audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
//        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if(fromUser){
//                    //Toast.makeText(MainActivity.this,Integer.toString(progress),Toast.LENGTH_SHORT).show();
//                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
 //we have implemented the setOnSeekBarChangeListener so this is the way to
 //the methods that are being implemented. The other way to do it is at the top commented(line 52-70 anonymous class).

        moveSeekbar.setOnSeekBarChangeListener(MainActivity.this);
        moveSeekbar.setMax(mediaPLayer.getDuration());

        volumeSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(this);
        btnPlayMusic.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);
        mediaPLayer.setOnCompletionListener(this);

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
                //adding thread to update the seekbar as the music progresses.
                timer=new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveSeekbar.setProgress(mediaPLayer.getCurrentPosition());
                    }
                },0,1000);
                break;

            case R.id.btnPauseMusic:
//                mediaPLayer.stop();
                mediaPLayer.pause();
                timer.cancel();
                break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()) {
            case R.id.seekBarVolume:
                if (fromUser) {
//                    Toast.makeText(this, progress + "", Toast.LENGTH_SHORT).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                    break;
                }
            case R.id.seekBarMove:
                if(fromUser){
//                    Toast.makeText(this,Integer.toString(progress),Toast.LENGTH_SHORT).show();
                    moveSeekbar.setProgress(mediaPLayer.getCurrentPosition());
                    mediaPLayer.seekTo(progress);
                }

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mediaPLayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPLayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
    }
}
