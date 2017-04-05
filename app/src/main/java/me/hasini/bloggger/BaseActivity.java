package me.hasini.bloggger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Calcey on 04-Apr-17.
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * Checks if the network connection is available
     * @return TRUE if the connection is available
     */
    public boolean isNetworkAvailable () {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
