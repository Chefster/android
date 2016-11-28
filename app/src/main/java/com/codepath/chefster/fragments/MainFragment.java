package com.codepath.chefster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.adapters.CategoryAdapter;
import com.codepath.chefster.models.Category;
import com.codepath.chefster.utils.ItemClickSupport;
import com.codepath.chefster.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainFragment extends BaseFragment {

    private List<Category> categoryList;
    CategoryClickedListener listener;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.rvCategoryGrid) RecyclerView rvCategories;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);
        initializeRecyclerView();

        ItemClickSupport.addTo(rvCategories).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Category category = categoryList.get(position);
                FragmentTransaction trans = getFragmentManager().beginTransaction();
        /*
         * IMPORTANT: We use the "root frame" defined in
         * "root_fragment.xml" as the reference to replace fragment
         */
                trans.replace(R.id.frame_container, DishesFragment.newInstance(category.getName()));

        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
            }
        });
        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        rvCategories.setLayoutManager(manager);
        rvCategories.setItemAnimator(new SlideInUpAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(30);
        rvCategories.addItemDecoration(decoration);
        categoryAdapter = new CategoryAdapter(getActivity(), getCategoryList());
        rvCategories.setAdapter(categoryAdapter);
    }

    //Will give the list of categories.
    //for now its hard coded, later can get from database
    private List<Category> getCategoryList() {
        try {
            categoryList = new ArrayList<>();
            categoryList.add(new Category("American", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_american.jpg?alt=media&token=7ce009ae-48be-4dad-8bd6-a8d5c27ccfe0"));
            categoryList.add(new Category("Israeli", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_israeli.jpg?alt=media&token=366d30d6-49ad-4316-8080-eabb906d475c"));
            categoryList.add(new Category("Indian", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_indian.jpg?alt=media&token=fbaa409d-e3ac-44db-aa3b-0f8cd0a5dce4"));
            categoryList.add(new Category("Italian", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_italian.jpg?alt=media&token=f1b87847-e52c-4002-9273-e8d7a3e6922b"));
            categoryList.add(new Category("Vietnamese", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_vietnamese.jpg?alt=media&token=9f0ae1af-2f3e-4e68-a957-51a5a6b407d1"));
            categoryList.add(new Category("Thai", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_thai.jpg?alt=media&token=8d6b62c1-160d-462e-a452-741987043ac4"));
            categoryList.add(new Category("Spanish", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_spanish.jpg?alt=media&token=bd0ecd68-a3b5-4b31-a435-c9ed96e88f0f"));
            categoryList.add(new Category("French", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_french.jpg?alt=media&token=77f98685-1007-42d4-8b4d-c019317c9b87"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryClickedListener) {
            listener = (CategoryClickedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CategoryClickedListener {
        void onCategoryclicked(String category);
    }
}
