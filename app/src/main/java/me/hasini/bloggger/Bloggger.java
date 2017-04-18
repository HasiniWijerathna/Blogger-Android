package me.hasini.bloggger;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Calcey on 06-Apr-17.
 */

public class Bloggger extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
