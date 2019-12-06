package com.jipjung.hucomin.sinderella.MyMenuActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OtherMyMenu extends AppCompatActivity {

    private User user;
    private User post_user;
    private Follow follow;

    private Switch other_people_follow_switch;
    private TextView other_people_follow_username;
    //private TextView other_people_follow_email;
    private TextView mypage_foot_size;
    private TextView mypage_foot_width;
    private Button action_bar_back_close;

    private Button other_people_post_board;
    private Button other_people_follower;
    private Button other_people_following;
    private TextView num_post;
    private TextView num_follower;
    private TextView num_following;

    private FirebaseFirestore firebaseFirestore;
    private TextView other_people_follow_text;
    private TextView other_people_unfollow_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_people_follow);

        user = (User)getIntent().getSerializableExtra("user");
        post_user= (User)getIntent().getSerializableExtra("post_user");
        follow = (Follow)getIntent().getSerializableExtra("follow");
        firebaseFirestore = FirebaseFirestore.getInstance();

        other_people_follow_switch = findViewById(R.id.other_people_follow_switch);
        other_people_follow_username = findViewById(R.id.other_people_follow_username);
        //other_people_follow_email = findViewById(R.id.other_people_follow_email);
        mypage_foot_size = findViewById(R.id.mypage_foot_size);
        mypage_foot_width = findViewById(R.id.mypage_foot_width);

        other_people_post_board = findViewById(R.id.other_people_post_board);
        other_people_follower = findViewById(R.id.other_people_follower);
        other_people_following = findViewById(R.id.other_people_following);
        num_post = findViewById(R.id.num_post);
        num_follower = findViewById(R.id.num_follower);
        num_following = findViewById(R.id.num_following);

        other_people_follow_text = findViewById(R.id.other_people_follow_text);
        other_people_unfollow_text = findViewById(R.id.other_people_unfollow_text);

        other_people_post_board.setSelected(true);
        other_people_post_board.setTextColor(Color.WHITE);
        num_post.setTextColor(Color.WHITE);

        //post, follower, following 전화
        Log.d("click",String.valueOf(other_people_post_board.isSelected()));
        Log.d("click",String.valueOf(other_people_following.isSelected()));
        Log.d("click",String.valueOf(other_people_follower.isSelected()));

        other_people_post_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","post_true");
                other_people_following.setSelected(false);
                other_people_following.setTextColor(Color.BLACK);
                num_following.setTextColor(Color.BLACK);
                other_people_follower.setSelected(false);
                other_people_follower.setTextColor(Color.BLACK);
                num_follower.setTextColor(Color.BLACK);
                other_people_post_board.setSelected(true);
                other_people_post_board.setTextColor(Color.WHITE);
                num_post.setTextColor(Color.WHITE);
            }
        });

        other_people_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","following_true");
                other_people_post_board.setSelected(false);
                other_people_post_board.setTextColor(Color.BLACK);
                num_post.setTextColor(Color.BLACK);
                other_people_follower.setSelected(false);
                other_people_follower.setTextColor(Color.BLACK);
                num_follower.setTextColor(Color.BLACK);
                other_people_following.setSelected(true);
                other_people_following.setTextColor(Color.WHITE);
                num_following.setTextColor(Color.WHITE);
            }
        });

        other_people_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","follower_true");
                other_people_following.setSelected(false);
                other_people_following.setTextColor(Color.BLACK);
                num_following.setTextColor(Color.BLACK);
                other_people_post_board.setSelected(false);
                other_people_post_board.setTextColor(Color.BLACK);
                num_post.setTextColor(Color.BLACK);
                other_people_follower.setSelected(true);
                other_people_follower.setTextColor(Color.WHITE);
                num_follower.setTextColor(Color.WHITE);
            }
        });




        if(follow!=null){
            if(follow.getStatus().equals("active")){
                other_people_follow_switch.setChecked(true);
            }
        }

        other_people_follow_username.setText(post_user.getNickname());
        mypage_foot_size.setText(String.valueOf(post_user.getFoot_size()));

        if(post_user.getFoot_width().equals("small")){
            mypage_foot_width.setText("좁은편");
        }else if(post_user.getFoot_width().equals("normal")){
            mypage_foot_width.setText("보통");
        }else{
            mypage_foot_width.setText("큰편");
        }

        action_bar_back_close = findViewById(R.id.action_bar_back_close);

        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        //follow, unfollow 버튼 보이기
        if(follow == null){
            other_people_follow_switch.setChecked(false);
            other_people_follow_text.setVisibility(View.INVISIBLE);
            other_people_unfollow_text.setVisibility(View.VISIBLE);

        }else if(follow.getStatus().equals("active")){
            other_people_follow_switch.setChecked(true);
            other_people_follow_text.setVisibility(View.VISIBLE);
            other_people_unfollow_text.setVisibility(View.INVISIBLE);
        }else{
            other_people_follow_switch.setChecked(false);
            other_people_follow_text.setVisibility(View.INVISIBLE);
            other_people_unfollow_text.setVisibility(View.VISIBLE);
        }
        other_people_follow_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    other_people_follow_text.setVisibility(View.VISIBLE);
                    other_people_unfollow_text.setVisibility(View.INVISIBLE);
                }
                else{
                    other_people_follow_text.setVisibility(View.INVISIBLE);
                    other_people_unfollow_text.setVisibility(View.VISIBLE);
                }
            }
        });

        other_people_follow_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("follows").whereEqualTo("follower_id", user.getUser_id())
                        .whereEqualTo("followed_id", post_user.getUser_id()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            /* 팔로우 객체가 없을때 */
                            WriteBatch batch = firebaseFirestore.batch();
                            DocumentReference follow = firebaseFirestore.collection("follows").document();
                            Map<String, Object> docData = new HashMap<>();

                            docData.put("follower_id", user.getUser_id());
                            docData.put("followed_id", post_user.getUser_id());
                            docData.put("id", follow.getId());

                            // 댓글 날짜 DB
                            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                            String format = s.format(new Date());

                            docData.put("created_at", format);
                            docData.put("status", "active");
//                            buttonLike.setImageResource(R.drawable.like_clicked);

                            batch.set(follow, docData);
                            batch.commit();

                            Toast.makeText(getApplicationContext(), "팔로우", Toast.LENGTH_LONG).show();
                        } else {
                            /* 팔로우 객체가 있을 때*/
                            Follow l = queryDocumentSnapshots.toObjects(Follow.class).get(0);
                            Log.d("qwea", "cccccC");
                            if (l.getStatus().equals("active")) {
                                firebaseFirestore.collection("follows").document(l.id).update("status", "deactivated");
//                                buttonLike.setImageResource(R.drawable.like);
                                Toast.makeText(getApplicationContext(), "팔로우 취소", Toast.LENGTH_LONG).show();
                            } else {
                                firebaseFirestore.collection("follows").document(l.id).update("status", "active");
//                                buttonLike.setImageResource(R.drawable.like_clicked);
                                Toast.makeText(getApplicationContext(), "팔로우", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        WriteBatch batch = firebaseFirestore.batch();
                        DocumentReference follow = firebaseFirestore.collection("follows").document();
                        Map<String, Object> docData = new HashMap<>();

                        docData.put("follower_id", user.getUser_id());
                        docData.put("followed_id", post_user.getUser_id());
                        docData.put("id", follow.getId());

                        // 댓글 날짜 DB
                        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                        String format = s.format(new Date());

                        docData.put("created_at", format);
                        docData.put("status", "active");
//                buttonLike.setImageResource(R.drawable.like_clicked);

                        batch.set(follow, docData);
                        batch.commit();
                    }
                });
            }
        });

    }


}
