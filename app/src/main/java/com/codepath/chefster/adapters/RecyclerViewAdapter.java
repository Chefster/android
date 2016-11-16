package com.codepath.chefster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.models.Step;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
    List<List<Step>> stepsList;
    Context context;
    List<ProgressAdapter> progressAdaptersList;

    public RecyclerViewAdapter(List<List<Step>> stepsList, Context context) {
        this.stepsList = stepsList;
        this.context = context;
    }

    @Override
    public RecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_horizontal_recycler_view, parent, false);
        return new RecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewViewHolder holder, int position) {
        Pro
        holder.horizontalRecyclerView.setAdapter();
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
