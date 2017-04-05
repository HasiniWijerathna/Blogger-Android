package me.hasini.bloggger.lib.models;

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

public class Comment extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;

    private String comment;

    @SerializedName("PostId")
    private int postId;

    public Comment(int id, String comment, int postId) {
        this.id = id;
        this.comment = comment;
        this.postId = postId;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public static Comment deserialize(String jsonString) {
        return new Gson().fromJson(jsonString, Comment.class);
    }

    public static List<Comment> deserializeCollection(String jsonString) {
        Type listType = new TypeToken<ArrayList<Comment>>(){}.getType();

        return new Gson().fromJson(jsonString, listType);
    }

    public String serialize() throws JSONException {
        return new Gson().toJson(this);
    }
}
