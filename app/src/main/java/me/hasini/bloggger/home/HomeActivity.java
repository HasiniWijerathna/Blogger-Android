package me.hasini.bloggger.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.hasini.bloggger.BaseActivity;
import me.hasini.bloggger.BlogCategory.BlogCategoryActivity;
import me.hasini.bloggger.home.adapter.HomeAdapter;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.BlogCategory;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;

public class HomeActivity extends BaseActivity {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    /**
     * {@link NetworkManager}  Required networkManager reference
     */
    private NetworkManager networkManager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.initializeModules();
        this.initializeUIElements();
        this.queryAndShowCategoryList();
        this.fetchBlogCategories();
    }

    private void initializeModules() {
        this.networkManager = new NetworkManager(this.getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }

    private void initializeUIElements() {
        this.recyclerView = (RecyclerView) findViewById(R.id.blogCategory_recycler_view);
    }

    private void fetchBlogCategories() {
        String URL = URLBuilder.modelURL("blogCategory");
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeGetRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                String results = null;

                try {
                    results = response.getJSONArray("results").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                List<BlogCategory> blogCategory = BlogCategory.deserializeCollection(results);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(blogCategory);
                realm.commitTransaction();

                queryAndShowCategoryList();
            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.toString());
            }
        });
    }

    private void queryAndShowCategoryList() {
        RealmResults<BlogCategory> blogCategories = realm.where(BlogCategory.class).findAll();

        if (this.homeAdapter == null) {
//            this.homeAdapter = new HomeAdapter(blogCategories, new BlogCategoryClickListner() {
//                @Override
//                public void onClickBlogCategory(BlogCategory blogCategory) {
//                    Toast.makeText(HomeActivity.this,
//                            "Your Message", Toast.LENGTH_LONG).show();
//                }
//            });
            this.homeAdapter = new HomeAdapter(this, blogCategories, new BlogCategoryClickListner() {
                @Override
                public void onClickBlogCategory(BlogCategory blogCategory) {

                    //showToastMessage(blogCategory);
                    navigateToBlogCategory(blogCategory);

                }
            });
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(homeAdapter);


        } else {
            homeAdapter.setBlogCategories(blogCategories);
        }
    }

    private void navigateToBlogCategory(BlogCategory blogCategory) {
        int blogCategoryId = blogCategory.getId();
        Intent intent = new Intent(HomeActivity.this, BlogCategoryActivity.class);
        intent.putExtra("blogCategoryId", blogCategoryId);
        startActivity(intent);
    }


    private void showToastMessage(BlogCategory blogCategoty) {

        Toast.makeText(HomeActivity.this, "" + blogCategoty.getName(), Toast.LENGTH_SHORT).show();
    }
}
