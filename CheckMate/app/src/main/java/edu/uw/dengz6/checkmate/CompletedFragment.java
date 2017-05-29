package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class CompletedFragment extends Fragment {

    public static final String TAG = "Tasks_Completed";

    private ArrayList<TaskCompletedData> completedTasksList;
    private TaskCompletedAdapter adapter;
    private RecyclerView completedTasksRecyclerView;

    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;

    private String groupName;

    public CompletedFragment() {
        // Required empty public constructor
    }

    public static CompletedFragment newInstance() {

        Bundle args = new Bundle();

        CompletedFragment fragment = new CompletedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_shopping_history, container, false);

        // Initialize "Shopping History List"
        completedTasksList = new ArrayList<TaskCompletedData>();

        // Construct adapter
        adapter = new TaskCompletedAdapter(completedTasksList, getActivity());

        // Get reference to RecyclerView
        completedTasksRecyclerView = (RecyclerView) rootView.findViewById(R.id.task_completed_recycler_view);

        // Attach RecyclerView with adapter
        completedTasksRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        completedTasksRecyclerView.setAdapter(adapter);

        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        // Get current group name
        final SessionManager sessionManager = new SessionManager(getActivity());
        groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

        // Set up Firebase connection
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName + "/users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                completedTasksList.clear();

                // Loop through each user
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {

                    String userID = userSnapShot.getKey();

                    User mUser = userSnapShot.getValue(User.class);

                    final String userName = mUser.name;
                    final String createdOn = mUser.createdOn;

                    // Set up Firebase connection
                    DatabaseReference totalTasksref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                    userInfo.get(SessionManager.KEY_GROUP_NAME) + "/users/" + userID + "/totalTasks");

                    totalTasksref.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int totalTasks = (Integer) dataSnapshot.getValue();

                            TaskCompletedData mTaskCompletedData =
                                    new TaskCompletedData(userName, totalTasks, createdOn);

                            completedTasksList.add(mTaskCompletedData);

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public class TaskCompletedAdapter extends RecyclerView.Adapter<TaskCompletedAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private ArrayList<TaskCompletedData> TaskCompletedList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtUserName;
            private TextView txtTotalCompleted;
            private TextView txtSince;
            private TextView txtTotalCompleletedFocus;

            public ViewHolder(View itemView) {
                super(itemView);

                // Tell our ViewHolder what kind of views it should contain
                txtUserName = (TextView) itemView.findViewById(R.id.task_completed_username);
                txtTotalCompleted = (TextView) itemView.findViewById(R.id.task_completed_total_tasks);
                txtSince = (TextView) itemView.findViewById(R.id.task_completed_since);
                txtTotalCompleletedFocus = (TextView) itemView.findViewById(R.id.task_completed_total_tasks_focus);
            }
        }

        public TaskCompletedAdapter(ArrayList<TaskCompletedData> TaskCompletedList, Context context) {
            this.TaskCompletedList = TaskCompletedList;
            this.inflater = LayoutInflater.from(context);
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // P1: which layout template file we want each item to inflate
            View view = inflater.inflate(R.layout.list_item_shopping_history, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Each item on the list is corresponding to an unique id
            // We can access to each item by using its id
            final TaskCompletedData item = TaskCompletedList.get(position);

            // Bind data in each item with our ViewHolder
            String userName = item.userName;
            int totalCompleted = item.totalCompletedTasks;
            String since = "Since: " + item.since;

            viewHolder.txtUserName.setText(userName);
            viewHolder.txtTotalCompleted.setText("Total Completed Tasks: " + totalCompleted);
            viewHolder.txtSince.setText(since);
            viewHolder.txtTotalCompleletedFocus.setText(totalCompleted);
        }

        @Override
        public int getItemCount() {
            // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
            return TaskCompletedList.size();
        }
    }
}
