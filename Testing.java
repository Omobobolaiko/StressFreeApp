package com.example.baads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

//Change FragmentSecondBinding to your page. It will follow the Pattern of FragmentNAMEOFYOURPAGEBinding
import com.example.baads.databinding.FragmentTestingBinding;

public class Testing extends Fragment {

    //Change FragmentSecondBinding to what you changed above to.
    private FragmentTestingBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        //Change FragmentSecondBinding to what you changed above to.
        binding = FragmentTestingBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}