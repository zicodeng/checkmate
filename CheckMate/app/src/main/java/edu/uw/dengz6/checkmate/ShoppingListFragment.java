package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by iguest on 5/21/17.
 */

public class ShoppingListFragment extends Fragment {

    public static final String TAG = "Shopping_List_Fragment";

    private ArrayList<ShoppingListData> shoppingLists;
    private ShoppingListAdapter shoppingListAdapter;
    private RecyclerView shoppingListRecyclerView;

    private String groupName;

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
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        // Initialize ArrayList
        shoppingLists = new ArrayList<ShoppingListData>();

        // Construct adapter
        shoppingListAdapter = new ShoppingListAdapter(shoppingLists, getActivity());

        // Get reference to RecyclerView
        shoppingListRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_list_recycler_view);

        // Attach RecyclerView with adapter
        shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shoppingListRecyclerView.setAdapter(shoppingListAdapter);

        // Get current group name
        final SessionManager sessionManager = new SessionManager(getActivity());
        groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

        // Set up Firebase connection
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName + "/shoppingLists");

        // Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        // Render shopping list on screen
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppingLists.clear();

                for (DataSnapshot shoppingListSnapShot : dataSnapshot.getChildren()) {
                    ShoppingListData mShoppingListData = shoppingListSnapShot.getValue(ShoppingListData.class);
                    shoppingLists.add(mShoppingListData);
                }

                progressDialog.dismiss();

                // Update adapter
                shoppingListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fabAddShoppingList = (FloatingActionButton) rootView.findViewById(R.id.fab_shopping);

        fabAddShoppingList.setOnClickListener(new View.OnClickListener() {
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
            final SessionManager sessionManager = new SessionManager(getActivity());
            final String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

                    // Establish connection and set "shoppingLists" as base URL
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName + "/shoppingLists");

                    // Create a new shopping list with an unique ID
                    DatabaseReference newShoppingList = ref.push();

                    // Get ID of this shopping list
                    String shoppingListID = newShoppingList.getKey();

                    // Get current date
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                    String currentDate = simpleDateFormat.format(new Date());

                    String ownerID = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);
                    String ownerName = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);

                    // Create a new shopping list object
                    ShoppingListData mShoppingList = new ShoppingListData(shoppingListID, shoppingListName, ownerID, ownerName, 0, 0, currentDate);

                    newShoppingList.setValue(mShoppingList);

                    // Inform the user
                    Toast.makeText(getActivity(), "New shopping list added", Toast.LENGTH_SHORT).show();
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
