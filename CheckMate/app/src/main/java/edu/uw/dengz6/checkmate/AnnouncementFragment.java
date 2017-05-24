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
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {
    public static final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups/";
    protected static SessionManager manager;
    protected static Firebase announcementRef;
    protected static HashMap<String, String> userInfo;
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

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_announcement);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addNewAnnouncementFragment = AddNewAnnouncementFragment.newInstance();
                addNewAnnouncementFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Shopping_List_Fragment");
            }
        });
        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        Firebase.setAndroidContext(getActivity());
        announcementRef = new Firebase(FIREBASE_URL + userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

        announcementRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Log.v(TAG, dataSnapshot.getValue().toString());
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

            // Get current group name
            SessionManager sessionManager = new SessionManager(getActivity());
            String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);
            final String userID = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);

            // Set up base firebase URL
            final String firebaseURL = FIREBASE_URL + groupName + "/announcements";

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
                    dialog.dismiss();
                    String getTitle = title.getText().toString();
                    String getDescription = description.getText().toString();

                    // Push it to Firebase
                    Firebase.setAndroidContext(getActivity());

                    // Establish connection and set current shopping list as base URL
                    announcementRef = new Firebase(firebaseURL);

                    // Create a new shopping list with random ID
                    Firebase newAnnouncement = announcementRef.push();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
                    String currentTime = simpleDateFormat.format(new Date());

                    // Create a new shopping list object
                    AnnouncementData mAnnouncement = new AnnouncementData(getDescription, currentTime, userID);

                    newAnnouncement.setValue(mAnnouncement);
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

