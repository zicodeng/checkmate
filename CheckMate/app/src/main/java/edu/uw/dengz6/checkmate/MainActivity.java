package edu.uw.dengz6.checkmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MAINACTIVITY";
    // Fragment
    private FragmentManager fm;
    private FragmentTransaction ft;
    private SessionManager session;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;

                case R.id.navigation_dashboard:

                    return true;

                case R.id.navigation_notifications:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());

        session.checkLogin();
        if(!session.isLoggedIn()){
            //TODO: need to put some logic here to see if the user has logged in or not
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // name
            String name = user.get(SessionManager.KEY_NAME);

            // email
            String email = user.get(SessionManager.KEY_EMAIL);
            Log.v(TAG, name);
            Log.v(TAG, email);
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Initialize FragmentManager
        fm = getSupportFragmentManager();

        // Set "Tasks Navigation Fragment" as default
        TasksNavigationFragment tasksNavigationFragment = TasksNavigationFragment.newInstance();
        ft = fm.beginTransaction();
        ft.replace(R.id.container, tasksNavigationFragment, "Tasks_Navigation_Fragment");
        ft.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2, menu);
        return true; //we've provided a menu!
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                session.logoutUser();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true; //handled

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
