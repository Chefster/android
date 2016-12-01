package com.codepath.chefster.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.chefster.ChefsterApplication;
import com.codepath.chefster.R;
import com.codepath.chefster.models.Ingredient;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShoppingListFragment extends DialogFragment {

    @BindView(R.id.main_container) LinearLayout mainContainer;

    private static final String INGREDIENTS_LIST = "ingredientsList";

    private List<Ingredient> ingredientsList;

    private OnShoppingListInteractionListener listener;
    private Unbinder unbinder;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ingredientsList = new ArrayList<>();
        for (Ingredient ingredient : ChefsterApplication.shoppingList.keySet()) {
            ingredientsList.add(ingredient);
        }
        Collections.sort(ingredientsList);
        String lastCategory = "jibrish";
        for (Ingredient ingredient : ingredientsList) {
            if (ingredient.getCategory() != null) {
                if (!ingredient.getCategory().equals(lastCategory)) {
                    lastCategory = ingredient.getCategory();
                    TextView categoryTextView = new TextView(getActivity());
                    categoryTextView.setText(lastCategory);
                    categoryTextView.setTextSize(30f);
                    categoryTextView.setTextColor(getActivity().getResources().getColor(R.color.orange_dark, null));
                    mainContainer.addView(categoryTextView);
                }
            }
            double amount = ChefsterApplication.shoppingList.get(ingredient);
            TextView ing = new TextView(getActivity());
            ing.setTextSize(20f);
            ing.setText(amount + " " + ingredient.getAmountType() + " " + ingredient.getName() + " = $" + (amount * ingredient.getPrice()));
            mainContainer.addView(ing);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShoppingListInteractionListener) {
            listener = (OnShoppingListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShoppingListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnShoppingListInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
