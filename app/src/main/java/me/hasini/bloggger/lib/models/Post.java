package me.hasini.bloggger.lib.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class Post extends RealmObject implements Serializable {

    /**
     *ID of the post
     */
    @PrimaryKey
    private int id;

    /**
     * Title of the post
     */
    private String title;

    /**
     * Content of the post
     */
    private String content;

    /**
     * ID of the blog
     */
    @SerializedName("BlogId")
    private int blogId;

    /**
     * ID of the user
     */
    @SerializedName("UserId")
    private int userId;

    /**
     * List of comments
     */
    @SerializedName("Comments")
    private RealmList<Comment> comments;

    public Post(int id, String title, String content, int blogId, int userId, RealmList<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.blogId = blogId;
        this.userId = userId;
        this.comments = comments;
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
    public RealmList<Comment> getComments() {
        return comments;
    }

    public void setComments(RealmList<Comment> comments) {
        this.comments = comments;
    }

    public static List<Post> deserializeCollection(String jsonString) {
        Type listType = new TypeToken<ArrayList<Post>>(){}.getType();

        return new Gson().fromJson(jsonString, listType);
    }

    public String serialize() throws JSONException {
        return new Gson().toJson(this);
    }

}
