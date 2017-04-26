package me.hasini.bloggger.addPost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import me.hasini.bloggger.R;

public class AddPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        int blogId;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            blogId = bundle.getInt("selectedBlogId");
            //RealmResults<Blog> blogs = realm.where(Blog.class).findAll();
            // Blog selectedBlog = blogs.where().equalTo("id",blogId).findFirst();
            Toast.makeText(AddPostActivity.this,"selectedBlogId" + blogId, Toast.LENGTH_LONG).show();

        }
    }

    }

