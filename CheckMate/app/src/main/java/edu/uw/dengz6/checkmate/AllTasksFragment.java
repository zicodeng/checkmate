package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksFragment extends Fragment {

    public static final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups/";

    public static final String TAG = "All_Tasks_Fragment";
    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;
    protected static Firebase taskRef;

    public AllTasksFragment() {
        // Required empty public constructor
    }

    public static AllTasksFragment newInstance() {
        Bundle args = new Bundle();
        AllTasksFragment fragment = new AllTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_all_tasks);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New Task", Toast.LENGTH_SHORT).show();
            }
        });

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New Task", Toast.LENGTH_SHORT).show();

                // Create a dialog and ask the user for input
                DialogFragment AddNewTaskFragment = AllTasksFragment.AddNewTaskFragment.newInstance();
                AddNewTaskFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Task");
            }
        });
        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        Firebase.setAndroidContext(getActivity());
        taskRef = new Firebase(FIREBASE_URL + userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

        taskRef.addValueEventListener(new ValueEventListener() {
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
    };


    public static class AddNewTaskFragment extends DialogFragment {

        public static AddNewTaskFragment newInstance() {

            Bundle args = new Bundle();

            AddNewTaskFragment fragment = new AddNewTaskFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Task");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_new_task, (ViewGroup) getView(), false);

            // Set up the input
            final EditText taskTitle = (EditText) viewInflated.findViewById(R.id.task_title);
            final EditText taskDetail = (EditText) viewInflated.findViewById(R.id.task_detail);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Firebase taskRef = AllTasksFragment.taskRef;

                    Firebase mTask = taskRef.push();
                    // TODO: roommate dropdown and date picker
                    dialog.dismiss();
                    String title = taskTitle.getText().toString();
                    String detail = taskDetail.getText().toString();
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String createdOn = dt.format(new Date());
                    String assigner = AllTasksFragment.userInfo.get(SessionManager.KEY_USER_ID);
                    mTask.setValue(new TaskData(title, detail, createdOn, createdOn, assigner, assigner));
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
