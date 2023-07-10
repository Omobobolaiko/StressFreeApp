package com.example.baads.soundBar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;


import com.example.baads.R;
import com.example.baads.databinding.ActivitySoundBarBinding;


public class soundBarReworkFragment extends Fragment {

    private ActivitySoundBarBinding binding;
    MediaPlayer oceanNoise;
    MediaPlayer whiteNoise;
    MediaPlayer guitarMusic;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ActivitySoundBarBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //white and ocean noise mp3 files pulled from https://mc2method.org/white-noise/
        //guitar music pulled from https://pixabay.com/music/search/mood/relaxing/
        AppCompatImageButton oceanButtonPlay = getActivity().findViewById(R.id.playOcean);
        oceanButtonPlay.setOnClickListener(e-> oceanStart());


        AppCompatImageButton oceanButtonPause = getActivity().findViewById(R.id.pauseOcean);
        oceanButtonPause.setOnClickListener(e->oceanNoise.stop());

        AppCompatImageButton whiteNoisePlay = getActivity().findViewById(R.id.playWhite);
        whiteNoisePlay.setOnClickListener(e->whiteStart());


        AppCompatImageButton whiteNoisePause = getActivity().findViewById(R.id.pauseWhite);
        whiteNoisePause.setOnClickListener(e->whiteNoise.stop());

        AppCompatImageButton guitarMusicPlay = getActivity().findViewById(R.id.playGuitar);
        guitarMusicPlay.setOnClickListener(e->guitarStart());

        AppCompatImageButton guitarMusicPause = getActivity().findViewById(R.id.pauseGuitar);
        guitarMusicPause.setOnClickListener(e->guitarMusic.stop());
    }

    private void oceanStart(){
        oceanNoise = MediaPlayer.create(getActivity(), R.raw.ocean_noise);
        oceanNoise.start();
    }

    private void whiteStart(){
        whiteNoise = MediaPlayer.create(getActivity(), R.raw.white_noise);
        whiteNoise.start();
    }

    private void guitarStart(){
        guitarMusic = MediaPlayer.create(getActivity(), R.raw.guitar_music);
        guitarMusic.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}