package me.hasini.bloggger.BlogCategory.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import me.hasini.bloggger.BlogCategory.interfaces.BlogClickListner;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Blog;

/**
 * Created by Calcey on 18-Apr-17.
 */

public class BlogCategoryAdapter extends
        RecyclerView.Adapter<BlogCategoryAdapter.ViewHolder> {

    private BlogClickListner blogClickListner;
    private List<Blog> blogs;
    private Context context;

    public BlogCategoryAdapter(Context context, List<Blog> blogs, BlogClickListner blogClickListner) {
        this.blogs = blogs;
        this.blogClickListner = blogClickListner;
        this.context = context;
    }

    @Override
    public BlogCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog, parent, false);
        return new BlogCategoryAdapter.ViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(BlogCategoryAdapter.ViewHolder holder, int position) {
       final Blog blog = blogs.get(position);
        holder.nameTextView.setText(blog.getName());
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogClickListner.OnClickBlog(blog);
            }
        });

    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView)itemView.findViewById(R.id.blog_name);
            //Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
            nameTextView.setTypeface(EasyFonts.robotoMedium(context));
        }
    }
}