package edu.uw.dengz6.checkmate;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnnouncementFragment extends Fragment {


    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public static AnnouncementFragment newInstance() {

        Bundle args = new Bundle();

        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get root view so we can use it to find its child views later
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);

        FloatingActionButton fabAllTasks = (FloatingActionButton) rootView.findViewById(R.id.fab_announcement);

        fabAllTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Add a New Announcement", Toast.LENGTH_SHORT).show();
                DialogFragment addNewAnnouncementFragment = AddNewAnnouncementFragment.newInstance();
                addNewAnnouncementFragment.show(getActivity().getSupportFragmentManager(), "Add_New_Shopping_List_Fragment");
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public static class AddNewAnnouncementFragment extends DialogFragment {

        public static AddNewAnnouncementFragment newInstance() {

            Bundle args = new Bundle();

            AddNewAnnouncementFragment fragment = new AddNewAnnouncementFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Add a New Announcement");

            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_new_announcement, (ViewGroup) getView(), false);

            // Set up the input
            final EditText title = (EditText) viewInflated.findViewById(R.id.textTitle);
            final EditText description = (EditText) viewInflated.findViewById(R.id.textDescription);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    String getTitle = title.getText().toString();
                    String getDescription = description.getText().toString();
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

