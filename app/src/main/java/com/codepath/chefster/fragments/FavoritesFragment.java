package com.codepath.chefster.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.chefster.R;
import com.codepath.chefster.Recipes;
import com.codepath.chefster.adapters.FavoritesListAdapter;
import com.codepath.chefster.models.Dish;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends BaseFragment {

    private List<Dish> dishList;

    private FavoritesListAdapter adapter;
    @BindView(R.id.rvFavorites) RecyclerView rvFavorites;

    private OnFavoritesInteractionListener mListener;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        getFavoriteRecipesList();
        initializeRecyclerView();
        return view;
    }

    private void initializeRecyclerView(){
        LinearLayoutManager manager  = new LinearLayoutManager(getActivity());

        rvFavorites.setLayoutManager(manager);
        adapter = new FavoritesListAdapter(getActivity(), dishList);
        rvFavorites.setAdapter(adapter);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFavoritesFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoritesInteractionListener) {
            mListener = (OnFavoritesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFavoritesInteractionListener {
        // TODO: Update argument type and name
        void onFavoritesFragmentInteraction(Uri uri);
    }

    //This method will take the lst from database and return favorite list
    private void getFavoriteRecipesList() {
        try {
            InputStream inputStream = getActivity().getAssets().open("recipes.json");
            dishList = Recipes.fromInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
