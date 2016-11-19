package com.codepath.chefster.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.activities.DishListActivity;
import com.codepath.chefster.adapters.CategoryAdapter;
import com.codepath.chefster.models.Categories;
import com.codepath.chefster.utils.ItemClickSupport;
import com.codepath.chefster.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnMainFragmentInteractionListener mListener;
    private List<Categories> categoriesList;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @BindView(R.id.rvCategoryGrid)
    RecyclerView rvCategories;
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
                Categories categories = categoriesList.get(position);
                Intent intent = new Intent(getActivity(), DishListActivity.class);
                intent.putExtra("category_name", categories.getCategoryName());
                startActivity(intent);
            }
        });
        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());

        rvCategories.setLayoutManager(manager);
        rvCategories.setItemAnimator(new SlideInUpAnimator());
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        rvCategories.addItemDecoration(decoration);
        categoryAdapter = new CategoryAdapter(getActivity(), getCategoryList());
        rvCategories.setAdapter(categoryAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMainFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMainFragmentInteraction(Uri uri);
    }

    //Will give the list of categories.
    //for now its hard coded, later can get from database
    private List<Categories> getCategoryList() {
        try {
            categoriesList = new ArrayList<>();
            categoriesList.add(new Categories("American", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_american.jpg?alt=media&token=7ce009ae-48be-4dad-8bd6-a8d5c27ccfe0"));
            categoriesList.add(new Categories("Israeli", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_israeli.jpg?alt=media&token=366d30d6-49ad-4316-8080-eabb906d475c"));
            categoriesList.add(new Categories("Indian", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_indian.jpg?alt=media&token=fbaa409d-e3ac-44db-aa3b-0f8cd0a5dce4"));
            categoriesList.add(new Categories("Italian", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_italian.jpg?alt=media&token=f1b87847-e52c-4002-9273-e8d7a3e6922b"));
            categoriesList.add(new Categories("Vietnamese", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_vietnamese.jpg?alt=media&token=9f0ae1af-2f3e-4e68-a957-51a5a6b407d1"));
            categoriesList.add(new Categories("Thai", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_thai.jpg?alt=media&token=8d6b62c1-160d-462e-a452-741987043ac4"));
            categoriesList.add(new Categories("Spanish", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_spanish.jpg?alt=media&token=bd0ecd68-a3b5-4b31-a435-c9ed96e88f0f"));
            categoriesList.add(new Categories("French", "https://firebasestorage.googleapis.com/v0/b/chefster-c96f8.appspot.com/o/category_french.jpg?alt=media&token=77f98685-1007-42d4-8b4d-c019317c9b87"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoriesList;
    }
}
