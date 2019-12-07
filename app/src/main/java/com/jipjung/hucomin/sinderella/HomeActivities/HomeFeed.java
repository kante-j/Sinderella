package com.jipjung.hucomin.sinderella.HomeActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.Fragments.FCart;
import com.jipjung.hucomin.sinderella.Fragments.FFollow;
import com.jipjung.hucomin.sinderella.Fragments.FHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jipjung.hucomin.sinderella.Fragments.FMymenu;
import com.jipjung.hucomin.sinderella.Fragments.FSearchResult;
import com.jipjung.hucomin.sinderella.MyMenuActivities.MyMenu;
import com.jipjung.hucomin.sinderella.PostActivities.Posting;
import com.jipjung.hucomin.sinderella.R;
import com.jipjung.hucomin.sinderella.Search.SearchActivity;
import com.jipjung.hucomin.sinderella.StartAppActivities.UserInfoInput;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class HomeFeed extends AppCompatActivity {

    public static String TAG="HomeFeed";
    private DrawerLayout drawerLayout;
    private View drawerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Fragment fr;
    private String nickname;
    private ArrayList<Button> buttons;
    boolean visible = false;
    public EditText searchingText;
    public static Context context;
    private String search_context;

    private Bundle userbundle;
    private Bundle followbundle;

    private User user;
    // under bar Button
    private ImageView follow_btn;
    private ImageView cart_btn;
    private ImageView home_btn;
    private ImageView mypage_btn;
    private Button btn_searchingText;
    private TextView sneakers_btn;
    private TextView converse_shoe;
    private TextView slip_on_shoe;
    private TextView running_shoe;
    private TextView aqua_shoe;
    private TextView golf_shoe;
    private TextView hiking_shoe;
    private TextView slid_shoe;
    private TextView slipper;
    private TextView strap_sandal;

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        follow_btn = findViewById(R.id.go_follow);
        cart_btn = findViewById(R.id.go_shop);
        mypage_btn = findViewById(R.id.go_mymenu);
        home_btn = findViewById(R.id.go_home);


        userbundle = new Bundle();
        followbundle = new Bundle();

        firebaseFirestore.collection("users").whereEqualTo("user_id",firebaseUser.getUid()).get()
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
                            userbundle.putSerializable("user",user);
                            followbundle.putSerializable("user",user);
                            fr = new FHome();
                            fr.setArguments(userbundle);
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

//        follow_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fr = new FFollow();
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();
//            }
//        });
//
//        home_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fr = new FHome();
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fr).commit();
//            }
//        });



//        buttons = new ArrayList<Button>();
//        buttons.add((Button)findViewById(R.id.chat));
//        buttons.add((Button)findViewById(R.id.cook));
//        buttons.add((Button)findViewById(R.id.room));
//        buttons.add((Button)findViewById(R.id.activities));
//        buttons.add((Button)findViewById(R.id.tips));
//        buttons.add((Button)findViewById(R.id.eatout));
//        buttons.add((Button)findViewById(R.id.trans));
//        buttons.get(0).setBackgroundResource(R.drawable.chat_2);

//        Button bCook = (Button) findViewById(R.id.cook);
//        Button bRoom = (Button) findViewById(R.id.room);
//        Button bActivities = (Button) findViewById(R.id.activities);
//        Button bTips = (Button) findViewById(R.id.tips);

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

        // Drawer Layout 화면
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

        findViewById(R.id.go_vv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFeed.this, VV.class);
                startActivity(intent);
            }
        });

        //TODO: 카테고리에서 누르시 Intent 값을 통해서 값 전달
        converse_shoe = findViewById(R.id.converse_shoe);
        slip_on_shoe = findViewById(R.id.slip_on_shoe);
        sneakers_btn = findViewById(R.id.sneakers);
        aqua_shoe = findViewById(R.id.aqua_shoe);
        golf_shoe = findViewById(R.id.golf_shoe);
        running_shoe = findViewById(R.id.running_shoe);
        hiking_shoe = findViewById(R.id.hiking_shoe);
        slid_shoe = findViewById(R.id.slid_shoe);
        slipper = findViewById(R.id.slipper);
        strap_sandal = findViewById(R.id.strap_sandal);

        Intent intent = new Intent(getApplicationContext(),FSearchResult.class);

        Fragment fragment = new FSearchResult();
        Bundle bundle = new Bundle();
//        FragmentManager fm_search = getSupportFragmentManager();
//        FragmentTransaction ft_search = fm_search.beginTransaction();

//        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sf.edit();


        converse_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", converse_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", converse_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();

            }
        });

        slip_on_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", slip_on_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", slip_on_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }
        });

        sneakers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", sneakers_btn.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", sneakers_btn.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        aqua_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", aqua_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", aqua_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        golf_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", golf_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", golf_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        running_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", running_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", running_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        hiking_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", hiking_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", hiking_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        slid_shoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", slid_shoe.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", slid_shoe.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        slipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", slipper.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", slipper.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });

        strap_sandal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FSearchResult();
                Bundle bundle = new Bundle();

                FragmentManager fm_search = getSupportFragmentManager();
                FragmentTransaction ft_search = fm_search.beginTransaction();

                bundle.putString("shoe", strap_sandal.getText().toString());
                bundle.putSerializable("user",user);

                Log.d("shoe", strap_sandal.getText().toString());

                fragment.setArguments(bundle);


                ft_search.replace(R.id.fragment_container, fragment);
                ft_search.commit();

                drawerLayout.closeDrawers();
            }

        });









        Button btn_gomymenu = findViewById(R.id.go_mymenu_btn);
        btn_gomymenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fr = new FMymenu();
                fr.setArguments(userbundle);
//                mypage_btn.setBackgroundResource(R.drawable.mypage_clicked);

                FragmentManager fm = getSupportFragmentManager();

                FragmentTransaction ft = fm.beginTransaction();

                ft.replace(R.id.fragment_container, fr);

                ft.commit();

                follow_btn.setBackgroundResource(R.drawable.follow);
                home_btn.setBackgroundResource(R.drawable.home);
                cart_btn.setBackgroundResource(R.drawable.cart_white);
                mypage_btn.setBackgroundResource(R.drawable.mypage_clicked);

                drawerLayout.closeDrawers();

//                Intent intent = new Intent(HomeFeed.this, MyMenu.class);
//                startActivity(intent);

            }
        });

        btn_searchingText = findViewById(R.id.searchingText);

//        btn_searchingText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeFeed.this, SearchActivity.class);
//                startActivityForResult(intent, 2);
//
//            }
//        });
        RelativeLayout search_Layout = findViewById(R.id.search_Layout);
        btn_searchingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                follow_btn.setBackgroundResource(R.drawable.follow);
                home_btn.setBackgroundResource(R.drawable.home);
                cart_btn.setBackgroundResource(R.drawable.cart_white);
                mypage_btn.setBackgroundResource(R.drawable.icon_perm_identity_rounded);
                btn_searchingText.setBackgroundResource(R.drawable.search_gray);
                fr = new FSearchResult();
                fr.setArguments(userbundle);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();
//                Intent intent = new Intent(HomeFeed.this, SearchActivity.class);
//                startActivityForResult(intent, 2);

            }
        });


        drawerLayout.setDrawerListener(myDrawerListener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return true;
            }
        });



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }




        context = this;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if(requestCode==2)
        {
            if(data !=null){
                search_context=data.getStringExtra("search_context");
                fr = new FSearchResult();
                userbundle.putString("search_keyword", search_context);
                fr.setArguments(userbundle);

                follow_btn.setBackgroundResource(R.drawable.follow);
                home_btn.setBackgroundResource(R.drawable.home);
                cart_btn.setBackgroundResource(R.drawable.cart_white);
                mypage_btn.setBackgroundResource(R.drawable.icon_perm_identity_rounded);

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_container, fr);
                ft.commit();
            }


        }
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
        follow_btn.setBackgroundResource(R.drawable.follow);
        home_btn.setBackgroundResource(R.drawable.home);
        cart_btn.setBackgroundResource(R.drawable.cart_white);
        mypage_btn.setBackgroundResource(R.drawable.icon_perm_identity_rounded);
        btn_searchingText.setBackgroundResource(R.drawable.search);
        fr = null;

        switch(view.getId()){
            case R.id.go_home:
                fr = new FHome();
                fr.setArguments(userbundle);
                home_btn.setBackgroundResource(R.drawable.icon_home);
                break;
            case R.id.go_follow:
                fr = new FFollow();
                fr.setArguments(userbundle);
                follow_btn.setBackgroundResource(R.drawable.follow_clicked);
                break;
            case R.id.go_shop:
                fr = new FCart();
                fr.setArguments(userbundle);
                cart_btn.setBackgroundResource(R.drawable.cart_clicked);
                break;
            case R.id.go_mymenu:
                fr = new FMymenu();
                fr.setArguments(userbundle);
                mypage_btn.setBackgroundResource(R.drawable.mypage_clicked);
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fr);
        ft.commit();
    }


    @Override
    protected void onResume() {

        if(fr!=null){
            if(fr.getClass()==FHome.class){
                fr = new FHome();
            }else if(fr.getClass()==FFollow.class){
                fr= new FFollow();
            }else if(fr.getClass()==FCart.class){
                fr= new FCart();
            }else if(fr.getClass()==FMymenu.class){
                fr= new FMymenu();
            }

            fr.setArguments(userbundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, fr);
            ft.commit();
        }

        super.onResume();

    }

}
