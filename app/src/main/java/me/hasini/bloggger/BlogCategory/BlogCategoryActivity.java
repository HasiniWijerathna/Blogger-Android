package me.hasini.bloggger.BlogCategory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import me.hasini.bloggger.BaseActivity;
import me.hasini.bloggger.BlogCategory.adapter.BlogCategoryAdapter;
import me.hasini.bloggger.BlogCategory.interfaces.BlogClickListner;
import me.hasini.bloggger.R;
import me.hasini.bloggger.home.HomeActivity;
import me.hasini.bloggger.lib.models.Blog;
import me.hasini.bloggger.lib.models.BlogCategory;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.login.LoginActivity;
import me.hasini.bloggger.post.PostActivity;

public class BlogCategoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private BlogCategoryAdapter blogCategoryAdapter;

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();

    /**
     * {@link NetworkManager}  Required networkManager reference
     */
    private NetworkManager networkManager;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeUIElements();
        setContentView(R.layout.activity_blog_category);
        networkManager = new NetworkManager(getApplicationContext());

        //Intent intent = getIntent();
        //BlogCategory categoryId = (BlogCategory) intent.getSerializableExtra("blogCategoryId");

        int categoryId;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            initializeModules();
            categoryId = bundle.getInt("blogCategoryId");
            RealmResults<BlogCategory> blogCategories = realm.where(BlogCategory.class).findAll();
            BlogCategory selectedCategory = blogCategories.where().equalTo("id",categoryId).findFirst();

            displayCategoryList(selectedCategory);
            //getCategoryBlogs(selectedCategory);

           if (isNetworkAvailable()) {
                getCategoryBlogs(selectedCategory);
            }

           // Toast.makeText(this, "" + selectedCategory.getName(), Toast.LENGTH_SHORT).show();

        }

    }

    private void initializeUIElements() {
      this.recyclerView = (RecyclerView) findViewById(R.id.blogCategory_recycler_view);
        //recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    private void getCategoryBlogs(BlogCategory selectedCategory) {
        String URL = URLBuilder.modelURLId("blogCategory", selectedCategory.getId());
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeGetRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                BlogCategory blogCategory = BlogCategory.deserialize(response.toString());
                //Log.d("hgg", blogCategory.toString());
                displayCategoryList(blogCategory);
            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.getErrorDetail());
            }

        });

    }

    private void displayCategoryList(BlogCategory selectedCategory) {
        this.recyclerView = (RecyclerView) findViewById(R.id.blogCategory_recycler_view);
        RealmList<Blog> blogs = selectedCategory.getBlogs();

        this.blogCategoryAdapter = new BlogCategoryAdapter(this, blogs, new BlogClickListner() {
            @Override
            public void OnClickBlog(Blog blog) {
                navigateToPost(blog);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(BlogCategoryActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(blogCategoryAdapter);
        // TODO: Show the blogs
    }

    private void navigateToPost(Blog blog) {
        int blogId = blog.getId();
        Intent intent = new Intent(BlogCategoryActivity.this, PostActivity.class);
        intent.putExtra("selectedBlogId", blogId);
        startActivity(intent);
    }

    private void initializeModules() {
        this.realm = Realm.getDefaultInstance();
    }


}
