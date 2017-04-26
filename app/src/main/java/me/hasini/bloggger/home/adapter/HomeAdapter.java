package me.hasini.bloggger.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import me.hasini.bloggger.R;
import me.hasini.bloggger.home.BlogCategoryClickListner;
import me.hasini.bloggger.lib.models.BlogCategory;

/**
 * Created by Calcey on 07-Apr-17.
 */

public class HomeAdapter extends
        RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private BlogCategoryClickListner blogCategoryClickListner;
    private LayoutInflater layoutInflater;
    private Context context;

    public HomeAdapter(Context context, List<BlogCategory> blogCategories, BlogCategoryClickListner blogCategoryClickListner ) {
        this.context = context;
        this.blogCategories = blogCategories;
        this.blogCategoryClickListner = blogCategoryClickListner;
        this.layoutInflater = LayoutInflater.from(context);
    }

    private String[] wallpapers = new String[] {
            "http://xtream.ch/wp-content/uploads/2016/01/wallpaperSunset.jpg",
            "http://www.wallpaperhdi.com/wp-content/uploads/2016/06/Material_Design_Wallpaper_Image_HD.jpg",
            "http://www.androidguys.com/wp-content/uploads/2014/11/image_new-11.png"
    };

    private List<BlogCategory> blogCategories;

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blogcategory, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        final BlogCategory blogCategory = blogCategories.get(position);
        holder.nameTextView.setText(blogCategory.getName());
        holder.blogCategoryImage.setImageUrl(blogCategory.getImageURL());
        holder.blogCategoryImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                blogCategoryClickListner.onClickBlogCategory(blogCategory);

            }
        });


    }

    @Override
    public int getItemCount() {
        return blogCategories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ANImageView blogCategoryImage;
        RelativeLayout blogCategoryLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.blogCategory_name);
            blogCategoryImage = (ANImageView) itemView.findViewById(R.id.blogCategory_image);
            blogCategoryLayout = (RelativeLayout) itemView.findViewById(R.id.blogcategory_layout);
        }
    }

    public void setBlogCategories(List<BlogCategory> blogCategories) {
        this.blogCategories = blogCategories;

        this.notifyDataSetChanged();
    }
}
