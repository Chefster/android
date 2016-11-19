package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.models.Ingredient;
import com.codepath.chefster.viewholders.IngredientsViewHolder;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {
    private Context context;
    private List<Ingredient> ingredientsList;

    public Context getContext() {
        return context;
    }

    public IngredientsAdapter(Context context, List<Ingredient> ingredientsList) {
        this.context = context;
        this.ingredientsList = ingredientsList;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        Ingredient ingredient = ingredientsList.get(position);

        if ( position % 2 != 0 )
            holder.getTvIngredient().setBackgroundResource(R.color.cardview_shadow_start_color);
        holder.getTvIngredient().setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            return ingredientsList.size();
        }
        return 0;
    }
}
