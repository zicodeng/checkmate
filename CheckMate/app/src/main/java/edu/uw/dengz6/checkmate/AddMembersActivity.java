package edu.uw.dengz6.checkmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class AddMembersActivity extends AppCompatActivity {
    private static final String TAG = "AddMembersActivity";
    private final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        Intent intent = getIntent();
        String group_id = intent.getStringExtra("group_id");

        final LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        //Keeping track of each edit view
        final ArrayList<EditText> myTextViews = new ArrayList<>();
        Button addMemberButton = (Button) findViewById(R.id.addMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText rowTextView = new EditText(AddMembersActivity.this);
                rowTextView.setHint("Group Member Email");
                rowTextView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                myLinearLayout.addView(rowTextView);
                myTextViews.add(rowTextView);
            }
        });
        final Map<String, String> userDetails = new SessionManager(this).getUserDetails();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(group_id).child("emails_to_be_sent");
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < myTextViews.size(); i++){
                    DatabaseReference pushedData = ref.push();
                    pushedData.setValue(myTextViews.get(i).getText().toString());
                    // Instantiate the RequestQueue.
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    final String url ="http://ec2-54-212-232-252.us-west-2.compute.amazonaws.com/form.php?email="+ myTextViews.get(i).getText().toString().trim()+"&from="+userDetails.get(SessionManager.KEY_NAME)+"&group_name="+userDetails.get(SessionManager.KEY_GROUP_NAME);

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.
                                    Log.v(TAG, url);
                                    Log.v(TAG, "Response is: "+ response.toString());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.v(TAG,"That didn't work!");
                        }
                    });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }

                final ProgressDialog progressDialog = new ProgressDialog(AddMembersActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Submitting List...");
                progressDialog.show();
                new android.os.Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(AddMembersActivity.this, MainActivity.class));
                            progressDialog.dismiss();
                        }
                    },2000);
            }
        });
    }
}
