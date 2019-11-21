package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.Timestamp;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User implements Serializable {
    public String id;
    public String user_id;
    public String nickname;
    public String birth_date;
    public String sex;
    public String foot_width;
    public String foot_height;
    public String profile_url;
    public int age;
    public int foot_size;
    public Timestamp created_at;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String uid, String title, String body) {
//        this.user_id = uid;
        this.nickname = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFoot_height() {
        return foot_height;
    }
public String getBirth_date(){return birth_date;}
    public String getSex() {
        return sex;
    }

    public String getFoot_width() {
        return foot_width;
    }

    public int getAge() {
        return age;
    }

    public int getFoot_size() {
        return foot_size;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public String getProfile_url() {
        return profile_url;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("user_id", user_id);
        result.put("nickname", nickname);
        result.put("foot_height",foot_height);
        result.put("id",id);
        result.put("sex", sex);
        result.put("birth_date",birth_date);
        result.put("foot_width", foot_width);
        result.put("age", age);
        result.put("foot_size", foot_size);
        result.put("profile_url",profile_url);
        result.put("created_at",created_at);
        return result;
    }

}
