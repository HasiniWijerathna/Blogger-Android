package me.hasini.bloggger.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.hasini.bloggger.BaseActivity;
import me.hasini.bloggger.BlogCategory.BlogCategoryActivity;
import me.hasini.bloggger.TestActivity;
import me.hasini.bloggger.addBlogs.AddBlogActivity;
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

        FloatingActionButton addBlogs = (FloatingActionButton) findViewById(R.id.floating_button_add_blog);
        addBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, AddBlogActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeModules() {
        this.networkManager = new NetworkManager(this.getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }

    private void initializeUIElements() {
        FloatingActionButton addBlogs = (FloatingActionButton) findViewById(R.id.floating_button_add_blog);
        this.recyclerView = (RecyclerView) findViewById(R.id.blogCategory_recycler_view);
        //recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerView.setItemAnimator(animator);

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
            this.homeAdapter = new HomeAdapter(this, blogCategories, new BlogCategoryClickListner() {
                @Override
                public void onClickBlogCategory(BlogCategory blogCategory) {
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

}
