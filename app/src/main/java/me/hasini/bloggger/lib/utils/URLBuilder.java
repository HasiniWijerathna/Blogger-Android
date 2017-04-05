package me.hasini.bloggger.lib.utils;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class URLBuilder {

    /**
     * URL for login page
     */
    public static final String AUTH_ENDPOINT = Constants.API_BASE + "/auth/login";

    /**
     * URL for registration page
     */
    public static final String AUTH_REGISTER = Constants.API_BASE + "auth/register";

    /**
     * Returns the model URL
     * @param modelType The model type to fetch
     * @return The URL to use for network requests
     */
    public static final String modelURL(String modelType) {
        return Constants.API_BASE + "/" + modelType;
    }

    /**
     * Returns the model URL for a single model object
     * @param modelType The model type to fetch
     * @param modelId The model ID
     * @return The URL to use for network requests
     */
    public static String modelURL(String modelType, int modelId) {
        return Constants.API_BASE + "/" + modelType + "/" + modelId;
    }
}
