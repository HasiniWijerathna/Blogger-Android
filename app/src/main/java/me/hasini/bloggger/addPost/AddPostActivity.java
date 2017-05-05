package me.hasini.bloggger.addPost;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;
import java.util.HashMap;

import io.realm.Realm;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.post.PostActivity;

public class AddPostActivity extends AppCompatActivity {

    private static final Object LOG_TAG =  AddPostActivity.class.getSimpleName();
    private NetworkManager networkManager;
    private Realm realm;

    private EditText postTitle;
    private EditText postContent;
    private String postTitleValue;
    private String postContentValue;
    private int blogId;
    private int blogCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initializeModules();
        initializeUIElements();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        blogId = bundle.getInt("newBlogId");
        //getSelectedBlog(blogId);
        Toast.makeText(AddPostActivity.this,"newblogId " + blogId, Toast.LENGTH_LONG).show();
        Button save = (Button) findViewById(R.id.post_save);
        if(bundle != null){
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPostToDatabase(blogId);
                    Intent intent = new Intent(AddPostActivity.this, PostActivity.class);
                    intent.putExtra("selectedBlogId", blogId);
                    startActivity(intent);

                }
            });
        }
    }
    private void addPostToDatabase(int blogId) {
        String URL = URLBuilder.modelURL("post");
        HashMap<String, Object> map = new HashMap<>();
        postTitleValue= postTitle.getText().toString();
        postContentValue = postContent.getText().toString();
        map.put("title", postTitleValue);
        map.put("content", postContentValue);
        String newBlogId = Integer.toString(blogId);
        map.put("blogId", newBlogId);

        this.networkManager.makePostRequest(URL, map, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                realm.beginTransaction();
                Post newPost = Post.deserialize(response.toString());
                realm.copyToRealmOrUpdate(newPost);
                realm.commitTransaction();
            }

            @Override
            public void onError(ANError anError) {
                Log.e(String.valueOf(LOG_TAG), anError.toString());
            }
        });
    }

    private void initializeUIElements() {
         postTitle = (EditText) findViewById(R.id.post_title);
         postContent = (EditText) findViewById(R.id.post_content);

    }


    private void initializeModules() {
        this.networkManager = new NetworkManager(getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }

    }

