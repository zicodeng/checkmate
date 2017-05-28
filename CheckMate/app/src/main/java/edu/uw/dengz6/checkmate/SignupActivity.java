package edu.uw.dengz6.checkmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private SessionManager sessionManager;
    private DatabaseReference groupRef;

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    @InjectView(R.id.input_group_name) TextView _groupText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        sessionManager = new SessionManager(this);

        if(getIntent() != null && getIntent().getData() != null && getIntent().getData().getQueryParameter("group_name") != null) {
            _groupText.setText(getIntent().getData().getQueryParameter("group_name"));
        }

        if(sessionManager.isLoggedIn()){
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
        }

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Group...");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        final String groupName = _groupText.getText().toString();

        //set up firebase before using
        groupRef = FirebaseDatabase.getInstance().getReference().child("groups");
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(groupName)){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Toast.makeText(SignupActivity.this, "Please pick a different group name", Toast.LENGTH_SHORT).show();
                                    _signupButton.setEnabled(true);
                                    progressDialog.dismiss();
                                }
                            }, 1000);
                }else{
                    final Group group = new Group(groupName);
                    final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("groups").child(groupName);
                    new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                ref1.setValue(group, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) { //check for errors while pushing
                                        if (databaseError != null) {
                                            Log.v(TAG, "Data could not be saved. " + databaseError.getMessage());
                                        } else {
                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("groups").child(groupName).child("users");

                                            // Date of creation
                                            // Get current date
                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                                            String currentDate = simpleDateFormat.format(new Date());

                                            User user = new User(name, email, password, currentDate);
                                            DatabaseReference userPush = userRef.push();
                                            // Generate a random value as user ID
                                            final String userID = userPush.getKey();

                                            userPush.setValue(user, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError != null) {
                                                        Log.v(TAG, "Data could not be saved. " + databaseError.getMessage());
                                                    }else{ //if there are no errors then we proceed to adding people into the group
                                                        // On complete call either onSignupSuccess or onSignupFailed
                                                        // depending on success
                                                        onSignupSuccess(groupName, email, name, userID);
                                                    }
                                                }
                                            });
                                        }
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }, 1500);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    //Passing in the group key so we can track which group are we talking about
    public void onSignupSuccess(String groupName, String email, String userName, String userID) {
        _signupButton.setEnabled(false);
        setResult(RESULT_OK, null);
        sessionManager.createLoginSession(userName, email, userID, groupName);

        // Start "AddMembersActivity"
        Intent intent = new Intent(getApplicationContext(), AddMembersActivity.class);
        intent.putExtra("group_id", groupName);
        startActivityForResult(intent, 0);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String groupName = _groupText.getText().toString();
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        if(groupName.isEmpty() || groupName.length() < 3){
            _groupText.setError("at least 3 characters");
            valid = false;
        } else {
            _groupText.setError(null);
        }
        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
