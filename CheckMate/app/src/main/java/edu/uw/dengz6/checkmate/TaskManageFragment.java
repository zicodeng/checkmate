package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Leon on 5/30/17.
 */
public class TaskManageFragment extends DialogFragment {

    private static final String TASK_ID_KEY = "Task_ID_Key";

    public static TaskManageFragment newInstance(String TaskID) {

        Bundle args = new Bundle();

        args.putString(TASK_ID_KEY, TaskID);

        TaskManageFragment fragment = new TaskManageFragment();
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

        final DatabaseReference tasksRef = ref.child("tasks").child(taskID);
        // Set up the buttons
        builder.setPositiveButton("Mark Completed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve the tasks and update its "isCompleted" field
                tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tasksRef.child("isCompleted").setValue(true);
                        TaskData taskData = dataSnapshot.getValue(TaskData.class);

                        // Add the updated total to "tasksCompleted" under that user
                        final DatabaseReference totalTasksRef = ref.child("users").child(taskData.assigneeID).child("tasksCompleted");
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
                tasksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TaskData taskData = dataSnapshot.getValue(TaskData.class);
                        // un-assign the task from the user data
                        Log.v("!!!!!", taskData.assigneeID);
                        final DatabaseReference totalTasksAssignedRef = ref.child("users").child(taskData.assigneeID).child("tasksAssigned");
                        totalTasksAssignedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totalTasksAssignedRef.setValue(((Long) dataSnapshot.getValue()).intValue() - 1);
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
                // delete the task
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