package com.jipjung.hucomin.sinderella.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FFollow extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;

    private ArrayList<Post> mArrayList;

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
        FirebaseFirestore.setLoggingEnabled(true);


        pgsBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mArrayList = new ArrayList<>();


        return v;
    }
}