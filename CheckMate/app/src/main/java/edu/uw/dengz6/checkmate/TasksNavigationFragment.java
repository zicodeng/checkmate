package edu.uw.dengz6.checkmate;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TasksNavigationFragment extends Fragment {

    public static final String TAG = "Tasks_Navigation_Fragment";

    private TextView navItemAllTasks;
    private TextView navItemMyTasks;
    private TextView navItemCompleted;

    // Fragment
    private FragmentManager fm;
    private FragmentTransaction ft;

    private AllTasksFragment allTasksFragment;
    private MyTasksFragment myTasksFragment;
    private CompletedFragment completedFragment;

    public TasksNavigationFragment() {
        // Required empty public constructor
    }

    public static TasksNavigationFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TasksNavigationFragment fragment = new TasksNavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tasks_navigation, container, false);

        navItemAllTasks = (TextView) rootView.findViewById(R.id.nav_item_all_tasks);
        navItemMyTasks = (TextView) rootView.findViewById(R.id.nav_item_my_tasks);
        navItemCompleted = (TextView) rootView.findViewById(R.id.nav_item_completed);

        // Initialize FragmentManager
        fm = getFragmentManager();

        // Initialize Fragments
        allTasksFragment = AllTasksFragment.newInstance();
        myTasksFragment = MyTasksFragment.newInstance();
        completedFragment = CompletedFragment.newInstance();

        // Set "All Tasks" as default navigation item with primary color
        navItemAllTasks.setTextColor(Color.parseColor("#4B2E83"));

        // Set "All Tasks Fragment" as default fragment in the container
        ft = fm.beginTransaction();
        ft.replace(R.id.container, allTasksFragment, "All_Tasks_Fragment");
        ft.commit();

        navItemAllTasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemAllTasks.setTextColor(Color.parseColor("#4B2E83"));

                // Replace the container with "All Tasks Fragment"
                ft = fm.beginTransaction();
                ft.replace(R.id.container, allTasksFragment, "All_Tasks_Fragment");
                ft.commit();
            }
        });

        navItemMyTasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemMyTasks.setTextColor(Color.parseColor("#4B2E83"));

                // Replace the container with "My Tasks Fragment"
                ft = fm.beginTransaction();
                ft.replace(R.id.container, myTasksFragment, "My_Tasks_Fragment");
                ft.commit();
            }
        });

        navItemCompleted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemCompleted.setTextColor(Color.parseColor("#4B2E83"));

                // Replace the container with "Completed Fragment"
                ft = fm.beginTransaction();
                ft.replace(R.id.container, completedFragment, "Completed_Fragment");
                ft.commit();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void resetColor() {
        // Remove primary color and reset top navigation items to white
        navItemAllTasks.setTextColor(Color.parseColor("#FFFFFF"));
        navItemMyTasks.setTextColor(Color.parseColor("#FFFFFF"));
        navItemCompleted.setTextColor(Color.parseColor("#FFFFFF"));
    }
}
