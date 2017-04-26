package me.hasini.bloggger.selectedPost;
;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.hasini.bloggger.BlogCategory.BlogCategoryActivity;
import me.hasini.bloggger.R;
import me.hasini.bloggger.editComment.EditCommentActivity;
import me.hasini.bloggger.home.HomeActivity;
import me.hasini.bloggger.lib.models.Comment;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.preference.PreferenceManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.login.LoginActivity;
import me.hasini.bloggger.selectedPost.adapter.CommentAdapter;
import me.hasini.bloggger.selectedPost.adapter.interfaces.CommentDeleteListner;
import me.hasini.bloggger.selectedPost.adapter.interfaces.CommentEditListner;

public class SelectedPostActivity extends AppCompatActivity {

    private static final String LOG_TAG = SelectedPostActivity.class.getSimpleName();
    private NetworkManager networkManager;
    private Realm realm;

    private ListView listView;
    private TextView postTile;
    private TextView postContent;
    private CommentAdapter commentAdapter;
    private CommentEditListner commentEditListner;
    private CommentDeleteListner commentDeleteListner;
    private TextView newComment;
    private Button saveComment;
    /**
     * {@Link PreferenceManager} Required PreferenceManager reference
     */
    private me.hasini.bloggger.lib.preference.PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_post);
        preferenceManager = PreferenceManager.getInstance(this.getApplicationContext());
        int postId = 0;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            postId = bundle.getInt("selectedPostId");
            initializeModules();
            initializeUIElements();
            getSelectedPost(postId);
            getCommentsOfPost();
            displayComments(postId);
        }

        final int finalPostId = postId;
        int loggedUserId = preferenceManager.getSession().getUser().getId();
        if(preferenceManager.getSession().getUser()!= null){
            saveComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addNewComment(finalPostId);
                    newComment.setText("");
                }
            });
        }
        else {
            AlertDialog dialog = new AlertDialog
                    .Builder(getApplicationContext())
                    .setCancelable(true)
                    .setMessage("Please login to add blogs!")
                   .setCancelable(true)
                    .create();
            dialog.show();
            Intent navigateTo = new Intent(SelectedPostActivity.this, LoginActivity.class);
            startActivity(navigateTo);

        }

    }

    private void addNewComment(final int id) {
        HashMap<String, Object> map = new HashMap<>();
        String URL = URLBuilder.modelURL("comment");
        String comment = newComment.getText().toString();
        String postId = Integer.toString(id);
        map.put("comment", comment);
        map.put("postId", postId);

        this.networkManager.makePostRequest(URL, map, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                realm.beginTransaction();
                RealmResults<Post> posts = realm.where(Post.class).findAll();
                Post selectedPost = posts.where().equalTo("id",id).findFirst();
                Comment comment  = Comment.deserialize(response.toString());
                selectedPost.getComments().add(comment);
                realm.copyToRealmOrUpdate(selectedPost);
                realm.commitTransaction();
                displayComments(id);

            }

            @Override
            public void onError(ANError anError) {
                    Log.e(LOG_TAG, anError.toString());
            }
        });

    }

    private void displayComments(int postId) {
//        getCommentsOfPost();
        int loggedUserId = preferenceManager.getSession().getUser().getId();
        this.listView = (ListView) findViewById(R.id.selected_post_comments);
        RealmResults<Post> posts = realm.where(Post.class).findAll();
        Post selectedPost = posts.where().equalTo("id",postId).findFirst();
        List<Comment> commentsOfPost = selectedPost.getComments();
//            commentAdapter = new CommentAdapter(this, commentsOfPost, commentEditListner, commentDeleteListner);
//        CommentEditListner editListner = new CommentEditListner() {
//            @Override
//            public void onClickCommentEdit(Comment comment) {
//                editComment(comment);
//            }
//        };
//
//        CommentDeleteListner deleteListner = new CommentDeleteListner() {
//            @Override
//            public void onClickCommentDelete(Comment comment) {
//                showCommentDeleteAuthentication(comment);
//
//            }
//        };
        commentAdapter = new CommentAdapter(this, commentsOfPost, networkManager,loggedUserId ,new CommentEditListner() {
            @Override
            public void onClickCommentEdit(Comment comment) {
                editComment(comment);
            }
        }, new CommentDeleteListner() {
            @Override
            public void onClickCommentDelete(Comment comment) {
                showCommentDeleteAuthentication(comment);
            }
        });
        listView.setAdapter(commentAdapter);
    }

    private void editComment(Comment comment) {
        int commentId = comment.getId();
        String URL = URLBuilder.modelURLId("comment",commentId);
        HashMap<String, String> bodyParams = new HashMap<>();
       // bodyParams.put("comment", authName);
       // this.networkManager.makePutRequest();
        Intent intent = new Intent(SelectedPostActivity.this, EditCommentActivity.class);
        intent.putExtra("selectedCommentId", commentId);
        startActivity(intent);
    }

    private void showCommentDeleteAuthentication(final Comment comment) {
        AlertDialog dialog = new AlertDialog
                .Builder(this)
                .setCancelable(true)
                .setMessage("Are you sure you want to delete the comment?")
                .setNegativeButton("No" , new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedComment(comment);
                       commentAdapter.notifyDataSetChanged();
                    }
                })
                .create();
        dialog.show();
        }

    private void deleteSelectedComment(Comment comment) {
        int commentId = comment.getId();
        final int postId = comment.getPostId();
        String URL = URLBuilder.modelURLId("comment", commentId);
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeDeleteRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                getCommentsOfPost();
                displayComments(postId);
            }

            @Override
            public void onError(ANError anError) {
                AlertDialog dialog = new AlertDialog
                        .Builder(getApplicationContext())
                        .setCancelable(true)
                        .setMessage("Invalid User!")
                        .setCancelable(true)
                        .create();
                dialog.show();
                Log.e(LOG_TAG, anError.getErrorDetail());
            }
        });
    }

    private void getCommentsOfPost() {
        String URL = URLBuilder.modelURL("comment");
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

                List<Comment> comments = Comment.deserializeCollection(results);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(comments);
                realm.commitTransaction();
            }
            RealmResults<Comment> comments = realm.where(Comment.class).findAll();

            @Override
            public void onError(ANError anError)  {
                Log.e(LOG_TAG, anError.getErrorDetail());
            }
        });
    }
    private void initializeUIElements() {
        postTile = (TextView) findViewById(R.id.selected_post_title);
        postContent = (TextView) findViewById(R.id.selected_post_content);
        postTile.setTypeface(EasyFonts.robotoMedium(this));
        postContent.setTypeface(EasyFonts.robotoMedium(this));
        newComment = (TextView)findViewById(R.id.selected_post_new_comment);
        saveComment = (Button)findViewById(R.id.selected_post_save);
    }

    private void initializeModules() {
        this.networkManager = new NetworkManager(getApplicationContext());
        this.realm = Realm.getDefaultInstance();
    }

    private void getSelectedPost(int postId) {
        String URL = URLBuilder.modelURLId("post", postId);
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeGetRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Post post = Post.deserialize(response.toString());
                displayPost(post);
            }

            @Override
            public void onError(ANError anError) {
                AlertDialog dialog = new AlertDialog
                        .Builder(getApplicationContext())
                        .setCancelable(true)
                        .setMessage("Invalid User!")
                        .setCancelable(true)
                        .create();
                dialog.show();



                Log.e(LOG_TAG, anError.getErrorDetail());
            }

        });

    }

    private void displayPost(Post post) {
        postTile.setText(post.getTitle());
        postContent.setText(post.getContent());
    }

}
