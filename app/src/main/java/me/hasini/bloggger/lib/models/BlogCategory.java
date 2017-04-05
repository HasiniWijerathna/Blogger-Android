package me.hasini.bloggger.lib.models;

import com.google.gson.Gson;
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

public class BlogCategory extends RealmObject implements Serializable {

    /**
     * ID of the blog category
     */
    @PrimaryKey
    private int id;
    /**
     * Name of the blog category
     */
    private String name;

    /**
     * Active flag
     */
    private boolean active;

    /**
     * URL of the image
     */
    private String imageURL;

    public BlogCategory(int id, String name, boolean active, String imageURL) {

        this.id = id;
        this.name = name;
        this.active = active;
        this.imageURL = imageURL;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BlogCategory() {

    }

    public static BlogCategory deserialize(String jsonString) {
        return new Gson().fromJson(jsonString, BlogCategory.class);
    }

    public static List<BlogCategory> deserializeCollection(String jsonString) {
        Type listType = new TypeToken<ArrayList<BlogCategory>>(){}.getType();

        return new Gson().fromJson(jsonString, listType);
    }

    public String serialize() throws JSONException {
        return new Gson().toJson(this);
    }
}
