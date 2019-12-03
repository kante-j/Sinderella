package com.jipjung.hucomin.sinderella.CartActivities;

import android.content.Intent;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.Fragments.FPostLinkProduct;
import com.jipjung.hucomin.sinderella.InAppBrowser.InAppBrowser;
import com.jipjung.hucomin.sinderella.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CartDetail extends AppCompatActivity {

    private Product product;
    private User user;

    private ImageView shoes_image;
    private TextView shoes_code;
    private TextView category;
    private TextView brand;
    private TextView price;
    private ImageView cart_detail_btn_profile;
    private String product_url;
    private Fragment fr;
    private Bundle productbundle;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Button action_bar_back_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_detail);
        action_bar_back_close = findViewById(R.id.action_bar_back_close);
        product = (Product)getIntent().getSerializableExtra("product");
        user = (User)getIntent().getSerializableExtra("user");
        shoes_image = findViewById(R.id.shoes_image);
        shoes_code = findViewById(R.id.shoes_code_name);
        category = findViewById(R.id.category);
        brand = findViewById(R.id.brand);
        price = findViewById(R.id.price);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");
        cart_detail_btn_profile = findViewById(R.id.cart_detail_btn_profile);

        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cart_detail_btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartDetail.this, InAppBrowser.class);
                intent.putExtra("url", product.getProduct_url());
                startActivity(intent);
            }
        });
        product_url = product.getProduct_url();

//        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                types = new ArrayList<Product>();
//                swipeContainer.setRefreshing(false);
//            }
//        });

        if (product.getImage_url() != null) {
            StorageReference path = storageRef.child(product.image_url);
            Glide.with(this).load(path).skipMemoryCache(true).into(shoes_image);
//            holder.url = product.getImage_url();
        }

        shoes_code.setText(product.getName());
        category.setText(product.getCategory());
        brand.setText(product.getBrand());
        price.setText(product.getPrice() + "원");

        Bundle productbundle = new Bundle();
        productbundle.putSerializable("product",product);
        productbundle.putSerializable("user",user);
        fr = new FPostLinkProduct();
        fr.setArguments(productbundle);
        getSupportFragmentManager().beginTransaction().add(R.id.mypage_commet_fragment_container,fr).commit();


    }
}
