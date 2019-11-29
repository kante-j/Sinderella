package com.jipjung.hucomin.sinderella.InAppBrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jipjung.hucomin.sinderella.R;

public class InAppBrowser extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);

        url = getIntent().getStringExtra("url");
        goURL(null,url);
    }

    public void goURL(View view, String url){
        WebView webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        webView.loadUrl(url);

    }

}
