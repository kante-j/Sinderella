package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.net.URL;
import java.text.BreakIterator;
import java.util.ArrayList;

public class FollowArrayAdapter extends RecyclerView.Adapter<FollowArrayAdapter.ViewHolder> {

    private int textViewResourcedId;
    //    private FirebaseAuth firebaseAuth;
//    private FirebaseFirestore firestore;
//    private User user;
    private ArrayList<String> arrayfollow;
    private Context context;
    private URL url;
//    private FirebaseFirestore firebaseFirestore;
//    private FirebaseStorage storage;
//    private StorageReference storageRef;

    public FollowArrayAdapter(Context context, int textViewResourcedId ,ArrayList<String> arrayfollow){
        this.context = context;
        this.arrayfollow= arrayfollow;
        this.textViewResourcedId = textViewResourcedId;
//        this.user = user;

//        firestore = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public FollowArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(context).inflate(R.layout.item_users,parent,false);
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        storage = FirebaseStorage.getInstance();
//        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item =arrayfollow.get(position);
        holder.feed_nickname.setText(item);
    }

    @Override
    public int getItemCount() {
        return arrayfollow.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView picture_post;
        private TextView feed_nickname;
        private CardView cardView;

        public ViewHolder(View itemview) {

            super(itemview);

            picture_post = itemview.findViewById(R.id.picture_post);
            feed_nickname = itemview.findViewById(R.id.feed_nickname);
            cardView = itemview.findViewById(R.id.follow_cardview);


        }
    }



}
