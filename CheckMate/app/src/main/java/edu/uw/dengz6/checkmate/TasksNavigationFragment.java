package edu.uw.dengz6.checkmate;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

    private AllTasksFragment allTasksFragment;
    private MyTasksFragment myTasksFragment;
    private CompletedFragment completedFragment;

    // Pager
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

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


        // Get views so we can manipulate them later
        navItemAllTasks = (TextView) rootView.findViewById(R.id.nav_item_all_tasks);
        navItemMyTasks = (TextView) rootView.findViewById(R.id.nav_item_my_tasks);
        navItemCompleted = (TextView) rootView.findViewById(R.id.nav_item_completed);

        // Initialize ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.pager_tasks);
        pagerAdapter = new TasksPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Initialize Fragments
        allTasksFragment = AllTasksFragment.newInstance();
        myTasksFragment = MyTasksFragment.newInstance();
        completedFragment = CompletedFragment.newInstance();

        // Set "All Tasks" as default navigation item with primary color
        navItemAllTasks.setTextColor(Color.parseColor("#4B2E83"));

        // Set "All Tasks Fragment" as default page
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);


        // Top navigation item click listener
        navItemAllTasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemAllTasks.setTextColor(Color.parseColor("#4B2E83"));

                // Change to "All Tasks Fragment" page
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
            }
        });

        navItemMyTasks.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemMyTasks.setTextColor(Color.parseColor("#4B2E83"));

                // Change to "My Tasks Fragment" page
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(1);
            }
        });

        navItemCompleted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetColor();
                navItemCompleted.setTextColor(Color.parseColor("#4B2E83"));

                // Change to "Completed Fragment" page
                pagerAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(0);
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

    private class TasksPagerAdapter extends FragmentPagerAdapter {

        public TasksPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // Define the page order
            if (position == 0) {
                return allTasksFragment;
            }

            if (position == 1) {
                return myTasksFragment;
            }

            if (position == 2) {
                return completedFragment;
            }

            return null;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            if (myTasksFragment == null) {
                return 1;
            } else if (completedFragment == null) {
                return 2;
            } else {
                return 3;
            }
        }
    }
}
