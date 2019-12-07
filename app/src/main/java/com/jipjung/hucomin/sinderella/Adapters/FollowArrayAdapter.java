package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Follow;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.MyMenuActivities.OtherMyMenu;
import com.jipjung.hucomin.sinderella.R;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class FollowArrayAdapter extends RecyclerView.Adapter<FollowArrayAdapter.ViewHolder> {

    private int textViewResourcedId;
    //    private FirebaseAuth firebaseAuth;
//    private FirebaseFirestore firestore;
    private User user;
    private List<Follow> arrayfollow;
    private Context context;
    private URL url;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private User followed_user;

    public FollowArrayAdapter(Context context, int textViewResourcedId , List<Follow> arrayfollow, User user){
        this.context = context;
        this.arrayfollow= arrayfollow;
        this.textViewResourcedId = textViewResourcedId;
        this.user = user;
        Log.d("qweqwe",String.valueOf(arrayfollow.size()));
//        this.user = user;

//        firestore = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public FollowArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.item_users,parent,false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Follow item =arrayfollow.get(position);

        firebaseFirestore.collection("users").document(item.followed_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                followed_user= documentSnapshot.toObject(User.class);

                holder.post_user =followed_user;
                holder.feed_nickname.setText(followed_user.getNickname());

                holder.user = user;
                holder.follow = item;
                if (followed_user.getProfile_url()!=null) {
                    StorageReference path = storageRef.child("/profiles/"+item.getFollowed_id());
                    Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true).centerCrop().into(holder.picture_post);
//            holder.url = product.getImage_url();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayfollow.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView picture_post;
        private TextView feed_nickname;
        private CardView cardView;
        private User user;
        private User post_user;
        private Follow follow;

        public ViewHolder(View itemview) {

            super(itemview);

            picture_post = itemview.findViewById(R.id.picture_post);
            feed_nickname = itemview.findViewById(R.id.feed_nickname);
            cardView = itemview.findViewById(R.id.follow_cardview);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OtherMyMenu.class);
                    intent.putExtra("user",user);
                    intent.putExtra("post_user",post_user);
                    intent.putExtra("follow",follow);
                    context.startActivity(intent);
                }
            });

        }
    }



}
