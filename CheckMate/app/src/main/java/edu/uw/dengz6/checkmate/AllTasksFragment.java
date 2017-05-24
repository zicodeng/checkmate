package edu.uw.dengz6.checkmate;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksFragment extends Fragment {

    public static final String FIREBASE_URL = "https://checkmate-d2c41.firebaseio.com/groups/";

    public static final String TAG = "All_Tasks_Fragment";
    public AllTasksFragment() {
        // Required empty public constructor
    }

    public static AllTasksFragment newInstance() {
        Bundle args = new Bundle();
        AllTasksFragment fragment = new AllTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_all_tasks, container, false);

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_all_tasks);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New Task", Toast.LENGTH_SHORT).show();
            }
        });

        SessionManager manager = new SessionManager(getContext());
        HashMap<String, String> userInfo = manager.getUserDetails();

        Firebase.setAndroidContext(getActivity());
        Firebase taskRef = new Firebase(FIREBASE_URL + userInfo.get(SessionManager.KEY_GROUP_NAME) + "/users");
        taskRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 Log.v(TAG, dataSnapshot.getValue().toString());

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    };

}
