package me.hasini.bloggger.lib.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import me.hasini.bloggger.BuildConfig;
import me.hasini.bloggger.lib.models.AppSession;
import me.hasini.bloggger.lib.preference.PreferenceManager;
import me.hasini.bloggger.lib.utils.URLBuilder;

/**
 * Created by Calcey on 04-Apr-17.
 */
public class NetworkManager {

    private PreferenceManager preferenceManager;

    public NetworkManager(@NonNull Context context) {
       preferenceManager = PreferenceManager.getInstance(context);

    }
    //
//    //Base URL
//    private static final String BASE_URL = BuildConfig.API_BASE;
//
//    /**
//     * Make an HTTP GET request to get all blogs
//     * @param listener {@link JSONObjectRequestListener} instance for callbacks
//     */
//    public void getAllBlogs(JSONObjectRequestListener listener) {
//        GET(BASE_URL + "blog", null, listener);
//    }
//
//    /**
//     * Make an HTTP GET request to get all posts
//     * @param listener {@link JSONObjectRequestListener} instance for callbacks
//     */
//   public void getAllPosts(JSONObjectRequestListener listener) {
//       GET(BASE_URL + "post", null, listener);
//   }
//
//   public void login(JSONObjectRequestListener listener) {
//       POST(URLBuilder.AUTH_ENDPOINT, null, listener);
//   }

    /**
     * Make an HTTP GET request
     * @param url The URL to GET from
     * @param queryParams Data to be sent as request parameters
     * @param listener {@link JSONObjectRequestListener} instance for callbacks
     */
    public void GET(@NonNull String url,@Nullable HashMap<String, String> queryParams,
                      @NonNull JSONObjectRequestListener listener) {

        // Create request builder instance
        ANRequest.GetRequestBuilder requestBuilder = AndroidNetworking.get(url);

        // Add query params
        requestBuilder.addQueryParameter(queryParams);

        // Build the request
        ANRequest request = requestBuilder.build();

        // Attach the listener
        request.getAsJSONObject(listener);
    }

    /**
     * Make an HTTP POST request
     * @param url The URL to POST to
     * @param bodyParams Data to be sent in the request body
     * @param listener {@link JSONObjectRequestListener} instance for callbacks
     */
    public void POST(@NonNull String url, @Nullable HashMap<String, String> bodyParams,
                      @NonNull JSONObjectRequestListener listener) {

        // Create request builder instance
        ANRequest.PostRequestBuilder requestBuilder = AndroidNetworking.post(url);

        // Add body parameters
        requestBuilder.addBodyParameter(bodyParams);


        // Build the request
        ANRequest request = requestBuilder.build();

        // Attach the listener
        request.getAsJSONObject(listener);
    }

    public void makeGetRequest(@NonNull final String url, @NonNull final HashMap<String, String> bodyParams,
                                @NonNull final JSONObjectRequestListener listener) {
        AppSession appSession = this.preferenceManager.getSession();
        //There is a current session
        if(appSession != null) {
            if(System.currentTimeMillis() > appSession.getExpiration()){
                //Session has expired
                //Refresh for a new token
                GET(URLBuilder.REFRESH_TOKEN_ENDPOINT, new HashMap<String, String>(), new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Sets a new session
                        AppSession appSession = AppSession.deserialize(response.toString());
                        preferenceManager.setSession(appSession);

                        //Make the request
                        GET(url,bodyParams,listener);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.onError(anError);
                    }
                });
            } else {
                //Session still active
                //Make the request
                GET(url,bodyParams,listener);
            }

        } else {
            //There is no session
            //Make the request
            POST(url,bodyParams,listener);
        }

    }
    public void makePostRequest(@NonNull final String url, @NonNull final HashMap<String, String> bodyParams,
                                @NonNull final JSONObjectRequestListener listener){
        AppSession appSession = this.preferenceManager.getSession();
        //There is a current session
        if(appSession != null) {
            if(System.currentTimeMillis() > appSession.getExpiration()){
                //Session has expired
                //Refresh for a new token
                GET(URLBuilder.REFRESH_TOKEN_ENDPOINT, new HashMap<String, String>(), new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Sets a new session
                        AppSession appSession = AppSession.deserialize(response.toString());
                        preferenceManager.setSession(appSession);

                        //Make the request
                        POST(url, bodyParams,listener);
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.onError(anError);
                    }
                });
            } else {
                //Session still active
                //Make the request
                POST(url,bodyParams,listener);
            }
        } else {
            //There is no session
            //Make the request
            POST(url, bodyParams, listener);
        }

    }
}



