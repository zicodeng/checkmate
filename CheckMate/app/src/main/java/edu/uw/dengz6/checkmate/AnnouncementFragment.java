package edu.uw.dengz6.checkmate;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {

    public static final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups/";

    public static final String TAG = "Announcement_Fragment";
    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;
    protected static Firebase announcementRef;
    protected static ArrayList<AnnouncementData> announcements;
    protected AnnouncementAdapter adapter;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public static AnnouncementFragment newInstance() {
        Bundle args = new Bundle();
        announcements = new ArrayList<>();
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_announcement);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addNewAnnouncementFragment = AddNewAnnouncementFragment.newInstance();
                addNewAnnouncementFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Shopping_List_Fragment");
            }
        });
        adapter = new AnnouncementAdapter(getActivity(), announcements);
        ListView announcementListView = (ListView) rootView.findViewById(R.id.announcementListView);
        announcementListView.setAdapter(adapter);

        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        Firebase.setAndroidContext(getActivity());
        announcementRef = new Firebase(FIREBASE_URL + userInfo.get(SessionManager.KEY_GROUP_NAME) + "/announcements");
        announcementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    adapter.clear();
                    announcements.clear();
                    for (DataSnapshot announcementSnapshot: dataSnapshot.getChildren()) {
                        //handle each task
                        AnnouncementData announcement = announcementSnapshot.getValue(AnnouncementData.class);
                        announcements.add(announcement);
                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Announcement");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_new_announcement, (ViewGroup) getView(), false);

            // Set up the input
            final EditText title = (EditText) viewInflated.findViewById(R.id.textTitle);
            final EditText description = (EditText) viewInflated.findViewById(R.id.textDescription);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Firebase announcementsRef = AnnouncementFragment.announcementRef;
                    Firebase mAnnouncement = announcementsRef.push();

                    dialog.dismiss();
                    String getTitle = title.getText().toString();
                    String getDescription = description.getText().toString();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
                    String currentTime = simpleDateFormat.format(new Date());
                    String assigner = AnnouncementFragment.userInfo.get(SessionManager.KEY_USER_ID);
                    mAnnouncement.setValue(new AnnouncementData(getDescription, currentTime, assigner));
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

