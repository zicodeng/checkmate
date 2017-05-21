package edu.uw.dengz6.checkmate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedFragment extends Fragment {


    public CompletedFragment() {
        // Required empty public constructor
    }

    public static CompletedFragment newInstance() {

        Bundle args = new Bundle();

        CompletedFragment fragment = new CompletedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed, container, false);
    }

}
