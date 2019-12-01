//package com.jipjung.hucomin.sinderella.MyMenuActivities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.PopupMenu;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.kante.live_alone.Adapters.RecyclerAdapter;
//import com.example.kante.live_alone.Classes.Post;
//import com.example.kante.live_alone.Classes.User;
//import com.example.kante.live_alone.R;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class MyPosts extends AppCompatActivity {
//    private FirebaseFirestore firestore;
//    private FirebaseAuth firebaseAuth;
//
//    private List<Post> types;
//    private ArrayList<Post> posts = new ArrayList<>();
//    RecyclerView recyclerView;
//    RecyclerAdapter mAdapter;
//
//    ProgressBar pgsBar;
//
//    private TextView mpText01;
//    private User user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_my_liking_posts);
//
//        firestore = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//        mpText01 = findViewById(R.id.mlp_text01);
//
//        mpText01.setText("내가 쓴 게시글");
//
//        recyclerView = (RecyclerView) findViewById(R.id.liking_feeds);
//        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        mAdapter = new RecyclerAdapter(this, posts, R.layout.activity_my_liking_posts);
//
//        pgsBar = (ProgressBar) findViewById(R.id.progress_bar);
//
//        firestore.collection("users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                user = documentSnapshot.toObject(User.class);
//            }
//        });
//
//        //사용사자 쓴 게시물을 정렬
//        makeList();
//
//        //새로고침
//        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                makeList();
//                swipeContainer.setRefreshing(false);
//            }
//        });
//    }
//
//    public void makeList(){
//        pgsBar.setVisibility(ProgressBar.VISIBLE);
//        if(!posts.isEmpty())
//            posts.clear();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                firestore.collection("posts").whereEqualTo("user_id", firebaseAuth.getCurrentUser().getUid()).
//                        get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        types = queryDocumentSnapshots.toObjects(Post.class);
//                        types.sort(new CustomComparator().reversed());
//                        for(Post p : types)
//                            posts.add(p);
//                        recyclerView.setAdapter(mAdapter);
//                        if(posts.size() == 0){
//                            mpText01.setText("아직 쓴 게시글이 없네요ㅠ");
//                        }
//                        pgsBar.setVisibility(ProgressBar.GONE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("dasdasdas", "아무내용이없습니다");
//                        pgsBar.setVisibility(ProgressBar.GONE);
//                    }
//                });
//            }
//        }, 1500);
//    }
//
//    public class CustomComparator implements Comparator<Post> {
//        @Override
//        public int compare(Post o1, Post o2) {
//            return o1.getCreated_at().compareTo(o2.getCreated_at());
//        }
//    }
//
//    public void menuClick(View v){
//        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
//        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch(item.getItemId()){
//                    case R.id.go_mymenu:
//                        Intent intent = new Intent(MyPosts.this, MyMenu.class);
//                        intent.putExtra("nickname",user.getNickname());
//                        startActivity(intent);
//                        finish();
//                        break;
//                    case R.id.messages:
//                        Intent i = new Intent(MyPosts.this, MyMessages.class);
//                        startActivity(i);
//                        break;
//                }
//                return false;
//            }
//        });
//        popup.show();
//    }
//}
