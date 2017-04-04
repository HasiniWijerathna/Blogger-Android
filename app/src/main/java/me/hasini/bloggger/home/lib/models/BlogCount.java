package me.hasini.bloggger.home.lib.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class BlogCount extends RealmObject implements Serializable {

    @PrimaryKey
    private int id;
    @SerializedName("BlogId")
    private int blogId;
    @SerializedName("UserId")
    private int userId;

    public BlogCount(int id, int blogId, int userId) {

        this.id = id;
        this.blogId = blogId;
        this.userId = userId;
    }

    public BlogCount() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


}
