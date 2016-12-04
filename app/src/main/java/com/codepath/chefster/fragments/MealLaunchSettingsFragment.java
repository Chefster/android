package com.codepath.chefster.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.chefster.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MealLaunchSettingsFragment extends DialogFragment {
    private static final String PANS = "pans";
    private static final String POTS = "pots";
    private static final String PEOPLE = "people";

    @BindView(R.id.pans_spinner) Spinner pansSpinner;
    @BindView(R.id.pots_spinner) Spinner potsSpinner;
    @BindView(R.id.text_view_person) TextView onePersonTextView;
    @BindView(R.id.text_view_persons) TextView morePeopleTextView;

    @BindColor(android.R.color.black) int chosenColor;
    @BindColor(android.R.color.white) int unchosenColor;

    int numberOfPeople = 1;
    int numberOfPans = 1;
    int numberOfPots = 1;

    private OnSettingsDialogCloseListener mListener;
    private Unbinder unbinder;

    public MealLaunchSettingsFragment() {
        // Required empty public constructor
    }

    public static MealLaunchSettingsFragment newInstance(int numberOfPans, int numberOfPots, int numberOfPeople) {
        MealLaunchSettingsFragment fragment = new MealLaunchSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(PANS, numberOfPans);
        args.putInt(POTS, numberOfPots);
        args.putInt(PEOPLE, numberOfPeople);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberOfPans = getArguments().getInt(PANS);
            numberOfPots = getArguments().getInt(POTS);
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

        setupSpinners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_ok)
    public void onSetTools() {
        if (mListener != null) {
            mListener.onToolsSet(numberOfPans, numberOfPots, numberOfPeople);
            dismiss();
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

    private void setupSpinners() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.amount_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        pansSpinner.setAdapter(adapter);
        pansSpinner.setSelection(numberOfPans - 1);
        pansSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPans = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        potsSpinner.setAdapter(adapter);
        potsSpinner.setSelection(numberOfPots - 1);
        potsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberOfPots = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        void onToolsSet(int pans, int pots, int people);
    }
}
