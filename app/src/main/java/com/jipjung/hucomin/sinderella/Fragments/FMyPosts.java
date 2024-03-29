package com.jipjung.hucomin.sinderella.Fragments;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;


import com.jipjung.hucomin.sinderella.Adapters.Filterarrayadapter;
import com.jipjung.hucomin.sinderella.Adapters.RecyclerAdapter;
import com.jipjung.hucomin.sinderella.Adapters.SpinnerAdapter;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.HomeActivities.HomeFeed;
import com.jipjung.hucomin.sinderella.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class FMyPosts extends Fragment {
    private FirebaseFirestore fs;
    //    static final int LIMIT = 50;
    private ArrayList<Post> mArrayList;
    private List<Post> types;
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    private User user;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar pgsBar;


    private CheckBox small_foot_checkbox;
    private CheckBox normal_foot_checkbox;
    private CheckBox bigger_foot_checkbox;
    private ListView listView;


    public FMyPosts() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.home_fragments, container, false);
        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();
        pgsBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        Bundle bundle = getArguments();
        mArrayList = new ArrayList<>();

        user = (User) bundle.getSerializable("user");
        //피드 카드뷰 생성
        recyclerView = (RecyclerView) v.findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(getContext(), mArrayList, R.layout.home_fragments, user);



        //데이터 정렬
        getListItems();

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

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems >= totalItems)){
                    isScrolling = false;
                    fetchData();
                }
            }
        });

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipe_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListItems();
                swipeContainer.setRefreshing(false);
            }
        });
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(pgsBar != null)
            pgsBar.setVisibility(ProgressBar.GONE);
    }

    private void fetchData() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int k = mArrayList.size();
                for (int i =0 ; i < 5; i++){
                    if(k + i < types.size()){
                        mArrayList.add(types.get(k + i));
                        mAdapter.notifyDataSetChanged();
                        pgsBar.setVisibility(ProgressBar.GONE);
                    }
                    else{
                        pgsBar.setVisibility(ProgressBar.GONE);
                        return;
                    }
                }
            }
        }, 1000);
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
                fs.collection("posts").whereEqualTo("user_id",user.getUser_id()).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    return;
                                } else {
                                    types = queryDocumentSnapshots.toObjects(Post.class);
                                    for(int x = 0; x<types.size(); x++){
                                        String post_id = queryDocumentSnapshots.getDocuments().get(x).getId();
                                        types.get(x).withId(post_id);
                                    }
                                    types.sort(new CustomComparator().reversed());
                                    if (types.size() < 10) {
//                                        for (int i = 0; i < types.size(); i++) {
//                                            mArrayList.add(types.get(i));
//                                        }
                                        mArrayList.addAll(types);
                                    } else {
                                        for (int j = 0; j < 10; j++)
                                            mArrayList.add(types.get(j));
                                    }
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

    public class CustomComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }
}
