package me.hasini.bloggger.selectedPost.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Comment;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.preference.PreferenceManager;
import me.hasini.bloggger.lib.utils.URLBuilder;
import me.hasini.bloggger.selectedPost.adapter.interfaces.CommentDeleteListner;
import me.hasini.bloggger.selectedPost.adapter.interfaces.CommentEditListner;

/**
 * Created by Calcey on 19-Apr-17.
 */

public class CommentAdapter extends BaseAdapter {

    private static final String LOG_TAG = CommentAdapter.class.getSimpleName();
    private List<Comment> comments;
    private int loggedUserId;
    private LayoutInflater layoutInflater;
    private NetworkManager networkManager;
    private CommentEditListner commentEditListner;
    private CommentDeleteListner commentDeleteListner;
    private Realm realm;


    public CommentAdapter(Context context,
                          List<Comment> comments,
                          NetworkManager networkManager,
                          int loggedUserId,
                          CommentEditListner commentEditListner,
                          CommentDeleteListner commentDeleteListner
                          ) {
        this.comments = comments;
        this.loggedUserId = loggedUserId;
        this.layoutInflater = LayoutInflater.from(context);
        this.commentEditListner = commentEditListner;
        this.networkManager = networkManager;
        this.commentDeleteListner = commentDeleteListner;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comments.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CommentViewHolder commentViewHolder;
        final Comment comment = (Comment)getItem(position);
        getComment(position);
        if(convertView == null) {

            convertView = this.layoutInflater.inflate(R.layout.item_comment, viewGroup, false);
            commentViewHolder = new CommentViewHolder();
            commentViewHolder.comment = (TextView)convertView.findViewById(R.id.comment);
            commentViewHolder.edit = (Button)convertView.findViewById(R.id.item_comment_edit);
            commentViewHolder.delete = (Button)convertView.findViewById(R.id.item_comment_delete);

            convertView.setTag(commentViewHolder);
        } else  {
            commentViewHolder = (CommentAdapter.CommentViewHolder) convertView.getTag();
        }

        commentViewHolder.comment.setText(String.valueOf(comment.getComment()));

        if (loggedUserId == comment.getUserId()) {

            commentViewHolder.edit.setVisibility(View.VISIBLE);
            commentViewHolder.delete.setVisibility(View.VISIBLE);

            commentViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentEditListner.onClickCommentEdit(comment);
                }
            });


            commentViewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentDeleteListner.onClickCommentDelete(comment);
                }
            });
        } else {
            commentViewHolder.edit.setVisibility(View.GONE);
            commentViewHolder.edit.setOnClickListener(null);

            commentViewHolder.delete.setVisibility(View.GONE);
            commentViewHolder.delete.setOnClickListener(null);
        }

        return convertView;
    }

    private int getComment(int commentId) {
        int userId;
        String URL = URLBuilder.modelURL("comment");
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeGetRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                String results = null;
               // Comment comment = Comment.deserialize(response.toString());
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

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.getErrorDetail());
            }
        });
       // RealmResults<Comment> comments = realm.where(Comment.class).findAll();
       // Comment comment = comments.where().equalTo("id", commentId).findFirst();
       //userId = comment.getUserId();

       //return userId;
        return 0;
    }

    private static class CommentViewHolder {
        TextView comment;
        Button edit;
        Button delete;
    }
}
