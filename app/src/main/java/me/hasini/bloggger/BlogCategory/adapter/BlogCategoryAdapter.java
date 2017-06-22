package me.hasini.bloggger.BlogCategory.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import me.hasini.bloggger.BlogCategory.interfaces.BlogClickListner;
import me.hasini.bloggger.R;
import me.hasini.bloggger.lib.models.Blog;
import me.hasini.bloggger.lib.models.BlogCategory;
import me.hasini.bloggger.lib.network.NetworkManager;
import me.hasini.bloggger.lib.utils.URLBuilder;

/**
 * Created by Calcey on 18-Apr-17.
 */

public class BlogCategoryAdapter extends
        RecyclerView.Adapter<BlogCategoryAdapter.ViewHolder> {

    private static final String LOG_TAG = BlogCategoryAdapter.class.getSimpleName();
    private BlogClickListner blogClickListner;
    private List<Blog> blogs;
    private Context context;
    private NetworkManager networkManager;
    private String blogCategoryUrl;

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
       getBlogCategotyId(blog);
        holder.nameTextView.setText(blog.getName());
        holder.imageView.setImageURI(Uri.parse(blogCategoryUrl));
        holder.nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogClickListner.OnClickBlog(blog);
            }
        });

    }

    private void getBlogCategotyId(Blog blog) {
        final int blogCategoryId = blog.getBlogCategoryId();
        networkManager = new NetworkManager(context);
        String URL = URLBuilder.modelURLId("blogCategory", blogCategoryId);
        HashMap<String, String> bodyParams = new HashMap<>();
        this.networkManager.makeGetRequest(URL, bodyParams, new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                BlogCategory blogCategory = BlogCategory.deserialize(response.toString());
                blogCategoryUrl = blogCategory.getImageURL();
            }

            @Override
            public void onError(ANError anError) {
                Log.e(LOG_TAG, anError.getErrorDetail());
            }

        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView)itemView.findViewById(R.id.blog_name);
            //Typeface typeface=Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
            nameTextView.setTypeface(EasyFonts.robotoMedium(context));
            imageView = (ImageView) itemView.findViewById(R.id.imageView_scroll);
        }
    }


}