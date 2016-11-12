package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;

/**
 * Created by PRAGYA on 11/12/2016.
 */

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{

    private Context context;

    public FavoritesListAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public FavoritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View favView = layoutInflater.inflate(R.layout.favorite_item, parent, false);
        //Return the a new Holder instance
        FavoritesViewHolder viewHolder = new FavoritesViewHolder(favView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FavoritesViewHolder holder, int position) {

        holder.tvMealTitle.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
