package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Cart implements Serializable {
    @Exclude
    public String id;
    public String user_id;
    public String product_id;
    public String created_at;
    public String status;


    public Cart() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Cart(String user_id, String product_id, String created_at, String status) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getStatus() {
        return status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id",id);
        result.put("user_id",user_id);
        result.put("product_id",product_id);
        result.put("status", status);
        result.put("created_at",created_at);
        return result;
    }
}
