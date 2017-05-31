package edu.uw.dengz6.checkmate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Zico Deng on 5/30/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout for "Settings Activity"
        setContentView(R.layout.activity_settings);

        SessionManager sessionManager = new SessionManager(this);

        // Get relevant information stored in session
        String groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);
        String username = sessionManager.getUserDetails().get(SessionManager.KEY_NAME);
        String email = sessionManager.getUserDetails().get(SessionManager.KEY_EMAIL);

        TextView txtGroupName = (TextView) findViewById(R.id.settings_group_name);
        TextView txtUsername = (TextView) findViewById(R.id.settings_username);
        TextView txtEmail = (TextView) findViewById(R.id.settings_email);

        txtGroupName.setText("Group Name: " + groupName);
        txtUsername.setText("Username: " + username);
        txtEmail.setText("Email: " + email);
    }
}
