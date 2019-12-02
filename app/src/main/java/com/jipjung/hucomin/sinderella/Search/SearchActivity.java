package com.jipjung.hucomin.sinderella.Search;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jipjung.hucomin.sinderella.Fragments.FSearchResult;
import com.jipjung.hucomin.sinderella.R;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private ImageButton search_out_button;
    private ImageButton search_Enter;
    private TextView search_context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        search_context = findViewById(R.id.search);

        search_Enter = findViewById(R.id.search_Enter);
        search_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent();
                intent.putExtra("search_context",search_context.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        search_context.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    default:
                        // 기본 엔터키 동작
                        search_Enter.callOnClick();
                        break;
                }
                return true;
            }
        });

        search_out_button = findViewById(R.id.search_out_button);
        search_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}