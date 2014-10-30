package com.techilse.mohit.musical.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techilse.mohit.musical.R;

/**
 * Created by Mohit on 25-10-2014.
 */
public class LandscapeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.landscape_my,container,false);
    }
}
