package com.jipjung.hucomin.sinderella.InAppBrowser;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.jipjung.hucomin.sinderella.R;

import androidx.appcompat.app.AppCompatActivity;

public class InAppBrowser extends AppCompatActivity {

    private String url;
    private EditText inputurl;
    private Button action_bar_back_close;
    private String weburi;
    private Button go;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app_browser);
        go = findViewById(R.id.go);
        webView = (WebView)findViewById(R.id.webview);

        inputurl = findViewById(R.id.uri);
        url = getIntent().getStringExtra("url");
        inputurl.setText(url);
//        inputurl.setWidth();
        goURL(null,url);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weburi = inputurl.getText().toString();
                if(weburi.startsWith("http://")) {
                    webView.loadUrl(weburi);
                } else {
                    webView.loadUrl("http://" + weburi); }
            }
        });

        action_bar_back_close = findViewById(R.id.action_bar_back_close);

        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void goURL(View view, String url){
        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        if(url.startsWith("http://")) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("http://" + url); }

    }



}
