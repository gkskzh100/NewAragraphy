package jm.dodam.newaragraphy.controller.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jm.dodam.newaragraphy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeColorFragment extends Fragment {


    public ChangeColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_color, container, false);
        init(view);
        return view;
    }

    private void init(View view) {

    }
}
