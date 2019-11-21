package com.jipjung.hucomin.sinderella.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.MyMenuActivities.MyMenu;
import com.jipjung.hucomin.sinderella.R;

public class FMymenu extends Fragment {

    private ImageView go_profilecorrect;

    private User user;
    private FirebaseUser fbUser;
    private FirebaseFirestore fs;
    private TextView text_email;
    private TextView text_nickname;
    private TextView text_foot_size;
    private TextView text_foot_width;

    public FMymenu(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mypage, container, false);
        FirebaseFirestore.setLoggingEnabled(true);

        fs = FirebaseFirestore.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        text_email = v.findViewById(R.id.mypage_email);
        text_nickname = v.findViewById(R.id.mypage_username);
        text_foot_size = v.findViewById(R.id.mypage_foot_size);
        text_foot_width = v.findViewById(R.id.mypage_foot_width);


        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
        text_nickname.setText(user.getNickname());
        text_email.setText(fbUser.getEmail());
        text_foot_width.setText(user.getFoot_width());
        text_foot_size.setText(String.valueOf(user.getFoot_size()));


        go_profilecorrect = v.findViewById(R.id.mypage_btn_profile);
        go_profilecorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyMenu.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

//        getUser();

        return v;
    }

    private void getUser(){
        fs.collection("users").whereEqualTo("user_id",fbUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            return;
                        }else{
                            user = queryDocumentSnapshots.toObjects(User.class).get(0);
                            text_nickname.setText(user.getNickname());
                            text_email.setText(fbUser.getEmail());
                            text_foot_width.setText(user.getFoot_width());
                            text_foot_size.setText(String.valueOf(user.getFoot_size()));

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}