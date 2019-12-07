package com.jipjung.hucomin.sinderella.CartActivities;

import android.content.Intent;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Cart;
import com.jipjung.hucomin.sinderella.Classes.Like;
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
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CartDetail extends AppCompatActivity {

    private Product product;
    private User user;
    private Cart cart;

    private ImageView shoes_image;
    private TextView shoes_code;
    private TextView category;
    private TextView item_option;
    private TextView brand;
    private TextView price;
    private int price_int;
    private String price_str;
    private ImageView cart_detail_btn_profile;
    private String product_url;
    private Fragment fr;
    private Bundle productbundle;
    private ToggleButton insert_cart;
    private FirebaseFirestore firebaseFirestore;

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
        cart = (Cart)getIntent().getSerializableExtra("cart");
        shoes_image = findViewById(R.id.shoes_image);
        shoes_code = findViewById(R.id.shoes_code_name);
        category = findViewById(R.id.category);
        brand = findViewById(R.id.brand);
        insert_cart = findViewById(R.id.insert_cart);
        item_option = findViewById(R.id.item_option);
        price = findViewById(R.id.price);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");
        cart_detail_btn_profile = findViewById(R.id.cart_detail_btn_profile);
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        isInCart();
        insert_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("carts").whereEqualTo("product_id",product.getId())
                        .whereEqualTo("user_id",user.getUser_id()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            /* cart데이터가 없을 때 */
                            WriteBatch batch = firebaseFirestore.batch();
                            DocumentReference cart = firebaseFirestore.collection("carts").document();
                            Map<String, Object> docData = new HashMap<>();

                            docData.put("user_id", user.getUser_id());
                            docData.put("product_id", product.getId());
                            docData.put("id", cart.getId());

                            // 댓글 날짜 DB
                            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                            String format = s.format(new Date());

                            docData.put("created_at",format);
                            docData.put("status", "active");
                            insert_cart.setChecked(true);
//                            buttonLike.setImageResource(R.drawable.like_clicked);
//                            post_like_count.setText(String.valueOf(Integer.valueOf(post_like_count.getText().toString())+1));
                            batch.set(cart,docData);
                            batch.commit();

                            Toast.makeText(getApplicationContext(),"카트에 담겼습니다!",Toast.LENGTH_LONG).show();
                        }else{
                            /* 이미 카트에 담았던 상품일 경우 */
                            Cart c = queryDocumentSnapshots.toObjects(Cart.class).get(0);
                            if(c.getStatus().equals("active")){
                                firebaseFirestore.collection("carts").document(c.getId()).update("status", "deactivated");
                                insert_cart.setChecked(false);
                                Toast.makeText(getApplicationContext(),"카트에서 빠졌습니다",Toast.LENGTH_LONG).show();
                            }else{
                                firebaseFirestore.collection("carts").document(c.getId()).update("status", "active");
                                insert_cart.setChecked(true);
                                Toast.makeText(getApplicationContext(),"카트에 담겼습니다!",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        WriteBatch batch = firebaseFirestore.batch();
                        DocumentReference cart = firebaseFirestore.collection("carts").document();
                        Map<String, Object> docData = new HashMap<>();

                        docData.put("user_id", user.getUser_id());
                        docData.put("product_id", product.getId());
                        docData.put("id", cart.getId());

                        // 댓글 날짜 DB
                        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                        String format = s.format(new Date());

                        docData.put("created_at",format);
                        docData.put("status", "active");
                        insert_cart.setChecked(true);
//                            buttonLike.setImageResource(R.drawable.like_clicked);
//                            post_like_count.setText(String.valueOf(Integer.valueOf(post_like_count.getText().toString())+1));
                        batch.set(cart,docData);
                        batch.commit();

                        Toast.makeText(getApplicationContext(),"카트에 담겼습니다!",Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

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
        item_option.setText(product.getOption());
        brand.setText(product.getBrand());
        //price.setText(product.getPrice());

        price_str = NumberFormat.getCurrencyInstance(Locale.KOREA).format(product.getPrice());
        price.setText(price_str);

        Bundle productbundle = new Bundle();
        productbundle.putSerializable("product",product);
        productbundle.putSerializable("user",user);
        fr = new FPostLinkProduct();
        fr.setArguments(productbundle);
        getSupportFragmentManager().beginTransaction().add(R.id.mypage_commet_fragment_container,fr).commit();


    }

    public void isInCart(){
//        firebaseFirestore.collection("carts").whereEqualTo("product_id",product.getId()).whereEqualTo("user_id",user.getUser_id()).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            Cart c = queryDocumentSnapshots.toObjects(Cart.class).get(0);
//                            if(c.getStatus().equals("active")){
//                                insert_cart.setChecked(true);
//                            }
//                        }else{
//                            return;
//                        }
//                    }
//                });
        if(cart!=null && cart.getStatus().equals("active")){
            insert_cart.setChecked(true);
        }
    }
}
