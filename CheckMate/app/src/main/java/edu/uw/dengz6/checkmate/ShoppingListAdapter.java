package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A custom array adapter for ShoppingHist Object
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>
        implements View.OnCreateContextMenuListener {

    public final static String EXTRA_SHOPPING_LIST_ID = "edu.uw.dengz6.checkmate.Extra_Shopping_List_ID";

    private LayoutInflater inflater;
    private ArrayList<ShoppingListData> shoppingLists;
    private Context context;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

    }

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

                

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
        return shoppingLists.size();
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
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
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
