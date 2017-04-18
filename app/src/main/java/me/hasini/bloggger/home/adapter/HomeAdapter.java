package me.hasini.bloggger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.widget.ANImageView;

import java.util.List;

import me.hasini.bloggger.lib.models.BlogCategory;

/**
 * Created by Calcey on 07-Apr-17.
 */

public class HomeAdapter extends
        RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public HomeAdapter(List<BlogCategory> blogCategories) {
        this.blogCategories = blogCategories;
    }

    private List<BlogCategory> blogCategories;

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blogcategory, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        BlogCategory blogCategory = blogCategories.get(position);
        holder.nameTextView.setText(blogCategory.getName());
        holder.blogCategoryImage.setImageUrl(blogCategory.getImageURL());
        holder.blogCategoryImage.setDefaultImageResId(R.drawable.giphy);

    }

    @Override
    public int getItemCount() {
        return blogCategories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ANImageView blogCategoryImage;

        ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.blogCategory_name);
            blogCategoryImage = (ANImageView) itemView.findViewById(R.id.blogCategory_image);

        }
    }

    public void setBlogCategories(List<BlogCategory> blogCategories) {
        this.blogCategories = blogCategories;

        this.notifyDataSetChanged();
    }
}
