package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private Product product;
    private List<Product> products;
    private int item_layout;
    private String id;
    private String name;
    private String category;
    private String price;
    private String image_url;
    private String product_url;
    private User user;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public ArrayList<Product> arrayList;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    public ProductAdapter(Context context, List<Product> products, int item_layout, User user) {
        this.context = context;
        this.products = products;
        this.item_layout = item_layout;
        this.user = user;

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<>();
        arrayList.addAll(products);
    }


    @Override
    public int getItemCount() {
        return this.products.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        product = products.get(position);
        holder.product = product;
        holder.category.setText(product.getCategory());
        holder.code_name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.productURL.setText(product.getProduct_url());

        if (product.getImage_url() != null) {
            StorageReference path = storageRef.child(product.image_url);
            Glide.with(this.context).load(path).skipMemoryCache(true).into(holder.image);
//            holder.url = product.getImage_url();
        }
        else {
//            holder.url = null;
            ViewGroup.LayoutParams l = holder.image.getLayoutParams();
            l.width = 0;
            l.height =0;
            holder.image.setLayoutParams(l);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_feeds, null);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");

        return new ViewHolder(v);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardview;
        private Product product;
        private ImageView image;
        private TextView price;
        private TextView productURL;
        private TextView code_name;
        private TextView category;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.mypage_item_feeds_cardview);
            image = itemView.findViewById(R.id.shoes_image);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            productURL = itemView.findViewById(R.id.brand);
            code_name = itemView.findViewById(R.id.shoes_code_name);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String pTitle = title.getText().toString();
//                    String pBody = body.getText().toString();
//                    String pUid = feed_nickname.getText().toString();
////                    String pTime = time.getText().toString();
//                    Intent intent = new Intent(context, DetailedPost.class);
////                    intent.putExtra("TIME", pTime);
//                    intent.putExtra("user",user);
//                    intent.putExtra("post",h_post);
//                    intent.putExtra("follow",h_follow);
//                    intent.putExtra("TITLE", pTitle);
//                    intent.putExtra("BODY", pBody);
//                    intent.putExtra("UID", pUid);
////                    intent.putExtra("TIME", pTime);
//                    intent.putExtra("URL", url);
//                    intent.putExtra("POSTID",post_id);
////                    intent.putExtra("posting_user_id",posting_user_id.getText().toString());
//                    intent.putExtra("CATEGORY",post_category);
//                    context.startActivity(intent);
                }
            });
        }
    }

}
