package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Comment {
    @Exclude
    public String id;
    public String user_id;
    public String post_id;
    public String context;
    public String nickname;
    public String created_at;


    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Comment(String user_id, String post_id, String context) {
        this.user_id = user_id;
        this.post_id = post_id;
        this.context = context;
    }

    public String getUser_id() {
        return user_id;
    }
    public String getCreated_at() {
        return created_at;
    }
    public String getNickname(){return nickname;}
    public String getPost_id() {
        return post_id;
    }

    public String getContext() {
        return context;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("post_id", post_id);
        result.put("nickname", nickname);
        result.put("context", context);
        result.put("created_at",created_at);
        return result;
    }
}
