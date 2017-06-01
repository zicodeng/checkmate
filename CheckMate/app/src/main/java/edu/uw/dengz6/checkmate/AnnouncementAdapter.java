package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A custom array adapter for AnnouncementData Object
 */
public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AnnouncementData> announcementLists;
    private Context context;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView content;
        private TextView description;
        private TextView assigner;
        private TextView createdOn;

        public ViewHolder(View itemView){
            super(itemView);
            context = itemView.getContext();
            content = (TextView) itemView.findViewById(R.id.announcement_item_content);
            description = (TextView) itemView.findViewById(R.id.announcement_item_description);
            assigner = (TextView) itemView.findViewById(R.id.announcement_created_by);
            createdOn = (TextView) itemView.findViewById(R.id.announcement_created_on);
        }
    }

    public AnnouncementAdapter(Context context, ArrayList<AnnouncementData> data) {
        this.announcementLists = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AnnouncementData data = announcementLists.get(position);
        final String announcementID = data.announcementID;
        viewHolder.content.setText(data.content);
        viewHolder.description.setText(data.description);
        viewHolder.assigner.setText(data.createdBy);
        viewHolder.createdOn.setText(data.createdOn);
        viewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                final SessionManager sessionManager = new SessionManager(context);
                String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                groupName + "/announcements/");
                ref.child(announcementID).removeValue();
                Toast.makeText(context, "Announcement removed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return announcementLists.size();
    }

    public void updateList(ArrayList<AnnouncementData> newAnnouncementLists) {
        if(newAnnouncementLists != null) {
            announcementLists.clear();
            announcementLists.addAll(announcementLists);
        }
        notifyDataSetChanged();
    }
}
