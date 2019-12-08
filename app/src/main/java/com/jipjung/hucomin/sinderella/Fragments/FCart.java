package com.jipjung.hucomin.sinderella.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Adapters.ProductAdapter;
import com.jipjung.hucomin.sinderella.Adapters.RecyclerAdapter;
import com.jipjung.hucomin.sinderella.CartActivities.AddProduct;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCart extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private Button add_model_btn;
    private Button total_btn;
    private Button cart_item_btn;
    private TextView no_product_cart;

    private ProductAdapter mAdapter;
    private List<Product> types;
    private ArrayList<Product> mArrayList;
    private User user;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar pgsBar;

    public FCart(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.cart_fragment, container, false);
        FirebaseFirestore.setLoggingEnabled(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);

        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("user");
        pgsBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mArrayList = new ArrayList<>();
        no_product_cart = v.findViewById(R.id.no_product_cart);
        total_btn = v.findViewById(R.id.total_btn);
        cart_item_btn = v.findViewById(R.id.cart_item_btn);
        add_model_btn = v.findViewById(R.id.add_model_btn);
        types = new ArrayList<Product>();

        //버튼 색 변경

        total_btn.setSelected(true);
        total_btn.setTextColor(Color.WHITE);

        Log.d("click",String.valueOf(total_btn.isSelected()));
        Log.d("click",String.valueOf(cart_item_btn.isSelected()));

        total_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_product_cart.setVisibility(View.GONE);
                Log.d("click","follower_true");
                mAdapter.clear();
                cart_item_btn.setSelected(false);
                cart_item_btn.setTextColor(Color.BLACK);
                total_btn.setSelected(true);
                total_btn.setTextColor(Color.WHITE);
            }
        });

        cart_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click",String.valueOf(cart_item_btn.isSelected()));
                boolean isEmpty = mAdapter.idFilter();
                if(isEmpty){
                    no_product_cart.setVisibility(View.VISIBLE);
                }else{
                    no_product_cart.setVisibility(View.GONE);
                }
                total_btn.setSelected(false);
                total_btn.setTextColor(Color.BLACK);
                cart_item_btn.setSelected(true);
                cart_item_btn.setTextColor(Color.WHITE);

                /*if(cart_item_btn.isSelected()){
                    Log.d("click","following_false");
                    cart_item_btn.setSelected(false);
                    cart_item_btn.setTextColor(Color.BLACK);
                }else{
                    Log.d("click","following_true");
                    total_btn.setSelected(false);
                    total_btn.setTextColor(Color.BLACK);
                    cart_item_btn.setSelected(true);
                    cart_item_btn.setTextColor(Color.WHITE);
                }*/
            }
        });




        //피드 카드뷰 생성
        recyclerView = (RecyclerView) v.findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new ProductAdapter(getContext(), mArrayList, R.layout.cart_item_feeds, user);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(-1) || !recyclerView.canScrollVertically(1)){
                    isScrolling = true;
                }else if(recyclerView.computeVerticalScrollOffset() == 0){
                    isScrolling = true;
                }

//                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    isScrolling = true;
//                }
            }

//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItems = layoutManager.getChildCount();
//                totalItems = layoutManager.getItemCount();
//                scrollOutItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//
//                if(isScrolling && (currentItems + scrollOutItems >= totalItems)){
//                    isScrolling = false;
//                    fetchData();
//                }
//            }
        });

//        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                types = new ArrayList<Product>();
//                getListItems();
//                swipeContainer.setRefreshing(false);
//            }
//        });
        add_model_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddProduct.class);
                startActivity(intent);
            }
        });

//        getFollowList();
        getListItems();
        return v;





    }


    public void getListItems() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
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
//                                    for(int x = 0; x<types.size(); x++){
//                                        String post_id = queryDocumentSnapshots.getDocuments().get(x).getId();
//                                    }
                                    types.sort(new FCart.CustomComparator().reversed());
//                                    if (types.size() < 10) {
                                        mArrayList.addAll(types);
//                                    } else {
//                                        for (int j = 0; j < 10; j++)
//                                            mArrayList.add(types.get(j));
//                                    }
                                    recyclerView.setAdapter(mAdapter);
                                    mAdapter.arrayList.addAll(mArrayList);
                                    pgsBar.setVisibility(ProgressBar.GONE);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pgsBar.setVisibility(ProgressBar.GONE);
                            }
                        });
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pgsBar != null)
            pgsBar.setVisibility(ProgressBar.GONE);
    }

    private void fetchData() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = mArrayList.size();
                for (int i = 0; i < 5; i++) {
                    if (k + i < types.size()) {
                        mArrayList.add(types.get(k + i));
                        mAdapter.notifyDataSetChanged();
                        pgsBar.setVisibility(ProgressBar.GONE);
                    } else {
                        pgsBar.setVisibility(ProgressBar.GONE);
                        return;
                    }
                }
            }
        }, 1000);
    }

    public class CustomComparator implements Comparator<Product> {
        @Override
        public int compare(Product o1, Product o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }


}
