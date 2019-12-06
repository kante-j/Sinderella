package com.jipjung.hucomin.sinderella.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jipjung.hucomin.sinderella.R;

public class FFollowerAndFollowing extends Fragment {


    private FirebaseFirestore firebaseFirestore;

    public FFollowerAndFollowing(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.follow_fragment, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);


        return v;
    }

}
