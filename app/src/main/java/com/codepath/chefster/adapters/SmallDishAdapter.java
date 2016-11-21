package com.codepath.chefster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.chefster.R;
import com.codepath.chefster.activities.DishDetailsActivity;
import com.codepath.chefster.models.Dish;
import com.codepath.chefster.models.Tool;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This adapter is for the MealLaunchActivity and it holds all the chosen dishes
 */
public class SmallDishAdapter extends RecyclerView.Adapter<SmallDishAdapter.ViewHolder> {
    private Context context;
    private List<Dish> dishes;

    public Context getContext() {
        return context;
    }

    public SmallDishAdapter(Context context, List<Dish> dishes) {
        this.context = context;
        this.dishes = dishes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dish_launch_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Dish dish = dishes.get(position);
        Glide.with(context).load(dish.getThumbnails().get(0)).centerCrop().into(holder.getDishImageView());
        holder.getDishTitleTextView().setText(dish.getTitle());
        StringBuilder tools = new StringBuilder();
        for (Tool tool : dish.getTools()) {
            tools.append(tool.getAmount()).append(" ").append(tool.getName()).append(", ");
        }
        tools.setLength(tools.length() - 2);

        String summary = "Est. " + (dish.getPrepTime() + dish.getCookingTime()) + " minutes. Tools:"
                + tools.toString();
        holder.getDishSummaryTextView().setText(summary);

        holder.getMainLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DishDetailsActivity.class);
                intent.putExtra("selected_dish", Parcels.wrap(dish));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.meal_launch_main_layout) RelativeLayout mainLayout;
        @BindView(R.id.image_view_dish_image) ImageView dishImageView;
        @BindView(R.id.text_view_dish_title) TextView dishTitleTextView;
        @BindView(R.id.text_view_dish_summary) TextView dishSummaryTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public ImageView getDishImageView() {
            return dishImageView;
        }

        public TextView getDishTitleTextView() {
            return dishTitleTextView;
        }

        public TextView getDishSummaryTextView() {
            return dishSummaryTextView;
        }

        public RelativeLayout getMainLayout() {
            return mainLayout;
        }
    }
}
