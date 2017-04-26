package me.hasini.bloggger.post;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.post.adapter.PostAdapter;
import me.hasini.bloggger.post.interfaces.PostClickListner;
import me.hasini.bloggger.selectedPost.SelectedPostActivity;

public class PostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListView listView;
    private PostAdapter postAdapter;
    private static final String LOG_TAG = PostActivity.class.getSimpleName();

    private Realm realm;
    private NetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        int blogId;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            initializeModules();
            getCategoryPosts();
            blogId = bundle.getInt("selectedBlogId");
            displayPostList(blogId);
            //RealmResults<Blog> blogs = realm.where(Blog.class).findAll();
           // Blog selectedBlog = blogs.where().equalTo("id",blogId).findFirst();

        }
    }

    private void getCategoryPosts() {
        String URL = URLBuilder.modelURL("post");
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

                List<Post> posts = Post.deserializeCollection(results);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(posts);
                realm.commitTransaction();

            }

            @Override
            public void onError(ANError anError)  {
                Log.e(LOG_TAG, anError.getErrorDetail());
            }
        });

    }

    private void displayPostList(int blogId) {
        getCategoryPosts();
        this.recyclerView = (RecyclerView) findViewById(R.id.post_recycler_view);

//        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
//        recyclerView.setItemAnimator(animator);
        RealmResults<Post> posts = realm.where(Post.class).findAll();

        this.postAdapter = new PostAdapter(this, posts, new PostClickListner() {
            @Override
            public void onPostClick(Post post) {
                int postId = post.getId();
                navigateToSelectedPost(postId);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(PostActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AlphaInAnimationAdapter(postAdapter));
        recyclerView.setAdapter(postAdapter);

    }

    private void navigateToSelectedPost(int postId) {
        Intent intent = new Intent(PostActivity.this, SelectedPostActivity.class);
        intent.putExtra("selectedPostId", postId);
        startActivity(intent);
    }

    private void initializeModules() {
        this.networkManager = new NetworkManager(getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }
}
