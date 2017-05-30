package edu.uw.dengz6.checkmate;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

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
                        if (task.assignee.equals(userInfo.get(SessionManager.KEY_NAME))) {
                            //converting the due date to long and set the alarm 10 minutes before due time
                            SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                            long timeDue = 0;
                            try {
                                timeDue = dt.parse(task.dueOn).getTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long currentTime = System.currentTimeMillis();
                            if((currentTime - timeDue) >= 1000 * 60 * 10){
                                timeDue = timeDue - 1000 * 60 * 10;
                            }
                            if(currentTime > timeDue){
                                Log.v(TAG, "passed due time");
                            }else {
                                alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                alarmIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                                // setRepeating() lets you specify a precise custom interval--in this case,
                                // 5 minutes.
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeDue, alarmIntent);
                            }
                            tasks.add(task);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    };
}
