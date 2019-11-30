package com.jipjung.hucomin.sinderella.MyMenuActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

public class OtherMyMenu extends AppCompatActivity {

    private User user;
    private User post_user;
    private Follow follow;

    private Switch other_peoploe_follow_switch;
    private TextView other_people_follow_username;
    //private TextView other_people_follow_email;
    private TextView mypage_foot_size;
    private TextView mypage_foot_width;

    private TextView other_people_follow_text;
    private TextView other_people_unfollow_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_people_follow);

        user = (User)getIntent().getSerializableExtra("user");
        post_user= (User)getIntent().getSerializableExtra("post_user");
        follow = (Follow)getIntent().getSerializableExtra("follow");

        other_peoploe_follow_switch = findViewById(R.id.other_peoploe_follow_switch);
        other_people_follow_username = findViewById(R.id.other_people_follow_username);
        //other_people_follow_email = findViewById(R.id.other_people_follow_email);
        mypage_foot_size = findViewById(R.id.mypage_foot_size);
        mypage_foot_width = findViewById(R.id.mypage_foot_width);

        other_people_follow_text = findViewById(R.id.other_people_follow_text);
        other_people_unfollow_text = findViewById(R.id.other_people_unfollow_text);

        if(follow!=null){
            if(follow.getStatus().equals("active")){
                other_peoploe_follow_switch.setChecked(true);
            }
        }
        other_people_follow_username.setText(post_user.getNickname());
        mypage_foot_size.setText(String.valueOf(post_user.getFoot_size()));

        if(post_user.getFoot_width().equals("small")){
            mypage_foot_width.setText("작은편");
        }else if(post_user.getFoot_width().equals("normal")){
            mypage_foot_width.setText("보통");
        }else{
            mypage_foot_width.setText("큰편");
        }

        other_peoploe_follow_switch.setChecked(false);
        other_peoploe_follow_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
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

    }
}
