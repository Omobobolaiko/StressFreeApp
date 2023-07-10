package com.example.baads.wikiTips;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.baads.R;

import com.example.baads.databinding.FragmentWikiTipsReworkedBinding;

public class wikiTipsFragmentReworked extends Fragment {

    private FragmentWikiTipsReworkedBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentWikiTipsReworkedBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView newWebView = (WebView) getActivity().findViewById(R.id.wikiV);
        newWebView.setWebViewClient(new WebViewClient());
        newWebView.loadUrl("https://www.wikihow.com/Deal-With-Stress");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}