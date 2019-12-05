package com.jipjung.hucomin.sinderella.PostActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Adapters.FindModelAdapter;
import com.jipjung.hucomin.sinderella.Adapters.ProductAdapter;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.Fragments.FCart;
import com.jipjung.hucomin.sinderella.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FindModel extends AppCompatActivity {


    private EditText model_search_bar;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private User user;
    private Button model_search_submit_btn;
    private EditText model_name_input;

    private FindModelAdapter mAdapter;
    private List<Product> types;
    private ArrayList<Product> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_search);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        model_search_bar = findViewById(R.id.model_search_bar);
        model_name_input = findViewById(R.id.model_name_input);
        model_search_submit_btn = findViewById(R.id.model_search_submit_btn);
        model_search_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                if(TextUtils.isEmpty(model_name_input.getText())){
                    model_name_input.setError("입력 후 확인을 눌러주세요!");
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("product_name",model_name_input.getText().toString());
                    setResult(50,intent);
                    finish();

                }
            }
        });
        mArrayList = new ArrayList<>();
        types = new ArrayList<Product>();
        user = (User)getIntent().getSerializableExtra("user");

        recyclerView = (RecyclerView)findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new FindModelAdapter(this, mArrayList, R.layout.cart_item_feeds, user);
        getListItems();

        model_search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchWord = model_search_bar.getText().toString()
                        .toLowerCase(Locale.getDefault());
                mAdapter.filter(searchWord);
            }
        });



    }

    public void getListItems() {
        if (!mArrayList.isEmpty()){
            mArrayList.clear();
            mAdapter.arrayList.clear();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("products").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    return;
                                } else {
                                    types = queryDocumentSnapshots.toObjects(Product.class);
                                    mArrayList.addAll(types);
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.arrayList.addAll(mArrayList);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        }, 500);
    }

    public class CustomComparator implements Comparator<Product> {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }


}
