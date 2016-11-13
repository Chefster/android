package com.codepath.chefster.adapters;

import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.codepath.chefster.ProgressFooterViewHolder;
import com.codepath.chefster.ProgressHeaderViewHolder;
import com.codepath.chefster.ProgressItemViewHolder;
import com.codepath.chefster.R;
import com.dd.CircularProgressButton;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.truizlop.sectionedrecyclerview.SimpleSectionedAdapter;

/**
 * Created by Hezi Eliyahu on 12/11/2016.
 */

public class ProgressAdapter extends SectionedRecyclerViewAdapter<ProgressHeaderViewHolder,
        ProgressItemViewHolder,ProgressFooterViewHolder> {
    private CircularProgressButton circularButton1;

    @Override
    protected int getSectionCount() {
        return 2;
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 3;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected ProgressHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_progress_header, parent, false);
        return new ProgressHeaderViewHolder(view);
    }

    @Override
    protected ProgressFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected ProgressItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_progress_item, parent, false);
        return new ProgressItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(ProgressHeaderViewHolder holder, int section) {
        holder.getTvHeaderTitle().setText("SECTION" + (section+1));
    }

    @Override
    protected void onBindSectionFooterViewHolder(ProgressFooterViewHolder holder, int section) {

    }

    protected String[][] agenda = {{"A", "B", "C"},{"A2", "B2", "C2"}};
    @Override
    protected void onBindItemViewHolder(ProgressItemViewHolder holder, int section, int position) {
        holder.getTvItemTitle().setText(agenda[section][position]);
        holder.setCircularButton(holder);
    }
}
