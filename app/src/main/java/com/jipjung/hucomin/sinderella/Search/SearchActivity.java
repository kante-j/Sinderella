package com.jipjung.hucomin.sinderella.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jipjung.hucomin.sinderella.R;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private ImageButton search_out_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        search_out_button = findViewById(R.id.search_out_button);
        search_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}