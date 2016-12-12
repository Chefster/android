package com.codepath.chefster.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MealLaunchSettingsFragment extends DialogFragment {
    private static final String PEOPLE = "people";

    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;

    @BindColor(android.R.color.black) int chosenColor;
    @BindColor(android.R.color.white) int unchosenColor;

    int numberOfPeople = 1;

    private OnSettingsDialogCloseListener numOfPeopleListener;
    private Unbinder unbinder;

    public MealLaunchSettingsFragment() {
        // Required empty public constructor
    }

    public static MealLaunchSettingsFragment newInstance(int numberOfPeople) {
        MealLaunchSettingsFragment fragment = new MealLaunchSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(PEOPLE, numberOfPeople);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberOfPeople = getArguments().getInt(PEOPLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_launch_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (numberOfPeople == 1) {
            onePersonTextView.setBackgroundColor(chosenColor);
            onePersonTextView.setTextColor(unchosenColor);
        } else {
            morePeopleTextView.setBackgroundColor(chosenColor);
            morePeopleTextView.setTextColor(unchosenColor);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_ok)
    public void onSetNumberOfPeople() {
        if (numOfPeopleListener != null) {
            numOfPeopleListener.onNumberOfPeopleSet(numberOfPeople);
            dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsDialogCloseListener) {
            numOfPeopleListener = (OnSettingsDialogCloseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsDialogCloseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        numOfPeopleListener = null;
    }

    @OnClick(R.id.text_view_person)
    public void chooseOnePerson() {
        onePersonTextView.setBackgroundColor(chosenColor);
        onePersonTextView.setTextColor(unchosenColor);
        morePeopleTextView.setBackgroundColor(unchosenColor);
        morePeopleTextView.setTextColor(chosenColor);
        numberOfPeople = 1;
    }

    @OnClick(R.id.text_view_persons)
    public void chooseMorePeople() {
        onePersonTextView.setBackgroundColor(unchosenColor);
        onePersonTextView.setTextColor(chosenColor);
        morePeopleTextView.setBackgroundColor(chosenColor);
        morePeopleTextView.setTextColor(unchosenColor);
        numberOfPeople = 2;
    }

    public interface OnSettingsDialogCloseListener {
        void onNumberOfPeopleSet(int people);
    }
}
