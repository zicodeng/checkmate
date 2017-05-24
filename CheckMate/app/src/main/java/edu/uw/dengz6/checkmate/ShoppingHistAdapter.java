package edu.uw.dengz6.checkmate;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A custom array adapter for ShoppingHist Object
 */

public class ShoppingHistAdapter extends ArrayAdapter<ShoppingListData> {


    public static class ViewHolder {
        TextView title;
        TextView detail;
        TextView highlight;
        int position;
    }

    public ShoppingHistAdapter(Context context, ArrayList<ShoppingListData> data) {
        super(context, 0, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ShoppingListData data = getItem(position);
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_announcement, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.shoppinghist_item_title);
            holder.detail = (TextView) convertView.findViewById(R.id.shoppinghist_item_detail);
            holder.highlight = (TextView) convertView.findViewById(R.id.shoppinghist_item_highlight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.owner);
        String detailText = "Total Cost: " + data.totalCost + "/n" + "Total Shopping Lists: " + data.totalCost
                + "/n" + "Since: " + data.since + "/n";
        holder.detail.setText(detailText);
        holder.highlight.setText(data.totalCost);

        // Return the completed view to render on screen
        return convertView;
    }
}
