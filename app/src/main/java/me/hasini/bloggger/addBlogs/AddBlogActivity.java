package me.hasini.bloggger.addBlogs;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.hasini.bloggger.R;
import me.hasini.bloggger.addPost.AddPostActivity;
import me.hasini.bloggger.lib.models.Blog;
import me.hasini.bloggger.lib.models.BlogCategory;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.preference.PreferenceManager;
import me.hasini.bloggger.lib.utils.URLBuilder;


public class AddBlogActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddBlogActivity.class.getSimpleName();
    private PreferenceManager preferenceManager;
    private NetworkManager networkManager;
    private Realm realm;

    private BlogCategory selectedBlogCategory;
    private EditText blogTitle;
    private String blogTitleValue;
    private int newBlogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        initializeUIElements();
        initializeModules();
        getBlogCategories();
        setSpinner();

        Button save = (Button)findViewById(R.id.add_blog_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(blogTitleValue != null) {
                    addBlogtoDatabase();
                } else {
                        AlertDialog dialog = new AlertDialog
                                .Builder(getApplicationContext())
                                .setCancelable(true)
                                .setMessage("Blog title can not be empty")
                                .create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                }
            }
        });
    }

    private void initializeUIElements() {
        blogTitle = (EditText) findViewById(R.id.add_blog_blog_title);
        blogTitleValue= blogTitle.getText().toString();
    }

    private void addBlogtoDatabase() {
        String URL = URLBuilder.modelURL("blog");
        HashMap<String, Object> map = new HashMap<>();
        blogTitleValue= blogTitle.getText().toString();
        map.put("name", blogTitleValue);
        String category = Integer.toString(selectedBlogCategory.getId());
        map.put("category", category);
        this.networkManager.makePostRequest(URL, map, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                realm.beginTransaction();
                Blog newBlog  = Blog.deserialize(response.toString());
                realm.copyToRealmOrUpdate(newBlog);
                realm.commitTransaction();
                newBlogId = newBlog.getId();

                navigateToAddPost(newBlogId);
            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.toString());
            }
        });



    }

    private void navigateToAddPost(int blogId) {
        Intent intent = new Intent(AddBlogActivity.this, AddPostActivity.class);
        intent.putExtra("newBlogId", blogId);
        startActivity(intent);
        finish();
    }

    private void setSpinner() {
        Spinner dropdown = (Spinner)findViewById(R.id.category_spinner);

        final RealmResults<BlogCategory> blogCategories = realm.where(BlogCategory.class).findAll();
        ArrayAdapter<BlogCategory> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, blogCategories);
        dropdown.setAdapter(adapter);

        onBlogCategorySelected(selectedBlogCategory);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BlogCategory selectedBlogCategory = blogCategories.get(position);
                AddBlogActivity.this.onBlogCategorySelected(selectedBlogCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onBlogCategorySelected(BlogCategory blogCategory) {
        this.selectedBlogCategory = blogCategory;
    }


    private void getBlogCategories() {
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

            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.toString());
            }
        });
    }


    private void initializeModules() {
        this.networkManager = new NetworkManager(getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }
}
