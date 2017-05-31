package edu.uw.dengz6.checkmate;

/**
 * Created by Leon on 5/30/17.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAllFragment extends Fragment {

    public static final String TAG = "All_Tasks_Fragment";
    protected static SessionManager manager;
    protected static HashMap<String, String> userInfo;
    protected static ArrayList<TaskData> tasks;
    private TaskAdapter adapter;

    public TaskAllFragment() {
        // Required empty public constructor
    }

    public static TaskAllFragment newInstance() {
        Bundle args = new Bundle();
        TaskAllFragment fragment = new TaskAllFragment();
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
                // Create a dialog and ask the user for input
                DialogFragment AddNewTaskFragment = TaskAllFragment.AddNewTaskFragment.newInstance();
                AddNewTaskFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Task");
            }
        });

        adapter = new TaskAdapter(getActivity(), tasks);
        // Attach the adapter to a ListView
        ListView listView = (ListView) rootView.findViewById(R.id.all_tasks_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String taskID = tasks.get(position).taskID;
                // Create a dialog and ask the user to
                // either delete the task or mark it as completed
                DialogFragment taskManageFragment = TaskManageFragment.newInstance(taskID);
                taskManageFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "Manage_Task");
                return false;
            }
        });

        // Progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Retrieving data...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

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
                    for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                        //handle each task
                        TaskData task = taskSnapshot.getValue(TaskData.class);
                        // show incomplete tasks
                        if (!task.isCompleted) {
                            tasks.add(task);
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    ;

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
            final EditText assigneeInput = (EditText)viewInflated.findViewById(R.id.task_assignee);
            final PopupMenu menu = new PopupMenu(getContext(), viewInflated);

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                            userInfo.get(SessionManager.KEY_GROUP_NAME) + "/users");

            final Map<String, String> storedOption = new HashMap();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        Map singleUser = (Map) entry.getValue();
                        String userName = (String) singleUser.get("name");
                        String userID = entry.getKey();
                        menu.getMenu().add(userName);
                        storedOption.put(userName, userID);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

                ;
            });

            // disable keyboard input for popup menu
            assigneeInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.show();
                }
            });
            assigneeInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        menu.show();
                    }
                }
            });
            assigneeInput.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    menu.show();
                    return true;
                }
            });

            //Set popup and drop down list
            final String[] assignee = new String[1];
            final String[] assigneeId = new String[1];

            //handle menu selection
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.v(TAG, "Item Selected: " + item.toString());
                    storedOption.get(item.toString());
                    assignee[0] = item.toString();
                    assigneeId[0] = storedOption.get(item.toString());
                    assigneeInput.setText(item.toString());
                    return true;
                }
            });

            //Time picker
            final EditText dueTime = (EditText) viewInflated.findViewById(R.id.task_due_time);
            dueTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if (hasFocus) {
                         final Dialog timePickerDialog = new Dialog(getContext());
                         timePickerDialog.setContentView(R.layout.custom_time_picker);
                         TimePicker picker = (TimePicker) timePickerDialog.findViewById(R.id.timePicker1);
                         picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                             @Override
                             public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                 String minuteString = "0";
                                 if (minute < 10) {
                                     minuteString+= minute;
                                 }
                                 if (hourOfDay < 13) {
                                     dueTime.setText(hourOfDay + ":" + minuteString+ " AM");
                                 } else {
                                     dueTime.setText(hourOfDay - 12 + ":" + minuteString + " PM");
                                 }
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
                 }
             });

            dueTime.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog timePickerDialog = new Dialog(getContext());
                    timePickerDialog.setContentView(R.layout.custom_time_picker);
                    TimePicker picker = (TimePicker) timePickerDialog.findViewById(R.id.timePicker1);
                    picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            if (hourOfDay < 13) {
                                dueTime.setText(hourOfDay + ":" + minute + " AM");
                            } else {
                                dueTime.setText(hourOfDay - 12 + ":" + minute + " PM");
                            }
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
            final EditText dueDate = (EditText) viewInflated.findViewById(R.id.task_due_date);
            dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if (hasFocus) {
                         final Dialog datePickerDialog = new Dialog(getContext());
                         datePickerDialog.setContentView(R.layout.custom_date_picker);
                         DatePicker datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datePicker1);
                         Calendar calendar = Calendar.getInstance();
                         calendar.setTimeInMillis(System.currentTimeMillis());
                         datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

                             @Override
                             public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                                 dueDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
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
                 }
             });

            dueDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Dialog datePickerDialog = new Dialog(getContext());
                    datePickerDialog.setContentView(R.layout.custom_date_picker);
                    DatePicker datePicker = (DatePicker) datePickerDialog.findViewById(R.id.datePicker1);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            new DatePicker.OnDateChangedListener() {

                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                            dueDate.setText((month + 1) + "/" + dayOfMonth + "/" + year);
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

                    String title = taskTitle.getText().toString();
                    String detail = taskDetail.getText().toString();
                    // Validate input
                    // Make sure all the fields are filled
                    if (title.length() != 0 && detail.length() != 0) {

                        DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                        userInfo.get(SessionManager.KEY_GROUP_NAME) + "/tasks");

                        DatabaseReference mTask = ref.push();

                        String taskID = mTask.getKey();

                        SimpleDateFormat dt = new SimpleDateFormat("MM/dd/yyyy hh:mm aaa");
                        String createdOn = dt.format(new Date());
                        String dueOn = dueDate.getText().toString() + " " + dueTime.getText().toString();
                        String assigner = TaskAllFragment.userInfo.get(SessionManager.KEY_NAME);
                        mTask.setValue(new TaskData(title, detail, dueOn, createdOn, assigner, assignee[0], assigneeId[0], false, taskID));


                        final DatabaseReference tasksAssignedRef = FirebaseDatabase.getInstance()
                                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                        userInfo.get(SessionManager.KEY_GROUP_NAME) + "/users/" +
                                        assigneeId[0] + "/tasksAssigned");

                        tasksAssignedRef.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                tasksAssignedRef.setValue(((Long) dataSnapshot.getValue()).intValue() + 1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(getActivity(), "New task added", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();
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
