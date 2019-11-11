package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Like {
    @Exclude
    public String id;
    public String user_id;
    public String post_id;
    public String created_at;
    public String status;


    public Like() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Like(String user_id, String post_id, String created_at, String status) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.status = status;
        this.created_at = created_at;
    }

    public String getUser_id() {
        return user_id;
    }
    public String getCreated_at() {
        return created_at;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getStatus() {
        return status;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("post_id", post_id);
        result.put("status", status);
        result.put("created_at",created_at);
        return result;
    }
}
