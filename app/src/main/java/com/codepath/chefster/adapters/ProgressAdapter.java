package com.codepath.chefster.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.viewholders.ProgressFooterViewHolder;
import com.codepath.chefster.viewholders.ProgressHeaderViewHolder;
import com.codepath.chefster.viewholders.ProgressItemViewHolder;
import com.codepath.chefster.R;
import com.dd.CircularProgressButton;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

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
