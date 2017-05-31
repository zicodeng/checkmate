package edu.uw.dengz6.checkmate;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {

    public static final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups/";

    public static final String TAG = "Announcement_Fragment";

    private ArrayList<AnnouncementData> announcements;
    private AnnouncementAdapter adapter;
    private RecyclerView announcementRecyclerView;
    private String groupName;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public static AnnouncementFragment newInstance() {
        Bundle args = new Bundle();
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);

        announcements = new ArrayList<>();

        announcementRecyclerView = (RecyclerView) rootView.findViewById(R.id.announcementRecyclerView);

        announcementRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AnnouncementAdapter(getActivity(), announcements);
        announcementRecyclerView.setAdapter(adapter);

        final SessionManager manager = new SessionManager(getActivity());

        groupName = manager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                        groupName + "/announcements");

        // Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                announcements.clear();

                for (DataSnapshot announcementSnapshot: dataSnapshot.getChildren()) {
                    //handle each task
                    AnnouncementData announcement = announcementSnapshot.getValue(AnnouncementData.class);
                    announcements.add(announcement);
                }

                progressDialog.dismiss();

                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_announcement);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addNewAnnouncementFragment = AddNewAnnouncementFragment.newInstance();
                addNewAnnouncementFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Shopping_List_Fragment");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public static class AddNewAnnouncementFragment extends DialogFragment {

        public static AddNewAnnouncementFragment newInstance() {

            Bundle args = new Bundle();

            AddNewAnnouncementFragment fragment = new AddNewAnnouncementFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final SessionManager sessionManager = new SessionManager(getActivity());

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Announcement");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_new_announcement, (ViewGroup) getView(), false);

            // Set up the input
            final EditText title = (EditText) viewInflated.findViewById(R.id.textTitle);
            final EditText textContent = (EditText) viewInflated.findViewById(R.id.text_content);
            final String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                    groupName + "/announcements");

                    DatabaseReference mAnnouncement = ref.push();

                    dialog.dismiss();

                    String getTitle = title.getText().toString();
                    String content = textContent.getText().toString();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                    String currentTime = simpleDateFormat.format(new Date());
                    String assigner = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);
                    String announcementID = mAnnouncement.getKey();
                    mAnnouncement.setValue(new AnnouncementData(announcementID, getTitle, content, currentTime, assigner));

                    Toast.makeText(getActivity(), "New Announcement added", Toast.LENGTH_SHORT).show();

                    sendAnnouncementNotificationToGroup(groupName, getTitle);
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

    public static void sendAnnouncementNotificationToGroup(String groupName, String message) {

        // Create a shoppingNotification field
        // Our Node.js server will take this field as entry to send notification to users belong to this group
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/notificationRequests");

        Map notification = new HashMap<>();
        notification.put("groupName", groupName);
        notification.put("message", message);

        // Tell server this is a announcement notification
        notification.put("category", "Announcement");

        ref.push().setValue(notification);
    }
}

