package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A custom array adapter for TaskData Object
 */

public class TaskAdapter extends ArrayAdapter<TaskData> {


    public static class ViewHolder {
        TextView title;
        TextView dueOn;
        TextView detail;
        TextView assignee;
        int position;
    }

    public TaskAdapter(Context context, ArrayList<TaskData> data) {
        super(context, 0, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TaskData data = getItem(position);
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_task, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.task_item_title);
            holder.detail = (TextView) convertView.findViewById(R.id.task_item_detail);
            holder.dueOn = (TextView) convertView.findViewById(R.id.task_item_dueOn);
            holder.assignee = (TextView) convertView.findViewById(R.id.task_item_assignee);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.title);
        holder.detail.setText(data.detail);
        holder.dueOn.setText("Due On" + data.dueOn);
        holder.assignee.setText("Assignee: " + data.assignee);
        // Return the completed view to render on screen
        return convertView;
    }
}