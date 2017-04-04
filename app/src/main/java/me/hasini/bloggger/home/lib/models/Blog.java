package me.hasini.bloggger.home.lib.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Calcey on 04-Apr-17.
 */
public class Blog extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;

    private String name;
    private int count;

    @SerializedName("UserId")
    private int userId;

    @SerializedName("BlogCategoryId")
    private int blogCategoryId;

    public Blog(int id, String name, int count, int userId, int blogCategoryId) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.userId = userId;
        this.blogCategoryId = blogCategoryId;
    }

    public Blog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlogCategoryId() {
        return blogCategoryId;
    }

    public void setBlogCategoryId(int blogCategoryId) {
        this.blogCategoryId = blogCategoryId;
    }

    public static Blog deserialize(String jsonString) {
        return new Gson().fromJson(jsonString, Blog.class);
    }

    public static List<Blog> deserializeCollection(String jsonString) {
        Type listType = new TypeToken<ArrayList<Blog>>(){}.getType();

        return new Gson().fromJson(jsonString, listType);
    }

    public String serialize() throws JSONException {
        return new Gson().toJson(this);
    }
}
