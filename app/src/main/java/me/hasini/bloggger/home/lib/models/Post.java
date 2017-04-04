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

public class Post extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    private String title;
    private String content;

    @SerializedName("BlogId")
    private int blogId;

    @SerializedName("UserId")
    private int userId;

    public Post(int id, String title, String content, int blogId, int userId) {

        this.id= id;
        this.title = title;
        this.content = content;
        this.blogId = blogId;
        this.userId = userId;
    }

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static Post deserialize(String jsonString) {
        return new Gson().fromJson(jsonString, Post.class);
    }

    public static List<Post> deserializeCollection(String jsonString) {
        Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

        return new Gson().fromJson(jsonString, listType);
    }

    public String serialize() throws JSONException {
        return new Gson().toJson(this);
    }

}
