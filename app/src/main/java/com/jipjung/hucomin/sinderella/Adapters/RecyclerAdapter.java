package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.PostActivities.DetailedPost;
import com.jipjung.hucomin.sinderella.Classes.Like;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//피드 어댑터
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private User user;
    private Post post;
    private Follow follow;
    private int item_layout;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    public ArrayList<Post> arrayList;

    private DocumentReference documentReference;
    private List<User> users;

    public RecyclerAdapter(Context context, List<Post> posts, int item_layout, User user) {
        this.context = context;
        this.posts = posts;
        this.item_layout = item_layout;
        this.user = user;

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<>();
        arrayList.addAll(posts);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypage_item_feeds, null);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");
        // TODO : 여기에다가 likes 데이터 불러온거 저장해놓고 onBindViewHolder에서 settext만 해주면 될까?
        // TODO : users의 닉네임도 마찬가지

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

          post = posts.get(position);
          holder.h_post = post;
//        Iterator<User> iterator = users.iterator();
//        User user = null;
//        while (iterator.hasNext()) {
//            user = iterator.next();
//            if (user.user_id.equals(post.getUid())) {
//                break;
//            }
//        }
        firestore.collection("follows").whereEqualTo("follower_id",firebaseAuth.getUid()).whereEqualTo("followed_id",post.getUser_id()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Follow l = queryDocumentSnapshots.toObjects(Follow.class).get(0);
                            if(l.getStatus().equals("active")){
                                holder.h_follow = l;
                            }
                        }else{
                            return;
                        }
                    }
                });

        //TODO : 그냥 FCOOK 이런데서 불러올 때 포스트 객체에다가 닉네임 칼럼 추가해서 넘겨주는게 로딩 안걸리고 젤 좋은것 같다...
//        firestore.collection("users").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(!documentSnapshot.exists()){
//                    return;
//                }else{
//                    user = documentSnapshot.toObject(User.class);
////                    String nickname = user.getNickname();
////                    holder.posting_user_id.setText(user.getUser_id());
////                    holder.feed_nickname.setText(nickname);
//                }
//            }
//        });

        holder.feed_nickname.setText(post.getNickname());


        holder.title.setText(post.getTitle());
        holder.body.setText(post.getBody());
        if(post.getBody().length() > 200){
            ViewGroup.LayoutParams l = holder.body.getLayoutParams();
            l.height = 200;
            holder.body.setLayoutParams(l);
        }
        holder.post_id= post.getId();
        holder.post_category = post.getCategory();

//        like count 설정
//        Like like = null;
//        int likecount = 0;
//        for(int i=0; i<likes.size(); i++){
//            if(post.id.equals(likes.get(i).getPost_id())){
//                likecount++;
//            }
//        }

//        if(like == null){
//            holder.like_counts.setText(String.valueOf(0));
//        }else{
//            holder.like_counts.setText(String.valueOf(likecount));
//        }
        firestore.collection("likes").whereEqualTo("post_id",post.id).whereEqualTo("status","active").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            holder.like_counts.setText(String.valueOf(0));
                        }else {
                            holder.like_counts.setText(String.valueOf(queryDocumentSnapshots.size()));
                        }
                    }
                });

        firestore.collection("likes").whereEqualTo("post_id",post.id).whereEqualTo("user_id",firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Like l = queryDocumentSnapshots.toObjects(Like.class).get(0);
                            if(l.status.equals("active")){
                                holder.like.setImageResource(R.drawable.like_clicked);
                            }
                        }
                    }
                });
//        holder.feed_nickname.setText(user.getNickname());

        //리사이클러뷰 각각의 아이템에 유저 닉네임 보이도록 표시

//        try{
//            Date date = new SimpleDateFormat("yyyyMMddkkmmss").parse(post.getCreated_at());
//            String format = new SimpleDateFormat("yyyy/MM/dd kk:mm").format(date);
//            holder.time.setText(format);
//        }catch(ParseException pe){
//            pe.printStackTrace();
//        }
        if (post.getImageURL() != null) {
            StorageReference path = storageRef.child(post.image_url);
            Glide.with(this.context).load(path).skipMemoryCache(true).into(holder.image);
            holder.url = post.getImageURL();
        }
        else {
            holder.url = null;
            ViewGroup.LayoutParams l = holder.image.getLayoutParams();
            l.width = 0;
            l.height =0;
            holder.image.setLayoutParams(l);
        }
    }

    //검색 기능
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        posts.clear();
        if (charText.length() == 0) {
            posts.addAll(arrayList);
        } else {
            for (Post p : arrayList) {
                String t = p.getTitle();
                String b = p.getBody();
                String n = p.getNickname();
                if (t.toLowerCase().contains(charText) ||
                b.toLowerCase().contains(charText) ||
                n.toLowerCase().contains(charText)) {
                    posts.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView body;
        private TextView feed_nickname;
        private CardView cardview;
        private ImageView image;
        private String url;
        private TextView time;
        private String post_id;
        private TextView like_counts;
        private ImageView like;
        private String post_category;
        private TextView posting_user_id;
        private Post h_post;
        private Follow h_follow;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.postedImage);

            title = (TextView) itemView.findViewById(R.id.title);
            body = (TextView) itemView.findViewById(R.id.detail);
            feed_nickname = (TextView) itemView.findViewById(R.id.feed_nickname);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            time = (TextView) itemView.findViewById(R.id.time);
            like_counts = itemView.findViewById(R.id.likes_count);
            like = itemView.findViewById(R.id.like);
//            posting_user_id = itemView.findViewById(R.id.posting_user_id);


            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pTitle = title.getText().toString();
                    String pBody = body.getText().toString();
                    String pUid = feed_nickname.getText().toString();
//                    String pTime = time.getText().toString();
                    Intent intent = new Intent(context, DetailedPost.class);
//                    intent.putExtra("TIME", pTime);
                    intent.putExtra("user",user);
                    intent.putExtra("post",h_post);
                    intent.putExtra("follow",h_follow);
                    intent.putExtra("TITLE", pTitle);
                    intent.putExtra("BODY", pBody);
                    intent.putExtra("UID", pUid);
//                    intent.putExtra("TIME", pTime);
                    intent.putExtra("URL", url);
                    intent.putExtra("POSTID",post_id);
//                    intent.putExtra("posting_user_id",posting_user_id.getText().toString());
                    intent.putExtra("CATEGORY",post_category);
                    context.startActivity(intent);
                }
            });
        }
    }
}
