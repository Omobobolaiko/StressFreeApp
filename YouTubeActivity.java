package com.example.baads.youtubePlayer;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.baads.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.security.KeyPairGenerator;


public class YouTubeActivity extends YouTubeBaseActivity {

    Button btn;
    Button btnChangeVideo;
    Button btnStressVideo3;
    Button btnStressVideo4;
    Button btnStressVideo5;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayerView youTubePlayerView2;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    YouTubePlayer.OnInitializedListener onInitializedListener2;
    YouTubePlayer mYouTubePlayer;
    YouTubePlayer youTubePlayer2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        final String videoId = "lrhPTqholcc";

        btn = findViewById(R.id.play);
        btnChangeVideo = findViewById(R.id.changeVideo);
        btnStressVideo3 = findViewById(R.id.stressVideo3);
        btnStressVideo4 = findViewById(R.id.stressVideo4);
        btnStressVideo5 = findViewById(R.id.stressVideo5);
        youTubePlayerView = findViewById(R.id.YoutubePlayerView);
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTubePlayer.cueVideo("lrhPTqholcc");
            }

        });
        //8TuRYV71Rgo
        btnChangeVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTubePlayer.cueVideo("8TuRYV71Rgo");
            }

        });
        btnStressVideo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTubePlayer.cueVideo("w4tlGeSrcNw");
            }

        });
        btnStressVideo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTubePlayer.cueVideo("oWPQJJ_n3Sk");
            }

        });
        btnStressVideo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYouTubePlayer.cueVideo("Sqe3h3l8a7w");
            }

        });
        youTubePlayerView.initialize("AIzaSyALdZO1D-c0JsQ4pDMlHzLHsfN8CPRFKAI", onInitializedListener);

    }
    private void youTubePlayerSetup(){


    }
}