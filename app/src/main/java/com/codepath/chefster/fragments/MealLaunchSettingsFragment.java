package com.codepath.chefster.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MealLaunchSettingsFragment extends DialogFragment {

    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;

    private OnSettingsDialogCloseListener mListener;
    private Unbinder unbinder;

    public MealLaunchSettingsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_meal_launch_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsDialogCloseListener) {
            mListener = (OnSettingsDialogCloseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsDialogCloseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSettingsDialogCloseListener {
        void onFragmentInteraction(Uri uri);
    }
}
