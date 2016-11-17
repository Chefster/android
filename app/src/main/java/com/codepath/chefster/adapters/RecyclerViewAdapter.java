package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Step;
import com.codepath.chefster.viewholders.RecyclerViewViewHolder;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
    List<List<Step>> stepsListList;
    Context context;

    public RecyclerViewAdapter(List<List<Step>> stepsListList, Context context) {
        this.stepsListList = stepsListList;
        this.context = context;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_horizontal_recycler_view, parent, false);

        int height = parent.getMeasuredHeight() / (stepsListList.size() > 1 ? 2 : 1);
        int width = parent.getMeasuredWidth();
        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));

        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
        List<Step> steps = stepsListList.get(position);
        ProgressAdapter adapter = new ProgressAdapter(steps, context);

        // vertical and cycle layout
        final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        holder.horizontalRecyclerView.setLayoutManager(layoutManager);
        holder.horizontalRecyclerView.setHasFixedSize(true);
        holder.horizontalRecyclerView.setAdapter(adapter);
        holder.horizontalRecyclerView.addOnScrollListener(new CenterScrollListener());
    }

    @Override
    public int getItemCount() {
        return stepsListList.size();
    }
}
