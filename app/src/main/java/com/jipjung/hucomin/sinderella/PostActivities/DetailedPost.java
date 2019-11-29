package com.jipjung.hucomin.sinderella.PostActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.example.kante.live_alone.MessageActivities.SendMessage;
//import com.example.kante.live_alone.MyMenuActivities.MyMenu;
//import com.example.kante.live_alone.MyMenuActivities.MyMessages;

import com.jipjung.hucomin.sinderella.Adapters.CommentAdapter;
import com.jipjung.hucomin.sinderella.Classes.Comment;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.Like;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedPost extends AppCompatActivity {
    // Pinch Zoom
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;


    private FirebaseStorage fs;
    private ImageView dImage;
    private TextView dTitle;
    private TextView dBody;
    private TextView dUid;
    private TextView dTime;
    private TextView post_commet_count;
    private TextView post_like_count;
    private TextView note;
    private String dUrl;
    private StorageReference sr;
    private Button deleteButton;
    private String currentUserId;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private User user;
    private String post_id;
    private Button writeCommentButton;
    private Button findLocationButton;
    private EditText contextComment;
    private ImageView buttonLike;
    private ImageButton sendMessagebtn;

    private DrawerLayout drawerLayout;
    private View drawerView;
    private ListView commentListView;
    private CommentAdapter adapter;
    private List<Comment> comments;
    private Post post;
    private Follow follow;
private TextView price_size;
    private Switch followSwitch;

//    private Post p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        //유저정보와 글쓴이가 일치하다면 포스트 삭제버튼 활성화
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String user_id = firebaseAuth.getUid(); // 유저버튼 받아옴
        user = (User)getIntent().getSerializableExtra("user");
        post = (Post)getIntent().getSerializableExtra("post");
        follow = (Follow) getIntent().getSerializableExtra("follow");
//        firebaseFirestore.collection("users").document(user_id).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            user = documentSnapshot.toObject(User.class);
//                            if(user.getNickname().equals(dUid.getText().toString())){
//                                deleteButton.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    }
//                });
        price_size = findViewById(R.id.price_size);

        post_like_count = findViewById(R.id.post_like_count);
        post_commet_count = findViewById(R.id.post_commet_count);
        followSwitch = findViewById(R.id.follow_switch);
        dImage = findViewById(R.id.dp_image);
        dTitle = findViewById(R.id.dp_title);
        dBody = findViewById(R.id.dp_body);
        dUid = findViewById(R.id.dp_user_id);
        dTime = findViewById(R.id.dp_posted_time);
        deleteButton = findViewById(R.id.postDelete);
        writeCommentButton = findViewById(R.id.btn_comment_input);
        contextComment = findViewById(R.id.input_comment_context);
        buttonLike = findViewById(R.id.btn_like);
//        findLocationButton = findViewById(R.id.find_location);
//        note = findViewById(R.id.note);
//        sendMessagebtn = findViewById(R.id.btn_send_message);
        Intent intent = getIntent();
        dTitle.setText(intent.getStringExtra("TITLE"));
        dBody.setText(intent.getStringExtra("BODY"));
        dUid.setText(intent.getStringExtra("UID"));
        dTime.setText(intent.getStringExtra("TIME"));
        price_size.setText("구매가격 : "+post.getPrice() + " /구매 사이즈 : "+String.valueOf(post.getShoe_size_num()));
        //음식점 추천 카테고리 글에만 위치 검색기능 버튼 활성화
        if(intent.getStringExtra("CATEGORY").equals("FEatout")){
            findLocationButton.setVisibility(View.VISIBLE);
            findLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchIntent = new Intent();
                    searchIntent.setAction(Intent.ACTION_VIEW);
                    searchIntent.setData(Uri.parse("geo:0,0?q="+dTitle.getText()));
                    startActivity(searchIntent);
                }
            });
        }

        //중고품 거래 기능 글에만 note 텍스트뷰 보이게
        if(intent.getStringExtra("CATEGORY").equals("FTrans")){
            note.setVisibility(View.VISIBLE);
        }

        dUrl = getIntent().getStringExtra("URL");
        if(dUrl != null){
            dImage.setVisibility(View.VISIBLE);
        }
        post_id = getIntent().getStringExtra("POSTID");
//        firebaseFirestore.collection("posts").document(post_id).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if(documentSnapshot.exists()){
//                            post = documentSnapshot.toObject(Post.class);
//
//                        }
//                    }
//                });



        fs = FirebaseStorage.getInstance();
        sr = fs.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");
        if(dUrl != null){
            StorageReference path = sr.child(dUrl);
            Glide.with(this).load(path).skipMemoryCache(true).into(dImage);
        }

        commentListView = (ListView)findViewById(R.id.list_comments);
        //댓글 보이기
        getComments();
        getLikeCount();
        //처음에 시작할 때 자기가 좋아요한 글일 경우 좋아요 이미지가 활성화 되어있게 변경
        isLikePost();
        isFollowed();

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComments();
                swipeContainer.setRefreshing(false);
            }
        });


        /* Drawer Menu*/
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

//        Button buttonOpenDrawer = (Button) findViewById(R.id.action_bar_menu);
//        buttonOpenDrawer.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                drawerLayout.openDrawer(drawerView);
//            }
//        });

//        Button buttonCloseDrawer = (Button) findViewById(R.id.closedrawer);
//        buttonCloseDrawer.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                drawerLayout.closeDrawers();
//            }
//        });


//        drawerLayout.setDrawerListener(myDrawerListener);
//        drawerView.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                return true;
//            }
//        });

        followSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("follows").whereEqualTo("follower_id", user.getUser_id())
                        .whereEqualTo("followed_id", post.getUser_id()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            /* 팔로우 객체가 없을때 */
                            WriteBatch batch = firebaseFirestore.batch();
                            DocumentReference follow = firebaseFirestore.collection("follows").document();
                            Map<String, Object> docData = new HashMap<>();

                            docData.put("follower_id", user.getUser_id());
                            docData.put("followed_id", post.getUser_id());
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
                            docData.put("followed_id", post.getUser_id());
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


        /******* 라디오 버튼 선택 *********/
        String str_shoesWeight = post.getShoes_weight();
        if(str_shoesWeight.equals("fit")){
            RadioButton r = findViewById(R.id.shoes_weight_fit);
            r.setChecked(true);
            r.setEnabled(true);
        }else if(str_shoesWeight.equals("heavy")){
            RadioButton r = findViewById(R.id.shoes_weight_heavy);
            r.setChecked(true);
            r.setEnabled(true);
        }else{
            RadioButton r = findViewById(R.id.shoes_weight_light);
            r.setChecked(true);
            r.setEnabled(true);
        }

        String str_shoes_size = post.getShoes_size();
        if(str_shoes_size.equals("big")){
            RadioButton r = findViewById(R.id.shoes_size_big);
            r.setChecked(true);
            r.setEnabled(true);
        }else if(str_shoes_size.equals("fit")){
            RadioButton r = findViewById(R.id.shoes_size_fit);
            r.setChecked(true);
            r.setEnabled(true);
        }else{
            RadioButton r = findViewById(R.id.shoes_size_small);
            r.setChecked(true);
            r.setEnabled(true);
        }

        String str_ventilation = post.getVentilation();
        if(str_ventilation.equals("good")){
            RadioButton r = findViewById(R.id.ventilation_good);
            r.setChecked(true);
            r.setEnabled(true);
        }else if(str_ventilation.equals("best")){
            RadioButton r = findViewById(R.id.ventilation_best);
            r.setChecked(true);
            r.setEnabled(true);
        }else{
            RadioButton r = findViewById(R.id.ventilation_bad);
            r.setChecked(true);
            r.setEnabled(true);
        }

        String str_waterproof = post.getWaterproof();
        if(str_waterproof.equals("bad")){
            RadioButton r = findViewById(R.id.waterproof_bad);
            r.setChecked(true);
            r.setEnabled(true);
        }else if(str_waterproof.equals("best")){
            RadioButton r = findViewById(R.id.waterproof_best);
            r.setChecked(true);
            r.setEnabled(true);
        }else{
            RadioButton r = findViewById(R.id.waterproof_good);
            r.setChecked(true);
            r.setEnabled(true);
        }


        /** 핀치 줌 **/
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    /********************************
     ************ 핀치 줌 관련***********
     ********************************/

    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,
                    Math.min(mScaleFactor, 10.0f));
            dImage.setScaleX(mScaleFactor);
            dImage.setScaleY(mScaleFactor);
            return true;
        }
    }
    /********************************
     ************ 팔로우 관련***********
     ********************************/

    public void isFollowed(){
        Log.d("qwea","qweqwe");
//        firebaseFirestore.collection("follows").whereEqualTo("follower_id",user.getUser_id()).whereEqualTo("followed_id",post.getUser_id()).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        if(!queryDocumentSnapshots.isEmpty()){
//                            Follow l = queryDocumentSnapshots.toObjects(Follow.class).get(0);
//                            if(l.getStatus().equals("active")){
//                                followSwitch.setChecked(true);
//                            }
//                        }else{
//                            return;
//                        }
//                    }
//                });
        if(follow == null){
        }else if(follow.getStatus().equals("active")){
            followSwitch.setChecked(true);
        }else{
        }
        if(post.getUser_id().equals(user.getUser_id())){
            followSwitch.setVisibility(View.GONE);
        }

    }


    @Override
    public void onResume(){
        super.onResume();
    }


    public void deletePost(){
        firebaseFirestore.collection("posts").document(post_id).delete();
    }

    public void deletePostAffirm(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("포스트 삭제");
        builder.setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deletePost();
                        Toast.makeText(getApplicationContext(),"포스트가 삭제되었습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }






    /**********************************
     *             댓글 관련            *
     **********************************/

    public void getComments(){
//        if (!comments.isEmpty())
////            comments.clear();
        firebaseFirestore.collection("comments").whereEqualTo("post_id",post_id).whereEqualTo("status","active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            post_commet_count.setText(String.valueOf(0));
                            return;
                        } else{
                            comments = queryDocumentSnapshots.toObjects(Comment.class);
                            post_commet_count.setText(String.valueOf(comments.size()));
                            comments.sort(new CommentComparator());
                            adapter = new CommentAdapter(DetailedPost.this, comments);
                            commentListView.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(commentListView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("qweqweqwe", "아무내용이없습니다");
                    }
                });



    }

    //댓글 수만큼 listView 크기 설정
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        CommentAdapter listAdapter = (CommentAdapter) listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void writeComment(){
        WriteBatch batch = firebaseFirestore.batch();
        DocumentReference comment = firebaseFirestore.collection("comments").document();
        Map<String, Object> docData = new HashMap<>();

        String context = contextComment.getText().toString();
        docData.put("context", context);
        docData.put("user_id", firebaseAuth.getUid());
        docData.put("nickname", user.nickname);
        docData.put("post_id", post_id);
        docData.put("status","active");
        docData.put("id", comment.getId());

        // 댓글 날짜 DB
        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
        String format = s.format(new Date());

        docData.put("created_at",format);

        batch.set(comment,docData);
        batch.commit();
    }


    public void onClickCommentDelete(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("댓글 삭제");
        builder.setMessage("정말 삭제하시겠습니까?");

        int a = commentListView.getPositionForView(v);
        final String commentID = comments.get(a).id;
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseFirestore.collection("comments").document(commentID).update("status", "deactivated");
                        Toast.makeText(getApplicationContext(),"댓글이 삭제되었습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    //댓글 시간순으로 정렬
    public class CommentComparator implements Comparator<Comment> {
        @Override
        public int compare(Comment o1, Comment o2) {
            return o1.getCreated_at().compareTo(o2.getCreated_at());
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


    /**********************************
     *             좋아요 관련            *
     **********************************/
    public void getLikeCount(){
//        if (!comments.isEmpty())
////            comments.clear();
        firebaseFirestore.collection("likes").whereEqualTo("post_id",post_id).whereEqualTo("status","active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            post_like_count.setText(String.valueOf(0));
                            return;
                        } else{
                            List<Like> l = queryDocumentSnapshots.toObjects(Like.class);
                            post_like_count.setText(String.valueOf(l.size()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });



    }

    public void isLikePost(){
        firebaseFirestore.collection("likes").whereEqualTo("post_id",post_id).whereEqualTo("user_id",firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Like l = queryDocumentSnapshots.toObjects(Like.class).get(0);
                            if(l.status.equals("active")){
                                buttonLike.setImageResource(R.drawable.like_clicked);
                            }
                        }else{
                            return;
                        }
                    }
                });
    }



    public void onClickwriteComment(View v){
        writeComment();
        Toast.makeText(this, "댓글이 등록되었습니다!", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }


    public void onClickLike(View v){

        firebaseFirestore.collection("likes").whereEqualTo("post_id",post_id).whereEqualTo("user_id",firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            /* 좋아요 객체가 없을때 */
                            WriteBatch batch = firebaseFirestore.batch();
                            DocumentReference like = firebaseFirestore.collection("likes").document();
                            Map<String, Object> docData = new HashMap<>();

                            docData.put("user_id", firebaseAuth.getUid());
                            docData.put("post_id", post_id);
                            docData.put("id", like.getId());

                            // 댓글 날짜 DB
                            SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                            String format = s.format(new Date());

                            docData.put("created_at",format);
                            docData.put("status", "active");
                            buttonLike.setImageResource(R.drawable.like_clicked);

                            batch.set(like,docData);
                            batch.commit();

                            Toast.makeText(getApplicationContext(),"좋아요",Toast.LENGTH_LONG).show();
                        }else {
                            /* 좋아요 객체가 있을 때*/
                            Like l = queryDocumentSnapshots.toObjects(Like.class).get(0);
                            if (l.status.equals("active")) {
                                Log.d("qweasdzxc", "zxc");
                                firebaseFirestore.collection("likes").document(l.id).update("status", "deactivated");
                                buttonLike.setImageResource(R.drawable.like);
                                Toast.makeText(getApplicationContext(),"좋아요 취소",Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("qweasdzxc", "qqq");
                                firebaseFirestore.collection("likes").document(l.id).update("status", "active");
                                buttonLike.setImageResource(R.drawable.like_clicked);
                                Toast.makeText(getApplicationContext(),"좋아요",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                WriteBatch batch = firebaseFirestore.batch();
                DocumentReference like = firebaseFirestore.collection("likes").document();
                Map<String, Object> docData = new HashMap<>();

                docData.put("user_id", firebaseAuth.getUid());
                docData.put("post_id", post_id);
                docData.put("id", like.getId());

                // 댓글 날짜 DB
                SimpleDateFormat s = new SimpleDateFormat("yyyyMMddkkmmss");
                String format = s.format(new Date());

                docData.put("created_at",format);
                docData.put("status", "active");
                buttonLike.setImageResource(R.drawable.like_clicked);

                batch.set(like,docData);
                batch.commit();
            }
        });
    }

    /**********************************
     *             쪽지 관련            *
     **********************************/
//    public void onClicksendMessage(View v){
//        Intent intent = new Intent(this, SendMessage.class);
//        intent.putExtra("receiver_id",getIntent().getStringExtra("posting_user_id"));
//        intent.putExtra("receiver_nickname",dUid.getText().toString());
//        startActivity(intent);
//    }

}
