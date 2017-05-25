package edu.uw.dengz6.checkmate;

import android.content.Context;
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

    private LayoutInflater inflater;
    private ArrayList<ShoppingListData> shoppingLists;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView shoppingListName;
        private TextView shoppingListCreatedDate;
        private TextView shoppingListOwner;

        public ViewHolder(View itemView) {
            super(itemView);

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
        View view = inflater.inflate(R.layout.shopping_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Each item on the list is corresponding to an unique id
        // We can access to each item by using its id
        final ShoppingListData item = shoppingLists.get(position);

        // Bind data in each item with our ViewHolder
        viewHolder.shoppingListName.setText(item.ownerName);
        viewHolder.shoppingListCreatedDate.setText(item.createdOn);
        viewHolder.shoppingListOwner.setText(item.shoppingListName);

        // Click an item in ArticleList will replace right panel with PreviewFragment
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callback.onArticleItemClicked(item);
//            }
//        });

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

    public void updateList(ArrayList<ShoppingListData> newShoppingList) {
        if(newShoppingList != null) {
            shoppingLists.clear();
            shoppingLists.addAll(newShoppingList);
        }
        notifyDataSetChanged();
    }
}
