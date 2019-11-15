package com.jipjung.hucomin.sinderella.HomeActivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jipjung.hucomin.sinderella.Fragments.FChat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jipjung.hucomin.sinderella.PostActivities.Posting;
import com.jipjung.hucomin.sinderella.R;
import com.jipjung.hucomin.sinderella.StartAppActivities.UserInfoInput;

import java.util.ArrayList;

public class HomeFeed extends AppCompatActivity {

    public static String TAG="HomeFeed";
    private DrawerLayout drawerLayout;
    private View drawerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private Fragment fr;
    private String nickname;
    private ArrayList<Button> buttons;
    boolean visible = false;
    public EditText searchingText;
    public static Context context;



    @Override
    protected void onStart() {
        super.onStart();
        final DocumentReference docRef = firebaseFirestore.collection("users").document(firebaseUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        User user = document.toObject(User.class);
                        nickname = document.getString("nickname");

                    } else {
                        Intent i = new Intent(HomeFeed.this,UserInfoInput.class);
                        startActivityForResult(i, 1);
//                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fr = new FChat();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();

        buttons = new ArrayList<Button>();
        buttons.add((Button)findViewById(R.id.chat));
        buttons.add((Button)findViewById(R.id.cook));
        buttons.add((Button)findViewById(R.id.room));
        buttons.add((Button)findViewById(R.id.activities));
        buttons.add((Button)findViewById(R.id.tips));
        buttons.add((Button)findViewById(R.id.eatout));
        buttons.add((Button)findViewById(R.id.trans));
//        buttons.get(0).setBackgroundResource(R.drawable.chat_2);

        Button bCook = (Button) findViewById(R.id.cook);
        Button bRoom = (Button) findViewById(R.id.room);
        Button bActivities = (Button) findViewById(R.id.activities);
        Button bTips = (Button) findViewById(R.id.tips);
        Button btn = (Button) findViewById(R.id.add_post);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPosting();
            }
        });
        Button btnSearch = (Button)findViewById(R.id.searchingText);
//        searchingText = (EditText)findViewById(R.id.searchingText);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(visible){
//                    searchingText.setVisibility(EditText.GONE);
//                    visible = false;
//                }
//                else{
//                    searchingText.setVisibility(EditText.VISIBLE);
//                    visible = true;
//                }
//            }
//        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        Button buttonOpenDrawer = (Button) findViewById(R.id.action_bar_menu);
        buttonOpenDrawer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button buttonCloseDrawer = (Button) findViewById(R.id.closedrawer);
        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                drawerLayout.closeDrawers();
            }
        });


        drawerLayout.setDrawerListener(myDrawerListener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });





        context = this;
    }
    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener() {

        public void onDrawerClosed(View drawerView) {
        }
        public void onDrawerOpened(View drawerView) {
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
//                txtPrompt.setText("onDrawerSlide: "
//                        + String.format("%.2f", slideOffset));
        }

        public void onDrawerStateChanged(int newState) {
            String state;
            switch (newState) {
                case DrawerLayout.STATE_IDLE:
                    state = "STATE_IDLE";
                    break;
                case DrawerLayout.STATE_DRAGGING:
                    state = "STATE_DRAGGING";
                    break;
                case DrawerLayout.STATE_SETTLING:
                    state = "STATE_SETTLING";
                    break;
                default:
                    state = "unknown!";
            }

//                txtPrompt2.setText(state);
        }
    };

    private void goPosting(){
        Intent intent = new Intent(this, Posting.class);
        intent.putExtra("Category",fr.getClass().getSimpleName());
        startActivity(intent);
    }
    public void selectCategory(View view){
        buttons.get(0).setBackgroundResource(R.drawable.converse);
//        buttons.get(1).setBackgroundResource(R.drawable.recipies);
//        buttons.get(2).setBackgroundResource(R.drawable.roominfo);
//        buttons.get(3).setBackgroundResource(R.drawable.schoolact);
//        buttons.get(4).setBackgroundResource(R.drawable.tips);
//        buttons.get(5).setBackgroundResource(R.drawable.eatout);
//        buttons.get(6).setBackgroundResource(R.drawable.trans);

        fr = null;
        switch(view.getId()){
            case R.id.chat:
                fr = new FChat();
                Bundle b = new Bundle();
                buttons.get(0).setBackgroundResource(R.drawable.converse);
                break;
//            case R.id.cook:
//                fr = new FCook();
//                buttons.get(1).setBackgroundResource(R.drawable.recipies_2);
//                break;
//            case R.id.room:
//                fr = new FRoom();
//                buttons.get(2).setBackgroundResource(R.drawable.room_2);
//                break;
//            case R.id.activities:
//                fr = new FActivities();
//                buttons.get(3).setBackgroundResource(R.drawable.activities_2);
//                break;
//            case R.id.tips:
//                fr = new FTips();
//                buttons.get(4).setBackgroundResource(R.drawable.tips_2);
//                break;
//            case R.id.eatout:
//                fr = new FEatout();
//                buttons.get(5).setBackgroundResource(R.drawable.eatout_2);
//                break;
//            case R.id.trans:
//                fr = new FTrans();
//                buttons.get(6).setBackgroundResource(R.drawable.trans_2);
//                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fr);
        ft.commit();
    }
}
