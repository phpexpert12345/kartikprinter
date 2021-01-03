package com.justfoodzorderreceivers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsActivity extends AppCompatActivity {

    LinearLayout liner_back;
    WebView web;
    ProgressBar progressBar;
    ProgressDialog progress;
    ImageView back;
    TextView txt_terms;
    MyPref myPref;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        web = (WebView) findViewById(R.id.wbterms);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        txt_terms=findViewById(R.id.txt_terms);
        web.setWebViewClient(new TermsActivity.myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(Url.website_url);
//        web.loadUrl("https://www.lieferadeal.de");
        myPref = new MyPref(TermsActivity.this);
        back = (ImageView) findViewById(R.id.back);
        liner_back = (LinearLayout) findViewById(R.id.liner_back);
if(myPref.getCustomer_default_langauge().equalsIgnoreCase("de")){
    txt_terms.setText(getString(R.string.terms_condition));
}
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TermsActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        liner_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TermsActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
            web.setVisibility(View.GONE);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
            web.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(TermsActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}