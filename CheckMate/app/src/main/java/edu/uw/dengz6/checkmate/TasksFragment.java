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
public class TasksFragment extends Fragment  {

    public static final String TAG = "Tasks_Fragment";

    private TextView navItemAllTasks;
    private TextView navItemMyTasks;
    private TextView navItemCompleted;

    // Pager
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private android.app.ActionBar actionBar;

    // Top navigation tab title
    private String[] tabs = {"All Tasks", "My Tasks", "Completed"};

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TasksFragment fragment = new TasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tasks, container, false);

        // Initialize ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.pager_tasks);
        pagerAdapter = new TasksPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

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
                return new AllTasksFragment();
            }

            if (position == 1) {
                return new MyTasksFragment();
            }

            if (position == 2) {
                return new CompletedFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
