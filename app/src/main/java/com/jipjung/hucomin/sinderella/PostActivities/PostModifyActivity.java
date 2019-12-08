package com.jipjung.hucomin.sinderella.PostActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapWell;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PostModifyActivity extends AppCompatActivity {

    private User user;
    private Post post;
    private Product product;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //Rating bar
    private RatingBar ratingBarForShoes;
    private float rating;
    // RadioButton
    private RadioGroup shoes_weight_radiogroup;
    private RadioGroup shoe_size_radiogroup;
    private RadioGroup vantilation_radiogroup;
    private RadioGroup waterproof_radiogroup;
    private String buyURLString;
    private int price;
    private File tempFile;

    private String shoes_weight;
    private String shoe_size;
    private String vantilation;
    private String waterproof;

    private Button find_model_btn;
    private Spinner categorySpinner;
    private TextView text_title;
    private EditText text_context;
    private Spinner shoesSizeSpinner;

    private Button postingButton;
    private BootstrapWell model_name;
    private ImageView imageView;

    private TextView buyURL;

    private TextView priceTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);

        user = (User)getIntent().getSerializableExtra("user");
        post = (Post)getIntent().getSerializableExtra("post");
        product = (Product)getIntent().getSerializableExtra("product");
        buyURL = findViewById(R.id.buyURL);
        mFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");
        text_context = findViewById(R.id.text_context);
        imageView = (ImageView) findViewById(R.id.postImage);
        find_model_btn  = findViewById(R.id.find_model_btn);
        postingButton = findViewById(R.id.btn_posting);
        model_name = findViewById(R.id.model_name);
        text_title = findViewById(R.id.text_title);
        ratingBarForShoes = findViewById(R.id.ratingForShoes);
        shoes_weight_radiogroup = findViewById(R.id.shoes_weight);
        ImageButton xButton = findViewById(R.id.xButtoninPosting);
        shoesSizeSpinner = findViewById(R.id.foot_size_correction);
        categorySpinner = findViewById(R.id.foot_size_correction2);


        ImageView btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setVisibility(View.GONE);

        imageView.setVisibility(View.VISIBLE);
        StorageReference path = storageReference.child(post.getImageURL());
        Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);

        find_model_btn.setVisibility(View.GONE);
        model_name.setVisibility(View.VISIBLE);
        text_title.setText(product.getName());
        text_title.setEnabled(false);

        ratingBarForShoes.setRating(post.rating);


        String compareValue = String.valueOf(post.getShoe_size_num());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.foot_size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shoesSizeSpinner.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            shoesSizeSpinner.setSelection(spinnerPosition);
        }

        buyURL.setText(post.getBuyURL());

        priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText(String.valueOf(post.getPrice()));

        text_context.setText(post.getBody());

        postingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
//                if(imageView.getDrawable()==null) {
//                        writeNewPost(uid, nickname, text_title.getText().toString(), text_context.getText().toString());
//                    if(validatePost())
//                        finish();
//                }else{
                if(validatePost())
                    updatePost();
            }
        });


        if(post.getShoes_weight().equals("light")){
            shoes_weight="light";
            shoes_weight_radiogroup.check(R.id.shoes_weight_light);
        }else if(post.getShoes_weight().equals("fit")){
            shoes_weight="fit";
            shoes_weight_radiogroup.check(R.id.shoes_weight_fit);
        }else{
            shoes_weight="heavy";
            shoes_weight_radiogroup.check(R.id.shoes_weight_heavy);
        }
        shoe_size_radiogroup = findViewById(R.id.shoes_size);
        if(post.getShoes_size().equals("big")){
            shoe_size="big";
            shoe_size_radiogroup.check(R.id.shoes_size_big);
        }else if(post.getShoes_size().equals("fit")){
            shoe_size="fit";
            shoe_size_radiogroup.check(R.id.shoes_size_fit);
        }else{
            shoe_size="small";
            shoe_size_radiogroup.check(R.id.shoes_size_small);
        }

        vantilation_radiogroup = findViewById(R.id.ventilation);
        if(post.getVentilation().equals("good")){
            vantilation = "good";
            vantilation_radiogroup.check(R.id.ventilation_good);
        }else if(post.getVentilation().equals("best")){
            vantilation = "best";
            vantilation_radiogroup.check(R.id.ventilation_best);
        }else{
            vantilation = "bad";
            vantilation_radiogroup.check(R.id.ventilation_bad);
        }

        waterproof_radiogroup = findViewById(R.id.waterproof);
        if(post.getWaterproof().equals("bad")){
            waterproof = "bad";
            waterproof_radiogroup.check(R.id.waterproof_bad);
        }else if(post.getWaterproof().equals("best")){
            waterproof = "best";
            waterproof_radiogroup.check(R.id.waterproof_best);
        }else{
            waterproof = "good";
            waterproof_radiogroup.check(R.id.waterproof_good);
        }

        shoes_weight_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shoes_weight_fit:
                        shoes_weight = "fit";
                        break;
                    case R.id.shoes_weight_heavy:
                        shoes_weight = "heavy";
                        break;
                    case R.id.shoes_weight_light:
                        shoes_weight = "light";
                        break;
                }

            }
        });

        shoe_size_radiogroup = findViewById(R.id.shoes_size);
        shoe_size_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.shoes_size_big:
                        shoe_size = "big";
                        break;
                    case R.id.shoes_size_fit:
                        shoe_size = "fit";
                        break;
                    case R.id.shoes_size_small:
                        shoe_size = "small";
                }

            }
        });

        vantilation_radiogroup = findViewById(R.id.ventilation);
        vantilation_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ventilation_good:
                        vantilation = "good";
                        break;
                    case R.id.ventilation_best:
                        vantilation = "best";
                        break;
                    case R.id.ventilation_bad:
                        vantilation = "bad";
                        break;
                }
            }
        });

        waterproof_radiogroup = findViewById(R.id.waterproof);
        waterproof_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.waterproof_bad:
                        waterproof = "bad";
                        break;
                    case R.id.waterproof_best:
                        waterproof= "best";
                        break;
                    case R.id.waterproof_good:
                        waterproof="good";
                        break;
                }
            }
        });

    }

    private void updatePost(){
        rating = ratingBarForShoes.getRating();
        buyURLString = buyURL.getText().toString();
        price = Integer.valueOf(priceTextView.getText().toString());
        int shoe_size_num = Integer.valueOf(shoesSizeSpinner.getSelectedItem().toString().substring(0,3));

        DocumentReference posts = mFirestore.collection("posts").document(post.getId());

        posts.update("body",text_context.getText().toString());
        posts.update("shoe_size_num",shoe_size_num);
        posts.update("waterproof",waterproof);
        posts.update("ventilation",vantilation);
        posts.update("shoes_size",shoe_size);
        posts.update("shoes_weight",shoes_weight);
        posts.update("rating",rating);
        posts.update("price",price);
        posts.update("buyURL",buyURLString);

        finish();
    }

    public boolean validatePost(){
        boolean valid = true;

        Log.d("qweqwe 1",String.valueOf(valid));
        if(TextUtils.isEmpty(priceTextView.getText())){
            priceTextView.setError("가격을 입력하세요!");
            valid = false;
        }else
            text_title.setError(null);
        if(TextUtils.isEmpty(buyURL.getText())){
            buyURL.setError("구입처를 입력하세요!");
            valid = false;
        }else
            text_title.setError(null);

        return valid;
    }
}
