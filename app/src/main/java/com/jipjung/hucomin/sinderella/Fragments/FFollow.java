package com.jipjung.hucomin.sinderella.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jipjung.hucomin.sinderella.R;
import com.google.firebase.firestore.FirebaseFirestore;
public class FFollow extends Fragment {
    public FFollow() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.follow_fragment, container, false);
        FirebaseFirestore.setLoggingEnabled(true);
        return v;
    }
}