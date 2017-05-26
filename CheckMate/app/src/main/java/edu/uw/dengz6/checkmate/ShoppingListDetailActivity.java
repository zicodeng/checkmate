package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShoppingListDetailActivity extends AppCompatActivity {

    private RecyclerView shoppingItemRecyclerView;

    private String shoppingListID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_detail);

        shoppingListID = getIntent().getStringExtra(ShoppingListAdapter.EXTRA_SHOPPING_LIST_ID);

        shoppingItemRecyclerView = (RecyclerView) findViewById(R.id.shopping_item_recycler_view);

        ArrayList<String> numbers = new ArrayList<String>();
        numbers.add("A");
        numbers.add("A");
        numbers.add("A");
        numbers.add("A");
        numbers.add("A");

        ShoppingItemAdapter shoppingItemAdapter = new ShoppingItemAdapter(numbers, this);

        shoppingItemRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        shoppingItemRecyclerView.setAdapter(shoppingItemAdapter);

        FloatingActionButton fabAddShoppingItem = (FloatingActionButton) findViewById(R.id.fab_add_shopping_item);

        fabAddShoppingItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment addNewShoppingItemFragment = AddNewShoppingItemFragment.newInstance(shoppingListID);
                addNewShoppingItemFragment.show(ShoppingListDetailActivity.this.getSupportFragmentManager(), "Add_New_Shopping_Item");
            }
        });
    }

    public class ShoppingItemAdapter extends RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private ArrayList<String> shoppingItemList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView shoppingItemName;

            public ViewHolder(View itemView) {
                super(itemView);

                // Tell our ViewHolder what kind of views it should contain
                shoppingItemName = (TextView) itemView.findViewById(R.id.shopping_item_name);
            }
        }

        public ShoppingItemAdapter(ArrayList<String> shoppingItemList, Context context) {
            this.shoppingItemList = shoppingItemList;
            this.inflater = LayoutInflater.from(context);
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // P1: which layout template file we want each item to inflate
            View view = inflater.inflate(R.layout.list_item_shopping_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Each item on the list is corresponding to an unique id
            // We can access to each item by using its id
            String itemName = shoppingItemList.get(position);

            // Bind data in each item with our ViewHolder
            viewHolder.shoppingItemName.setText(itemName);
        }

        @Override
        public int getItemCount() {
            // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
            return shoppingItemList.size();
        }
    }

    public static class AddNewShoppingItemFragment extends DialogFragment {

        private static final String SHOPPING_LIST_ID_KEY = "Shopping_List_ID_Key";

        public static AddNewShoppingItemFragment newInstance(String shoppingListID) {

            Bundle args = new Bundle();

            args.putString(SHOPPING_LIST_ID_KEY, shoppingListID);

            AddNewShoppingItemFragment fragment = new AddNewShoppingItemFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final String shoppingListID = getArguments().getString(SHOPPING_LIST_ID_KEY);

            final SessionManager sessionManager = new SessionManager(getActivity());
            final String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Shopping Item");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_new_shopping_item, (ViewGroup) getView(), false);

            // Set up the input
            final EditText input = (EditText) viewInflated.findViewById(R.id.input_shopping_item_name);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    // Get the user input
                    String shoppingItemName = input.getText().toString();

                    // Establish connection and set "itemList" as base URL
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName
                                    + "/shoppingLists/" + shoppingListID + "/itemList");

                    // Create a new shopping list with an unique ID
                    DatabaseReference newShoppingItem = ref.push();

                    newShoppingItem.setValue(shoppingItemName);

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
