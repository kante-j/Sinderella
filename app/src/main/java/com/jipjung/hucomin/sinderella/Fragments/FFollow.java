package com.jipjung.hucomin.sinderella.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Adapters.FollowArrayAdapter;
import com.jipjung.hucomin.sinderella.Adapters.RecyclerAdapter;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FFollow extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private TextView is_no_follower;
//    private LinearLayout layout;
    private RecyclerAdapter mAdapter;
    private List<Post> types;
    private ArrayList<Post> mArrayList;
    private User user;
    private List<Follow> followList;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar pgsBar;
    private RecyclerView list_follow;

    public FFollow() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.follow_fragment, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);



        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("user");

        pgsBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mArrayList = new ArrayList<>();
        types = new ArrayList<Post>();
        is_no_follower = v.findViewById(R.id.is_no_follower);




        //피드 카드뷰 생성
        recyclerView = (RecyclerView) v.findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(getContext(), mArrayList, R.layout.follow_fragment, user);

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

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFollowList();
                types = new ArrayList<Post>();
                getListItems();
                swipeContainer.setRefreshing(false);
            }
        });

//        layout = (LinearLayout) v.findViewById(R.id.follow_linear_layout);
////        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        for (int i = 0; i < 1; i++) {
////            layoutParams.setMargins(20, 20, 20, 20);
////            layoutParams.gravity = Gravity.CENTER;
//            ImageView imageView = new ImageView(getActivity());
//
//            imageView.setImageResource(R.drawable.person_circle);
////            imageView.setOnClickListener(documentImageListener);
////            imageView.setLayoutParams(layoutParams);
//            imageView.setLayoutParams(
//                    new ViewGroup.LayoutParams(
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT
//                            (int)dpToPx(getActivity(), 75),
//                            // or ViewGroup.LayoutParams.WRAP_CONTENT,
//                            (int)dpToPx(getActivity(), 75) ) );
//
//            layout.addView(imageView);
//
//        }

        getFollowList();
        getListItems();

        //TODO: follow Horizantal RecyclerView

        list_follow = v.findViewById(R.id.list_follow);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_follow.setLayoutManager(linearLayoutManager);


        //TODO: 없애시오
        ArrayList<String> items = new ArrayList<>();
        items.add("Hard");
        items.add("DBHard");
        items.add("Please");
        items.add("Sorry");
        items.add("Maybe");
        items.add("Sorry");
        items.add("Maybe");
        items.add("Sorry");
        items.add("Maybe");
        // 여기까지







        return v;
    }
    public float dpToPx(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public void getListItems() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        if (!mArrayList.isEmpty()) {
            mArrayList.clear();
            mAdapter.arrayList.clear();
        }
    }

    public void getFollowList() {

        firebaseFirestore.collection("follows").whereEqualTo("follower_id", user.getUser_id())
                .whereEqualTo("status", "active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        followList = queryDocumentSnapshots.toObjects(Follow.class);
                        if((followList.size() ==0)){
                            is_no_follower.setVisibility(View.VISIBLE);
                        }
                        FollowArrayAdapter followArrayAdapter = new FollowArrayAdapter(getContext(),0, followList, user);
                        list_follow.setAdapter(followArrayAdapter);
//                        String[] followarr = new String[];
                        ArrayList<String> followarr = new ArrayList<String>();
                        for(Iterator<Follow> it = followList.iterator(); it.hasNext(); ) {
                            Follow follow = it.next();
                            followarr.add(follow.getFollowed_id());
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firebaseFirestore.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<Post> postList = queryDocumentSnapshots.toObjects(Post.class);
                                        for(Iterator<Post> it = postList.iterator(); it.hasNext(); ) {
                                            Post post = it.next();
                                            if(followarr.contains(post.getUser_id())) {
                                                types.add(post);

                                            }
                                            Log.d("qweqwe",String.valueOf(types.size()));
                                        }
                                        types.sort(new FFollow.CustomComparator().reversed());
                                        if (types.size() < 10) {
                                            mArrayList.addAll(types);
                                        } else {
                                            for (int j = 0; j < 10; j++)
                                                mArrayList.add(types.get(j));
                                        }
                                        recyclerView.setAdapter(mAdapter);
                                        mAdapter.arrayList.addAll(mArrayList);
                                        pgsBar.setVisibility(ProgressBar.GONE);


                                    }
                                });

                            }

                        }, 500);

                    }
                });

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

    public class CustomComparator implements Comparator<Post> {
        @Override
        public int compare(Post o1, Post o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
        }
    }
}