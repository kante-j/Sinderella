package com.jipjung.hucomin.sinderella.Classes;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

@IgnoreExtraProperties
public class Post implements Serializable{
    @Exclude
    public String id;
    public String image_url;
    public String user_id;
    public String nickname;
    public String title;
    public String body;
    public String category;
    public String product;
    public String shoes_weight;
    public String ventilation;
    public String shoes_size;
    public String buyURL;
    public String waterproof;
    public int price;
    public float rating;
    public String created_at;
    public int shoe_size_num;
    public Bitmap bitmap;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String title, String body) {
        this.user_id = uid;
        this.title = title;
        this.body = body;
    }


    public int getPrice() {
        return price;
    }

    public int getShoe_size_num() {
        return shoe_size_num;
    }

    public String getBuyURL() {
        return buyURL;
    }

    public String getProduct() {
        return product;
    }

    public String getImageURL() {
        return image_url;
    }
    public String getTitle() {
        return this.title;
    }
    public String getBody() {
        return this.body;
    }
    public String getCategory() {return this.category;}
    public String getCreated_at() {return this.created_at;}
    public String getId(){ return this.id;}
    public String getNickname(){ return this.nickname;}
    public float getRating() {return rating; }
    public String getImage_url() { return image_url; }
    public String getUser_id() { return user_id; }
    public String getShoes_weight() { return shoes_weight; }
    public String getVentilation() { return ventilation; }
    public String getShoes_size() { return shoes_size; }
    public String getWaterproof() { return waterproof; }
    public Bitmap getBitmap() { return bitmap; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("title", title);
        result.put("body", body);
        result.put("image_url", image_url);
        result.put("nickname",nickname);
        result.put("category",category);
        result.put("created_at",created_at);
        result.put("id",id);
        result.put("price",price);
        result.put("product",product);
        result.put("rating",rating);
        result.put("shoes_weight",shoes_weight);
        result.put("user_id",user_id);
        result.put("ventilation",ventilation);
        result.put("shoe_size",shoes_size);
        result.put("waterproof",waterproof);
        result.put("shoe_size_num",shoe_size_num);
        result.put("buyURL",buyURL);
        return result;
    }

    public <T extends Post> T withId(@NonNull final String id) {
        this.id = id;
        return (T) this;
    }
}
