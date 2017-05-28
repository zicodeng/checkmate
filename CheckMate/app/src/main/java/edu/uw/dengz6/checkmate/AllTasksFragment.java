package edu.uw.dengz6.checkmate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllTasksFragment extends Fragment {

    public static final String TAG = "All_Tasks_Fragment";
    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;
    protected static ArrayList<TaskData> tasks;
    private TaskAdapter adapter;

    public AllTasksFragment() {
        // Required empty public constructor
    }

    public static AllTasksFragment newInstance() {
        Bundle args = new Bundle();
        AllTasksFragment fragment = new AllTasksFragment();
        tasks = new ArrayList<>();
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

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New Task", Toast.LENGTH_SHORT).show();

                // Create a dialog and ask the user for input
                DialogFragment AddNewTaskFragment = AllTasksFragment.AddNewTaskFragment.newInstance();
                AddNewTaskFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Task");
            }
        });

        adapter = new TaskAdapter(getActivity(), tasks);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.all_tasks_list_view);
        listView.setAdapter(adapter);

        manager = new SessionManager(getContext());
        userInfo = manager.getUserDetails();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                        userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    adapter.clear();
                    tasks.clear();
                    for (DataSnapshot taskSnapshot: dataSnapshot.getChildren()) {
                        //handle each task
                        TaskData task = taskSnapshot.getValue(TaskData.class);
                        tasks.add(task);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    };


    public static class AddNewTaskFragment extends DialogFragment {

        public static AddNewTaskFragment newInstance() {

            Bundle args = new Bundle();

            AddNewTaskFragment fragment = new AddNewTaskFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Task");

            final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_new_task, (ViewGroup) getView(), false);

            // Set up the input
            final EditText taskTitle = (EditText) viewInflated.findViewById(R.id.task_title);
            final EditText taskDetail = (EditText) viewInflated.findViewById(R.id.task_detail);
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                            userInfo.get(SessionManager.KEY_GROUP_NAME) + "/users");
            final List<CharSequence> users = new ArrayList<>();
            final List<String> usersID = new ArrayList<>();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                   for (Map.Entry<String, Object> entry : data.entrySet()) {
                       Map singleUser = (Map) entry.getValue();
                       users.add((String)singleUser.get("name"));
                       usersID.add(entry.getKey());
                   }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               };
           });

            //Set spinner and drop down list
            final String[] assignee = {""};
            final Spinner spinner = (Spinner) viewInflated.findViewById(R.id.mySpinner);
            final ArrayAdapter<CharSequence> adapter =  new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,users);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(adapter);
            //TODO: THIS FUNCTION IS NEVER CALLED ???!
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    assignee[0] = (String) parent.getItemAtPosition(position);
                    Log.v(TAG, usersID.get(position));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            //Time picker
            final EditText time = (EditText) viewInflated.findViewById(R.id.task_due_time);
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog timePickerDialog = new Dialog(getContext());
                    timePickerDialog.setContentView(R.layout.custom_time_picker);
                    TimePicker picker = (TimePicker) timePickerDialog.findViewById(R.id.timePicker1);
                    picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            time.setText(hourOfDay+":"+minute);
                            Log.v(TAG, "Changed");
                        }
                    });
                    timePickerDialog.show();
                    Button button = (Button) timePickerDialog.findViewById(R.id.button2);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            timePickerDialog.dismiss();
                        }
                    });
                }
            });
            //Date picker
            final EditText date = (EditText) viewInflated.findViewById(R.id.task_due_date);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog datePickerDialog = new Dialog(getContext());
                    datePickerDialog.setContentView(R.layout.custom_date_picker);
                    DatePicker datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datePicker1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            date.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                            Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                        }
                    });
                    datePickerDialog.show();
                    Button button = (Button) datePickerDialog.findViewById(R.id.button3);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            datePickerDialog.dismiss();
                        }
                    });
                }
            });



            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                    userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

                    DatabaseReference mTask = ref.push();
                    // TODO: roommate dropdown and date picker
                    dialog.dismiss();
                    String title = taskTitle.getText().toString();
                    String detail = taskDetail.getText().toString();
                    SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                    String createdOn = dt.format(new Date());
                    String assigner = AllTasksFragment.userInfo.get(SessionManager.KEY_NAME);
                    mTask.setValue(new TaskData(title, detail, createdOn, createdOn, assigner, assignee[0]));
                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            return builder.create();
        }
    }

}
