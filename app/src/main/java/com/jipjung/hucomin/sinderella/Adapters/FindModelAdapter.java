package com.jipjung.hucomin.sinderella.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.jipjung.hucomin.sinderella.CartActivities.CartDetail;
import com.jipjung.hucomin.sinderella.Classes.Product;
import com.jipjung.hucomin.sinderella.Classes.User;
import com.jipjung.hucomin.sinderella.PostActivities.FindModel;
import com.jipjung.hucomin.sinderella.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FindModelAdapter extends RecyclerView.Adapter<FindModelAdapter.ViewHolder>{
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

    public FindModelAdapter(Context context, List<Product> products, int item_layout, User user) {
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
    public void onBindViewHolder(final FindModelAdapter.ViewHolder holder, int position){
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
    }

    @Override
    public FindModelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_feeds, null);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://sinderella-d45a8.appspot.com");

        return new FindModelAdapter.ViewHolder(v);
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
                    Intent intent = new Intent();
                    intent.putExtra("product",product);
                    ((FindModel)context).setResult(50,intent);
                    ((FindModel)context).finish();
                }
            });
        }
    }
}
