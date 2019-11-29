package com.jipjung.hucomin.sinderella.Classes;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {

    @Exclude
    public String id;
    public String name;
    public String category;
    public String price;
    public String image_url;
    public String product_url;
    public String brand;
    public String shoes_code_name;
    public String created_at;


    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Product(String id, String name, String price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getProduct_url() {
        return product_url;
    }

    public String getShoes_code_name() {
        return shoes_code_name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("price", price);
        result.put("image_url", image_url);
        result.put("category",category);
        result.put("product_url",product_url);
        result.put("brand",brand);
        result.put("shoes_code_name",shoes_code_name);
        return result;
    }

}
