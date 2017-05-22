package edu.uw.dengz6.checkmate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    // Fragment
    private FragmentManager fm;
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shopping:

                    // Change to "Shopping Fragment"
                    ShoppingFragment shoppingFragment = ShoppingFragment.newInstance();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.container, shoppingFragment, "Shopping_Fragment");
                    ft.commit();
                    return true;

                case R.id.navigation_tasks:

                    // Change to "Tasks Fragment"
                    TasksFragment tasksFragment = TasksFragment.newInstance();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.container, tasksFragment, "Tasks_Fragment");
                    ft.commit();
                    return true;

                case R.id.navigation_announcement:

                    // Change to "Announcement Fragment"
                    AnnouncementFragment announcementFragment = AnnouncementFragment.newInstance();
                    ft = fm.beginTransaction();
                    ft.replace(R.id.container, announcementFragment, "Shopping_Fragment");
                    ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize FragmentManager
        fm = getSupportFragmentManager();

        // Set "Tasks Fragment" as default
        TasksFragment tasksFragment = TasksFragment.newInstance();
        ft = fm.beginTransaction();
        ft.replace(R.id.container, tasksFragment, "Tasks_Fragment");
        ft.commit();

        navigation.getMenu().getItem(1).setChecked(true);
    }
}
