package edu.uw.dengz6.checkmate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by iguest on 5/21/17.
 */

public class ShoppingHistoryFragment extends Fragment {


    public ShoppingHistoryFragment() {
        // Required empty public constructor
    }

    public static ShoppingHistoryFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingHistoryFragment fragment = new ShoppingHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shoppinghistory, container, false);
    }
}
