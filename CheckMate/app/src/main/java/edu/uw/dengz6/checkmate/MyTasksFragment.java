package edu.uw.dengz6.checkmate;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyTasksFragment extends Fragment {

    public static final String TAG = "All_Tasks_Fragment";
    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;
    protected static ArrayList<TaskData> tasks;
    private TaskAdapter adapter;

    public MyTasksFragment() {
        // Required empty public constructor
    }

    public static MyTasksFragment newInstance() {
        Bundle args = new Bundle();
        MyTasksFragment fragment = new MyTasksFragment();
        tasks = new ArrayList<>();
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

        adapter = new TaskAdapter(getActivity(), tasks);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.all_tasks_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String taskID = tasks.get(position).taskID;
                return false;
            }
        });

        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                        userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    adapter.clear();
                    tasks.clear();
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        //handle each task
                        TaskData task = taskSnapshot.getValue(TaskData.class);
                        // show incomplete tasks, the current user as the assignee
                        if (task.assignee.equals(userInfo.get(SessionManager.KEY_NAME)) && !task.isCompleted) {
                            tasks.add(task);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    ;


    public static class ManageTaskFragment extends DialogFragment {

        private static final String TASK_ID_KEY = "Task_ID_Key";

        public static ManageTaskFragment newInstance(String TaskID) {

            Bundle args = new Bundle();

            args.putString(TASK_ID_KEY, TaskID);

            ManageTaskFragment fragment = new ManageTaskFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final String taskID = getArguments().getString(TASK_ID_KEY);

            final SessionManager sessionManager = new SessionManager(getActivity());
            final String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);
            final String userID = sessionManager.getUserDetails().get(SessionManager.KEY_USER_ID);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Manage Task");
            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.manage_task, (ViewGroup) getView(), false);
            builder.setView(viewInflated);
            // Establish connection and set "tasks" as base URL
            final DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName);

            // Set up the buttons
            builder.setPositiveButton("Mark Completed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final DatabaseReference tasksRef = ref.child("tasks").child(taskID);

                    // Retrieve the tasks and update its "isCompleted" field
                    tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            tasksRef.child("isCompleted").setValue(true);

                            // Add the updated total to "" under that user
                            final DatabaseReference totalTasksRef = ref.child("users").child(userID).child("totalTasks");
                            totalTasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    totalTasksRef.setValue(((Long) dataSnapshot.getValue()).intValue() + 1);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    // Inform the user
                    Toast.makeText(getActivity(), "Task Marked as Completed", Toast.LENGTH_SHORT).show();
                    // Close the dialog
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ref.child("tasks").child(taskID).removeValue();
                    // Inform the user
                    Toast.makeText(getActivity(), "Task Deleted", Toast.LENGTH_SHORT).show();
                    // Close the dialog
                    dialog.dismiss();
                }
            });
            return builder.create();
        }
    }
}
