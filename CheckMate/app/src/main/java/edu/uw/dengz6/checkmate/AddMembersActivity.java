package edu.uw.dengz6.checkmate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddMembersActivity extends AppCompatActivity {
    private static final String TAG = "AddMembersActivity";
    private int number = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        Intent intent = getIntent();
        int group_id = intent.getIntExtra("group_id", 0);
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
        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0 ; i < myTextViews.length; i++){
                    Log.v(TAG, "seomthing");
                }
            }
        });
    }


}
