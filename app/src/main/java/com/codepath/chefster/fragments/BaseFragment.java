package com.codepath.chefster.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

// The reason I added this, is so later it will be easier for us to add logic to all fragments together
// and avoid code duplication
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // if there are arguments, get them with getArguments()
        }
    }


}
