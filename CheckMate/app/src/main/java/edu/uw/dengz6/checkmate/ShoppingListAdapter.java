package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        // Long press an item will replace left panel with FullArticleDialogFragment
//        viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                callback.onArticleItemLongPressed(item.getWebUrl());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
        return shoppingLists.size();
    }

    public void updateList(ArrayList<ShoppingListData> newShoppingLists) {
        if(newShoppingLists != null) {
            shoppingLists.clear();
            shoppingLists.addAll(newShoppingLists);
        }
        notifyDataSetChanged();
    }
}
