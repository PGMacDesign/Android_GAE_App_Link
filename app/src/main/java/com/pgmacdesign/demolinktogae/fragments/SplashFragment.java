package com.pgmacdesign.demolinktogae.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pgmacdesign.demolinktogae.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashFragment extends Fragment {

    public SplashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }
}
