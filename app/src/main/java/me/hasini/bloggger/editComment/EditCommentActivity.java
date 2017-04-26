package me.hasini.bloggger.editComment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.HashMap;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmResults;
import me.hasini.bloggger.R;
import me.hasini.bloggger.home.HomeActivity;
import me.hasini.bloggger.lib.models.AppSession;
import me.hasini.bloggger.lib.models.Comment;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.register.RegistrationActivity;
import me.hasini.bloggger.selectedPost.SelectedPostActivity;

public class EditCommentActivity extends AppCompatActivity {
    private static final String LOG_TAG = EditCommentActivity.class.getSimpleName();
    private NetworkManager networkManager;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        final int commentId;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            commentId = bundle.getInt("selectedCommentId");
            Toast.makeText(EditCommentActivity.this,
                            "Edit comment", Toast.LENGTH_LONG).show();

            Button save = (Button) findViewById(R.id.edit_comment_save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editComment(commentId);
                }
            });
        }
     }



    private void editComment(final int commentId) {
        EditText newComment = (EditText) findViewById(R.id.edit_comment);
        final String comment = newComment.getText().toString();
        this.networkManager = new NetworkManager(getApplicationContext());
        this.realm = Realm.getDefaultInstance();
        String URL = URLBuilder.modelURLId("comment", commentId);
        HashMap<String, String> map = new HashMap<>();
            map.put("comment",comment );

        networkManager.makePutRequest(URL, map, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                Comment comment  = Comment.deserialize(response.toString());
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(comment);
                realm.commitTransaction();
                int postId = comment.getPostId();
                Intent intent = new Intent(EditCommentActivity.this, SelectedPostActivity.class);
                intent.putExtra("selectedPostId", postId);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.toString());
            }
        });

   }
}

