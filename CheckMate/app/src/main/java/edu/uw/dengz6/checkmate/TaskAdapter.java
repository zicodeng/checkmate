package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An custom array adapter for TaskData Object
 */

public class TaskAdapter extends ArrayAdapter<TaskAdapter.TaskData> {

    public static class TaskData {
        public String title;
        public String details;
        public String dueOn;
        public String createdOn;
        public String assigner;
        public String assignee;

        public TaskData(String title, String details, String dueOn, String createdOn, String assigner, String assignee) {
            this.title = title;
            this.details = details;
            this.dueOn = dueOn;
            this.createdOn = createdOn;
            this.assigner = assigner;
            this.assignee = assignee;
        }
    }

    public static class ViewHolder {
        TextView title;
        TextView dueOn;
        TextView details;
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
            // TODO: complete the rest view binding
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(data.title);
        // TODO: complete the rest view binding
        // Return the completed view to render on screen
        return convertView;
    }
}