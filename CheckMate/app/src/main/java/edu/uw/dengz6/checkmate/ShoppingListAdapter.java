package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A custom array adapter for ShoppingHist Object
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    public final static String EXTRA_SHOPPING_LIST_ID = "edu.uw.dengz6.checkmate.Extra_Shopping_List_ID";

    private LayoutInflater inflater;
    private ArrayList<ShoppingListData> shoppingLists;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shoppingListName;
        private TextView shoppingListCreatedDate;
        private TextView shoppingListOwner;

        public ViewHolder(View itemView) {
            super(itemView);

            context = itemView.getContext();

            // Tell our ViewHolder what kind of views it should contain
            shoppingListName = (TextView) itemView.findViewById(R.id.shopping_list_name);
            shoppingListCreatedDate = (TextView) itemView.findViewById(R.id.shopping_list_created_date);
            shoppingListOwner = (TextView) itemView.findViewById(R.id.shopping_list_owner);
        }
    }

    public ShoppingListAdapter(ArrayList<ShoppingListData> shoppingLists, Context context) {
        this.shoppingLists = shoppingLists;
        this.inflater = LayoutInflater.from(context);
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // P1: which layout template file we want each item to inflate
        View view = inflater.inflate(R.layout.list_item_shopping_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Each item on the list is corresponding to an unique id
        // We can access to each item by using its id
        final ShoppingListData item = shoppingLists.get(position);

        // Bind data in each item with our ViewHolder
        String shoppingListName = item.shoppingListName;
        final String shoppingListID = item.shoppingListID;

        viewHolder.shoppingListName.setText(shoppingListName);
        viewHolder.shoppingListCreatedDate.setText(item.createdOn);
        viewHolder.shoppingListOwner.setText(item.ownerName);

        // Click a shopping list to see detail
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start shopping list detail activity
                Intent shoppingListDetailIntent = new Intent(context, ShoppingListDetailActivity.class);

                // Include shopping list ID and send it to "Shopping List Detail Activity"
                shoppingListDetailIntent.putExtra(EXTRA_SHOPPING_LIST_ID, shoppingListID);

                context.startActivity(shoppingListDetailIntent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Create a dialog and ask the user to
                // either delete the list or add the list to history
                DialogFragment addNewShoppingListFragment = ManageShoppingListFragment.newInstance(shoppingListID);
                addNewShoppingListFragment.show(((FragmentActivity)context).getSupportFragmentManager(), "Manage_Shopping_List");

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
        return shoppingLists.size();
    }

    public static class ManageShoppingListFragment extends DialogFragment {

        private static final String SHOPPING_LIST_ID_KEY = "Shopping_List_ID_Key";

        public static ManageShoppingListFragment newInstance(String shoppingListID) {

            Bundle args = new Bundle();

            args.putString(SHOPPING_LIST_ID_KEY, shoppingListID);

            ManageShoppingListFragment fragment = new ManageShoppingListFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String shoppingListID = getArguments().getString(SHOPPING_LIST_ID_KEY);

            final SessionManager sessionManager = new SessionManager(getActivity());
            final String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);
            final String userID = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Manage Shopping List");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.manage_shopping_list, (ViewGroup) getView(), false);

            // Grab views in the dialog
            final EditText input = (EditText) viewInflated.findViewById(R.id.shopping_list_total_cost);

            builder.setView(viewInflated);

            // Establish connection and set "groups" as base URL
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName);

            // Set up the buttons
            builder.setPositiveButton("ADD TO HISTORY", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String inputValue = String.valueOf(input.getText());

                    // Validate input
                    if(inputValue.length() == 0) {
                        Toast.makeText(getActivity(), "Total cost must be non-empty", Toast.LENGTH_SHORT).show();
                    } else {
                        // Get the user input
                        final int totalCost = Integer.parseInt(inputValue);

                        final DatabaseReference shoppingListRef = ref.child("shoppingLists").child(shoppingListID);

                        // Retrieve the shopping list and update its "Total Cost" field
                        shoppingListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ShoppingListData mShoppingListData = dataSnapshot.getValue(ShoppingListData.class);

                                // Update "Total Cost"
                                mShoppingListData.totalCost = totalCost;

                                // Add the updated list to "Shopping Lists History" under that user
                                ref.child("users").child(userID).child("shoppingHistoryList").push().setValue(mShoppingListData);

                                // Delete the shopping list in "Shopping Lists"
                                shoppingListRef.removeValue();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        // Inform the user
                        Toast.makeText(getActivity(), "Total Cost: " + totalCost, Toast.LENGTH_SHORT).show();
                    }

                    // Close the dialog
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ref.child("shoppingLists").child(shoppingListID).removeValue();

                    // Inform the user
                    Toast.makeText(getActivity(), "Shopping List deleted", Toast.LENGTH_SHORT).show();

                    // Close the dialog
                    dialog.dismiss();
                }
            });

            return builder.create();
        }
    }
}
