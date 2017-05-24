package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.client.Firebase;

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

                // Create a dialog and ask the user for input
                DialogFragment addNewShoppingListFragment = AddNewShoppingListFragment.newInstance();
                addNewShoppingListFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Shopping_List_Fragment");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public static class AddNewShoppingListFragment extends DialogFragment {

        public static AddNewShoppingListFragment newInstance() {
            
            Bundle args = new Bundle();
            
            AddNewShoppingListFragment fragment = new AddNewShoppingListFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Get current group name
            SessionManager sessionManager = new SessionManager(getActivity());
            String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);
            final String userID = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);

            // Set up base firebase URL
            final String firebaseURL = "https://checkmate-d2c41.firebaseio.com/groups/" + groupName + "/shopping_lists";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Shopping List");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_new_shopping_list, (ViewGroup) getView(), false);

            // Set up the input
            final EditText input = (EditText) viewInflated.findViewById(R.id.shopping_list_name);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // Get the user input
                    String shoppingListName = input.getText().toString();

                    // Push it to Firebase
                    Firebase.setAndroidContext(getActivity());

                    // Establish connection and set current shopping list as base URL
                    Firebase shoppingListsRef = new Firebase(firebaseURL);

                    // Create a new shopping list with random ID
                    Firebase newShoppingList = shoppingListsRef.push();

                    // Create a new shopping list object
                    ShoppingListData mShoppingList = new ShoppingListData(userID, 0, 0, "5/14/2");

                    newShoppingList.setValue(mShoppingList);

                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            return builder.create();
        }
    }
}
