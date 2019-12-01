package com.jipjung.hucomin.sinderella.Search;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jipjung.hucomin.sinderella.Fragments.FSearchResult;
import com.jipjung.hucomin.sinderella.R;

public class SearchActivity extends AppCompatActivity{

    private ImageButton search_out_button;
    private ImageButton search_Enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        search_Enter = findViewById(R.id.search_Enter);

        search_Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("movepage","what");
                Intent intent =new Intent(SearchActivity.this,FSearchResult.class);
                startActivity(intent);
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