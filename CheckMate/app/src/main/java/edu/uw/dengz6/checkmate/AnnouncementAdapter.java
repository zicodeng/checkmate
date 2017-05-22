package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class AnnouncementAdapter extends ArrayAdapter<AnnouncementAdapter.AnnouncementData> {

    public static class AnnouncementData {
        public String content;
        public String createdBy;
        public Date createdOn;

        public AnnouncementData(String content, Date createdOn, String createdBy) {
            this.content = content;
            this.createdOn = createdOn;
            this.createdBy = createdBy;
        }
    }

    public static class ViewHolder {
        TextView content;
        TextView meta;
        int position;
    }

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementData> data) {
        super(context, 0, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AnnouncementData data = getItem(position);
        ViewHolder holder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_announcement, parent, false);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.announcement_item_content);
            holder.meta = (TextView) convertView.findViewById(R.id.announcement_item_meta);
            // TODO: complete the rest view binding
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(data.content);
        String metaText = data.createdBy + data.createdOn;
        holder.meta.setText(metaText);

        // Return the completed view to render on screen
        return convertView;
    }
}
