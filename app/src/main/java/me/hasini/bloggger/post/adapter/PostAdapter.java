package me.hasini.bloggger.post.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import io.realm.RealmResults;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Post;
import me.hasini.bloggger.post.interfaces.PostClickListner;
import us.feras.mdv.MarkdownView;

/**
 * Created by Calcey on 18-Apr-17.
 */

public class PostAdapter extends
        RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private PostClickListner postClickListner;
    private List<Post> posts;
    private Context context;

    public PostAdapter(Context context, List<Post> posts, PostClickListner postClickListner) {
        this.postClickListner = postClickListner;
        this.posts = posts;
        this.context = context;
    }


    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostAdapter.ViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        final Post post = posts.get(position);

        holder.nameView.setText(post.getTitle());
        //holder.contentView.setText(post.getContent());
        holder.contentView.loadMarkdown(post.getContent());
        holder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postClickListner.onPostClick(post);
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        //TextView contentView;
        MarkdownView contentView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView)itemView.findViewById(R.id.post_title);
            //contentView = (TextView) itemView.findViewById(R.id.post_content);
            contentView = (MarkdownView) itemView.findViewById(R.id.post_content);
            nameView.setTypeface(EasyFonts.robotoMedium(context));
           // contentView.setTypeface(EasyFonts.robotoMedium(context));
        }

    }

}
