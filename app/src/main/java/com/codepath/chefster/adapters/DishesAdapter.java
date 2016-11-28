package com.codepath.chefster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.activities.DishDetailsActivity;
import com.codepath.chefster.activities.MainActivity;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.utils.LocalStorage;
import com.codepath.chefster.viewholders.DishViewHolder;

import org.parceler.Parcels;

import java.util.List;

public class DishesAdapter extends RecyclerView.Adapter<DishViewHolder> {
    final static public String DISH_KEY = "selected_dish";

    private Context context;
    private List<Dish> dishesList;
    private String category;
    private LocalStorage localStorage;
    private DishesInteractionListener listener;

    public DishesAdapter(Context context, List<Dish> recipes, String category) {
        this.context = context;
        this.dishesList = recipes;
        this.category = category;
        this.localStorage = ChefsterApplication.getLocalStorage();
        listener = (DishesInteractionListener) context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View favView = layoutInflater.inflate(R.layout.dish_item, parent, false);

        //Return the a new Holder instance
        DishViewHolder viewHolder = new DishViewHolder(favView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DishViewHolder holder, int position) {
        final Dish dish = dishesList.get(position);

        holder.tvMealTitle.setText(dish.getTitle());
        holder.tvMealSummary.setText(dish.getDescription());
        holder.tvCookingTime.setText("Est." + String.valueOf(dish.getPrepTime() + dish.getCookingTime()) + " mins");
        holder.tvMealRating.setText(String.valueOf(dish.getRating()));
        if (dish.getThumbnails() != null && !dish.getThumbnails().isEmpty()) {
            Glide.with(context).load(dish.getThumbnails().get(0)).into(holder.ivMealImage);
        }

        // when user Click on ItemImage it Will take Him To Dish Details
        holder.ivMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dish selectedDish  = dishesList.get(holder.getAdapterPosition());
                Intent intent = new Intent(getContext(), DishDetailsActivity.class);
                intent.putExtra(DISH_KEY, Parcels.wrap(selectedDish));
                getContext().startActivity(intent);
            }
        });

        holder.ivfavorite.setLiked(localStorage.isDishFavorited(dish.getUid()));
        holder.ivfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localStorage.isDishFavorited(dish.getUid())) {
                    localStorage.unfavoriteDish(dish.getUid());
                    holder.ivfavorite.setLiked(false);
                } else {
                    localStorage.favoriteDish(dish.getUid());
                    holder.ivfavorite.setLiked(true);
                }
                listener.onDishLiked();
            }
        });

        if (category != null) {
            if (!((MainActivity) context).selectedDishes.contains(dish)) {
                holder.btnAddToMenu.setText(R.string.add_to_menu);
                holder.btnAddToMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_dish, 0, 0, 0);
            } else {
                holder.btnAddToMenu.setText(R.string.added);
                holder.btnAddToMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_added_dish, 0, 0, 0);
            }
            holder.btnAddToMenu.setVisibility(View.VISIBLE);
            holder.btnAddToMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (!((MainActivity) context).selectedDishes.contains(dish)) {
                    if (((MainActivity) context).selectedDishes.size() < 3) {
                        holder.btnAddToMenu.setText(R.string.added);
                        holder.btnAddToMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_added_dish, 0, 0, 0);
                    }
                    listener.onDishAdded(dish);
                } else{
                    holder.btnAddToMenu.setText(R.string.add_to_menu);
                    holder.btnAddToMenu.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_dish, 0, 0, 0);
                    listener.onDishRemoved(dish);
                }
                }
            });
        } else {
            holder.btnAddToMenu.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (dishesList != null && !dishesList.isEmpty()) {
            return dishesList.size();
        }
        return 0;
    }

    public interface DishesInteractionListener {
        void onDishLiked();
        void onDishAdded(Dish dish);
        void onDishRemoved(Dish dish);
    }
}
