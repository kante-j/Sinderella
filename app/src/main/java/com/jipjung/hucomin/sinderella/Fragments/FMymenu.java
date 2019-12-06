package com.jipjung.hucomin.sinderella.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.MyMenuActivities.MyMenu;
import com.jipjung.hucomin.sinderella.PostActivities.Posting;
import com.jipjung.hucomin.sinderella.R;
import com.jipjung.hucomin.sinderella.StartAppActivities.FirstPage;
import com.jipjung.hucomin.sinderella.StartAppActivities.SplashScreen;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;



public class FMymenu extends Fragment {

    private ImageView go_profilecorrect;
    private StorageReference storageReference;

    private User user;
    private FirebaseUser fbUser;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore fs;
    private TextView text_email;
    private TextView text_nickname;
    private TextView text_foot_size;
    private TextView text_foot_width;
    private Button logoutbtn;
    private Button followerbtn;
    private Button followingbtn;
    private TextView follower_num;
    private TextView following_num;
    private FrameLayout follow_framelayout;
    private ImageView mypage_profile_picture;
    private FirebaseStorage storage;

    private Fragment fr;


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
        fbAuth = FirebaseAuth.getInstance();

        text_email = v.findViewById(R.id.mypage_email);
        text_nickname = v.findViewById(R.id.mypage_username);
        text_foot_size = v.findViewById(R.id.mypage_foot_size);
        text_foot_width = v.findViewById(R.id.mypage_foot_width);
        mypage_profile_picture = v.findViewById(R.id.mypage_profile_picture);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");

        Bundle bundle = getArguments();
        user = (User)bundle.getSerializable("user");
        text_nickname.setText(user.getNickname());
        text_email.setText(fbUser.getEmail());
        text_foot_width.setText(user.getFoot_width());
        text_foot_size.setText(String.valueOf(user.getFoot_size()));

        Bundle userbundle = new Bundle();
        userbundle.putSerializable("user",user);
        fr = new FMyPosts();
        fr.setArguments(userbundle);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.mypage_commet_fragment_container,fr).commit();


        go_profilecorrect = v.findViewById(R.id.mypage_btn_profile);
        go_profilecorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyMenu.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });

        if(user.getProfile_url() !=null){

            StorageReference path = storageReference.child(user.getProfile_url());
            Glide.with(this).load(path).skipMemoryCache(false).into(mypage_profile_picture);
        }

        logoutbtn = v.findViewById(R.id.mypage_logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        fbUser = FirebaseAuth.getInstance();
                        fbAuth.signOut();
                        Intent intent = new Intent(getActivity(), SplashScreen.class);
                        getActivity().finishAffinity();
//                        finishAffinity();
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });

        //TODO: btn 색깔변화

        followerbtn = v.findViewById(R.id.mypage_follower);
        followingbtn=v.findViewById(R.id.mypage_following);
        follow_framelayout = v.findViewById(R.id.mypage_follower_and_following);
        follower_num = v.findViewById(R.id.follower_num);
        following_num = v.findViewById(R.id.following_num);

        Log.d("click",String.valueOf(followerbtn.isSelected()));
        Log.d("click",String.valueOf(followingbtn.isSelected()));

        followerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("click","follower_true");
                    followingbtn.setSelected(false);
                    followingbtn.setTextColor(Color.BLACK);
                    following_num.setTextColor(Color.BLACK);
                    followerbtn.setSelected(true);
                    followerbtn.setTextColor(Color.WHITE);
                    follower_num.setTextColor(Color.WHITE);
                    follow_framelayout.setVisibility(getView().VISIBLE);

            }
        });


        followingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("click","following_true");
                    followerbtn.setSelected(false);
                    followerbtn.setTextColor(Color.BLACK);
                    follower_num.setTextColor(Color.BLACK);
                    followingbtn.setSelected(true);
                    followingbtn.setTextColor(Color.WHITE);
                    following_num.setTextColor(Color.WHITE);
                    follow_framelayout.setVisibility(getView().VISIBLE);

            }
        });




//        getUser();

        return v;
    }

//유저id에 따라 유저 정보 받기
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