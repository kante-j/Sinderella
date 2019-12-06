package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jipjung.hucomin.sinderella.CartActivities.CartDetail;
import com.jipjung.hucomin.sinderella.Classes.Cart;
import com.jipjung.hucomin.sinderella.Classes.Post;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
    private Cart cart;
    private User user;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private List<Cart> carts;
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

        firestore.collection("carts").whereEqualTo("user_id",user.getUser_id())
                .whereEqualTo("status","active").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    carts = queryDocumentSnapshots.toObjects(Cart.class);
                }
            }
        });

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
        holder.price.setText(String.valueOf(product.getPrice()));
        holder.brand.setText(product.getBrand());

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

        firebaseFirestore.collection("carts").whereEqualTo("product_id",product.getId()).whereEqualTo("user_id",user.getUser_id()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            cart = queryDocumentSnapshots.toObjects(Cart.class).get(0);
                        }else{
                            return;
                        }
                    }
                });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_feeds, null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");

        return new ViewHolder(v);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        products.clear();
        if (charText.length() == 0) {
            products.addAll(arrayList);
        } else {
            for (Product p : arrayList) {
                String t = p.getName();
                String b = p.getBrand();
                if (t.toLowerCase().contains(charText) ||
                        b.toLowerCase().contains(charText)){
                    products.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    public boolean idFilter() {
        boolean isEmpty = false;
        String user_id = user.getUser_id().toLowerCase(Locale.getDefault());
        products.clear();
        if (user_id.length() == 0) {
            products.addAll(arrayList);
        } else {
            if (carts !=null) {
                for (Product p : arrayList) {
                    for (Cart c : carts) {
                        if (c.getProduct_id().equals(p.getId())) {
                            products.add(p);
                        }
                    }
                }
            }else{
                isEmpty=true;
            }
            notifyDataSetChanged();
        }
        return isEmpty;
    }
    public void clear() {
        products.clear();
        products.addAll(arrayList);
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardview;
        private Product product;
        private ImageView image;
        private TextView price;
        private TextView brand;
        private TextView code_name;
        private TextView category;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.mypage_item_feeds_cardview);
            image = itemView.findViewById(R.id.shoes_image);
            category = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);
            brand = itemView.findViewById(R.id.brand);
            code_name = itemView.findViewById(R.id.shoes_code_name);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CartDetail.class);
                    intent.putExtra("product",product);
                    intent.putExtra("user",user);
                    intent.putExtra("cart",cart);
                    context.startActivity(intent);
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
