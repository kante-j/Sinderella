package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Follow {
    @Exclude
    public String id;
    public String follower_id;
    public String followed_id;
    public String created_at;
    public String status;


    public Follow() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Follow(String user_id, String post_id, String created_at, String status) {
        this.follower_id = user_id;
        this.followed_id = post_id;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getFollower_id() {
        return follower_id;
    }

    public String getFollowed_id() {
        return followed_id;
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
        result.put("follower_id", follower_id);
        result.put("followed_id", followed_id);
        result.put("status", status);
        result.put("created_at",created_at);
        return result;
    }
}
