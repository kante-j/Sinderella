package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message implements Serializable {
    @Exclude
    public String id;
    public String sender_id;
    public String sender_nickname;
    public String receiver_id;
    public String receiver_nickname;
    public String title;
    public String context;
    public String created_at;
    public String updated_at;
    public String status;


    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Message(String sender_id, String sender_nickname, String created_at, String receiver_id) {
        this.sender_id = sender_id;
        this.sender_nickname = sender_nickname;
        this.receiver_id = receiver_id;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getSender_nickname() {
        return sender_nickname;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiver_nickname() {
        return receiver_nickname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("sender_id", sender_id);
        result.put("sender_nickname", sender_nickname);
        result.put("receiver_id", receiver_id);
        result.put("title", title);
        result.put("context", context);
        result.put("created_at",created_at);
        result.put("status",status);
        result.put("receiver_nickname",receiver_nickname);
        result.put("updated_at",updated_at);
        return result;
    }
}
