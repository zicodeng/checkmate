package edu.uw.dengz6.checkmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v4.app.Fragment;

/**
 * Created by iguest on 5/21/17.
 */

public class ShoppingListFragment extends Fragment{


    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingListFragment fragment = new ShoppingListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_shopping);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New ShoppingList", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
