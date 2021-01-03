package com.justfoodzorderreceivers.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.justfoodzorderreceivers.MainActivity;
import com.justfoodzorderreceivers.R;

public class  About_us extends Fragment {
    WebView wb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.aboutus,container,false);
        wb = (WebView) v.findViewById(R.id.wb);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(""+ MainActivity.webUrl);
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return v;
    }
}
