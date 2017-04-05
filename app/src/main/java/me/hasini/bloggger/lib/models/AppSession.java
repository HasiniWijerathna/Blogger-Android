package me.hasini.bloggger.lib.models;

import com.google.gson.Gson;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class AppSession {
    /**
     *  Token
     */
    private String token;

    /**
     * Session user object
     */
    private User user;

    /**
     * Session expiration
     */
    private long expiration;

    public AppSession(String authToken, User user, long expiration) {

        this.token = authToken;
        this.user = user;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String authToken) {
        this.token = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    /**
     * Create an {@link AppSession} instance by deserializing the given JSON String
     * @param jsonString The JSON String to deserialize
     * @return Deserialized {@link AppSession} instance
     */
    public static AppSession deserialize(String jsonString) {
        return new Gson().fromJson(jsonString, AppSession.class);
    }

    public String serialize() {
        return new Gson().toJson(this);
    }
}
