package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View categoryView = layoutInflater.inflate(R.layout.category_item, parent, false);
        //Return the a new Holder instance
        CategoryViewHolder viewHolder = new CategoryViewHolder(categoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        //for now hardcoding as we dont have any data model for category
        
        Glide.with(context).load("http://images.media-allrecipes.com/userphotos/720x405/233383.jpg").into(holder.ivCategory);
        holder.tvCategoryName.setText("Indian");
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
