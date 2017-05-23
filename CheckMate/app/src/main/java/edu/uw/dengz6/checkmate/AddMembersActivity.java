package edu.uw.dengz6.checkmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class AddMembersActivity extends AppCompatActivity {
    private static final String TAG = "AddMembersActivity";
    private final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups";

    private int number = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        Intent intent = getIntent();
        String group_id = intent.getStringExtra("group_id");
        final int N = 10; // total number of textviews to add
        final LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);

        final TextView[] myTextViews = new TextView[N]; // create an empty array;
        Button addMemberButton = (Button) findViewById(R.id.addMemberButton);
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText rowTextView = new EditText(AddMembersActivity.this);
                myLinearLayout.addView(rowTextView);
                myTextViews[number] = rowTextView;
                number++;
            }
        });
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(FIREBASE_URL+"/"+group_id+"/emails_to_be_sent");
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < number; i++){
                    Firebase pushedData = ref.push();
                    pushedData.setValue(myTextViews[i].getText().toString());
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
                        },1000);
            }
        });

    }


}
