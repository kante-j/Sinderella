package com.jipjung.hucomin.sinderella.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Adapters.RecyclerAdapter;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FFollow extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerAdapter mAdapter;
    private List<Post> types;
    private ArrayList<Post> mArrayList;
    private User user;
    private List<Follow> followList;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar pgsBar;

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

        //피드 카드뷰 생성
        recyclerView = (RecyclerView) v.findViewById(R.id.feeds);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new RecyclerAdapter(getContext(), mArrayList, R.layout.follow_fragment, user);

        getFollowList();
        getListItems();
        return v;
    }

    public void getListItems() {
        pgsBar.setVisibility(ProgressBar.VISIBLE);
        if (!mArrayList.isEmpty()) {
            mArrayList.clear();
            mAdapter.arrayList.clear();
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < followList.size(); i++) {
//                    Log.d("qweqwe f",String.valueOf(followList.get(i).getFollowed_id()));
//                    firebaseFirestore.collection("posts").whereEqualTo("user_id", followList.get(i).getFollowed_id()).get()
//                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                @Override
//                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                    if (queryDocumentSnapshots.isEmpty()) {
//                                        return;
//                                    } else {
//                                        types.addAll(queryDocumentSnapshots.toObjects(Post.class));
//                                        Log.d("qweqwe",String.valueOf(types.size()));
////                                        types = queryDocumentSnapshots.toObjects(Post.class);
////                                        for (int x = 0; x < types.size(); x++) {
////                                            String post_id = queryDocumentSnapshots.getDocuments().get(x).getId();
////                                            types.get(x).withId(post_id);
////                                        }
////                                        types.sort(new FFollow.CustomComparator().reversed());
////                                        if (types.size() < 10) {
//////                                        for (int i = 0; i < types.size(); i++) {
//////                                            mArrayList.add(types.get(i));
//////                                        }
////                                            mArrayList.addAll(types);
////                                        } else {
////                                            for (int j = 0; j < 10; j++)
////                                                mArrayList.add(types.get(j));
////                                        }
////                                        recyclerView.setAdapter(mAdapter);
////                                        mAdapter.arrayList.addAll(mArrayList);
////                                        pgsBar.setVisibility(ProgressBar.GONE);
//                                    }
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d("qweqweqwe", "아무내용이없습니다");
//                                    pgsBar.setVisibility(ProgressBar.GONE);
//                                }
//                            });
//                }
//
//            }
//        }, 500);
    }

    public void getFollowList() {

        firebaseFirestore.collection("follows").whereEqualTo("follower_id", user.getUser_id()).whereEqualTo("status", "active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        followList = queryDocumentSnapshots.toObjects(Follow.class);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < followList.size(); i++) {
                                    Log.d("qweqwe f", String.valueOf(followList.get(i).getFollowed_id()));
                                    firebaseFirestore.collection("posts").whereEqualTo("user_id", followList.get(i).getFollowed_id()).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if (queryDocumentSnapshots.isEmpty()) {
                                                        return;
                                                    } else {
                                                        types.addAll(queryDocumentSnapshots.toObjects(Post.class));
//                                        types = queryDocumentSnapshots.toObjects(Post.class);
//                                        for (int x = 0; x < types.size(); x++) {
//                                            String post_id = queryDocumentSnapshots.getDocuments().get(x).getId();
//                                            types.get(x).withId(post_id);
//                                        }
//                                        types.sort(new FFollow.CustomComparator().reversed());
//                                        if (types.size() < 10) {
////                                        for (int i = 0; i < types.size(); i++) {
////                                            mArrayList.add(types.get(i));
////                                        }
//                                            mArrayList.addAll(types);
//                                        } else {
//                                            for (int j = 0; j < 10; j++)
//                                                mArrayList.add(types.get(j));
//                                        }
//                                        recyclerView.setAdapter(mAdapter);
//                                        mAdapter.arrayList.addAll(mArrayList);
//                                        pgsBar.setVisibility(ProgressBar.GONE);
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("qweqweqwe", "아무내용이없습니다");
                                                    pgsBar.setVisibility(ProgressBar.GONE);
                                                }
                                            });
                                }

                            }
                        }, 500);

                    }
                });

        types.sort(new FFollow.CustomComparator().reversed());
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