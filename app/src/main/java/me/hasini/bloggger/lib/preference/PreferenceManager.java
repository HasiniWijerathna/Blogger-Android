package me.hasini.bloggger.lib.preference;

import android.content.Context;
import android.content.SharedPreferences;

import me.hasini.bloggger.lib.models.AppSession;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class PreferenceManager {
    private static PreferenceManager preferenceManager;

    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "blogger";
    private static final String SESSION_NAME = "session";

    /**
     * Private method to create a new class object
     * @param context Application{@link Context}
     */
    private PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_APPEND);
    }

    /**
     * Get an instance of {@link PreferenceManager}
     * @param context The Application{@link Context}
     * @return Created {@link PreferenceManager} instance
     */
    public static PreferenceManager getInstance(Context context) {
        if(preferenceManager == null) {
            preferenceManager = new PreferenceManager(context);
        }

        return preferenceManager;
    }

    /**
     * Gets the current Appsession from SharedPreferences
     * @return Appsession instance
     */
    public AppSession getSession (){
        AppSession appSession = null;
        if(sharedPreferences.contains(SESSION_NAME)) {
            appSession = AppSession.deserialize(sharedPreferences.getString(SESSION_NAME, null));
        }

        return appSession;
    }

    /**
     * Persists the given {@link AppSession} object to {@link SharedPreferences}
     * @param appSession The new {@link AppSession} to be persisted
     */
    public void setSession(AppSession appSession) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SESSION_NAME, appSession.serialize());
        editor.apply();
    }
}
