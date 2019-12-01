package com.jipjung.hucomin.sinderella.MyMenuActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jipjung.hucomin.sinderella.R;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.StartAppActivities.SplashScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;

public class MyMenu extends AppCompatActivity {

    private FirebaseUser fbUser;
    private FirebaseFirestore fs;
    private TextView text_created_at;
    private EditText text_email;
    private EditText text_nickname;
    private TextView birth_date;
    private Spinner foot_size;
    private RadioGroup foot_width;

    private User user;
    private FirebaseAuth fbAuth;
    private Button btn_password;
    private RelativeLayout password_modify_layout;
    private Button profilemodify_check;
    private Button action_bar_back_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilemodify);

//        //get User Info
//        FirebaseFirestore.setLoggingEnabled(true);
        fs = FirebaseFirestore.getInstance();
////        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
////                .setTimestampsInSnapshotsEnabled(true)
////                .build();
////        fs.setFirestoreSettings(settings);
//
//
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        user = (User)getIntent().getSerializableExtra("user");

//        //Find Text View by Id
        text_email = findViewById(R.id.profilemodify_email);
//        text_created_at = findViewById(R.id.mymenu_created_at);
        text_nickname = findViewById(R.id.profilemodify_nicname);
        birth_date = findViewById(R.id.year_month_day);


        // Spinner set

        foot_size = findViewById(R.id.profilemodify_foot_size);
        String compareValue = String.valueOf(user.getFoot_size());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.foot_size, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foot_size.setAdapter(adapter);
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            foot_size.setSelection(spinnerPosition);
        }


        text_nickname.setText(user.getNickname());
        text_email.setText(fbUser.getEmail());
        birth_date.setText(user.getBirth_date());

        //MoHyeonMin


        btn_password = findViewById(R.id.btn_passward_profilemodify);
        password_modify_layout = findViewById(R.id.password_modify_layout);
        profilemodify_check = findViewById(R.id.profilemodify_check);
        action_bar_back_close = findViewById(R.id.action_bar_back_close);

        //click시 비밀번호 창 생성했다가 사라지기
        btn_password.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(btn_password.getVisibility() == View.VISIBLE){
                    btn_password.setVisibility((View.GONE));
                    password_modify_layout.setVisibility(View.VISIBLE);
                }
            }


        });

        profilemodify_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickHandler();

                if(profilemodify_check.isPressed()){
                    password_modify_layout.setVisibility(View.GONE);
                    btn_password.setVisibility(View.VISIBLE);
                }
            }
        });

        //뒤로 가기 버튼
        action_bar_back_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        filterbtn =v.findViewById(R.id.btn_filter);
//        filterbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(applytext.getVisibility() == View.GONE){
//                    applytext.setVisibility(View.VISIBLE);
//                }else{
//                    applytext.setVisibility(View.GONE);
//                }
//
//                if(filterscreen.getVisibility() == View.GONE){
//                    filterscreen.setVisibility(View.VISIBLE);
//                }else{
//                    filterscreen.setVisibility(View.GONE);
//                }
//
//            }
//        });



//        text_foot_width.setText(user.getFoot_width());
//        text_foot_size.setText(user.getFoot_size());

//        String timeStamp = new SimpleDateFormat("yyyy년 MM월 dd일").format(fbUser.getMetadata().getCreationTimestamp());
//        text_email.setText(fbUser.getEmail().toString());
//        text_created_at.setText(timeStamp);
//
//
//        text_nickname.setText(getIntent().getStringExtra("nickname"));
//
//        //로그아웃
//        Button btn_logout = findViewById(R.id.button_logout);
//        btn_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!MyMenu.this.isFinishing()){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MyMenu.this);
//                    builder.setTitle("로그아웃");
//                    builder.setMessage("로그아웃 하시겠습니까?");
//                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            fbAuth = FirebaseAuth.getInstance();
//                            fbAuth.signOut();
//                            Intent intent = new Intent(MyMenu.this, SplashScreen.class);
//                            finishAffinity();
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                        }
//                    });
//                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            return;
//                        }
//                    });
//                    builder.show();
//                }
//            }
//        });
//
//        //내가 쓴 게시글 보기
//        Button goMyPosts = findViewById(R.id.my_posts);
//        goMyPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyMenu.this, MyPosts.class);
//                startActivity(intent);
//            }
//        });
//
//        //내가 좋아하는 게시글 보기
//        Button goLikingPosts = findViewById(R.id.go_liking_posts);
//        goLikingPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MyMenu.this, LikingPosts.class);
//                intent.putExtra("nickname",text_nickname.getText().toString());
//                startActivity(intent);
//            }
//        });
    }

    public void OnClickHandler(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("비밀번호 수정").setMessage("수정되었습니다.");

        AlertDialog alertDialog =builder.create();

        alertDialog.show();
    }

    public void restartApp(Context context) {
    }

//    private void getUser(){
//        fs.collection("users").whereEqualTo("user_id",fbUser.getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(queryDocumentSnapshots.isEmpty()){
//                            return;
//                        }else{
//                            user = queryDocumentSnapshots.toObjects(User.class).get(0);
//
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//
//    }
//    public void menuClick(View v){
//        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
//        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch(item.getItemId()){
////                    case R.id.go_mymenu:
////                        Intent intent = new Intent(MyMenu.this, MyMenu.class);
////                        startActivity(intent);
////                        finish();
////                        break;
//                    case R.id.messages:
//                        Intent i = new Intent(MyMenu.this, MyMessages.class);
//                        startActivity(i);
//                        break;
//                }
//                return false;
//            }
//        });
//        popup.show();
//    }
}