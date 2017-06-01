package edu.uw.dengz6.checkmate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by iguest on 5/21/17.
 */

public class ShoppingHistoryFragment extends Fragment {

    public static final String TAG = "Shopping_History";

    private ArrayList<ShoppingHistoryData> shoppingHistoryList;
    private ShoppingHistoryAdapter shoppingHistoryAdapter;
    private RecyclerView shoppingHistoryRecyclerView;

    private String groupName;

    public ShoppingHistoryFragment() {
        // Required empty public constructor
    }

    public static ShoppingHistoryFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingHistoryFragment fragment = new ShoppingHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_shopping_history, container, false);

        // Initialize "Shopping History List"
        shoppingHistoryList = new ArrayList<ShoppingHistoryData>();

        // Construct adapter
        shoppingHistoryAdapter = new ShoppingHistoryAdapter(shoppingHistoryList, getActivity());

        // Get reference to RecyclerView
        shoppingHistoryRecyclerView = (RecyclerView) rootView.findViewById(R.id.shopping_history_recycler_view);

        // Attach RecyclerView with adapter
        shoppingHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shoppingHistoryRecyclerView.setAdapter(shoppingHistoryAdapter);

        // Get current group name
        final SessionManager sessionManager = new SessionManager(getActivity());
        groupName = sessionManager.getUserDetails().get(SessionManager.KEY_GROUP_NAME);

        // Set up Firebase connection
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" + groupName + "/users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppingHistoryList.clear();

                // Loop through each user
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {

                    String userID = userSnapShot.getKey();

                    User mUser = userSnapShot.getValue(User.class);

                    final String userName = mUser.name;
                    final String createdOn = mUser.createdOn;

                    // Set up Firebase connection
                    DatabaseReference shoppingHistoryListRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://checkmate-d2c41.firebaseio.com/groups/" +
                                    groupName + "/users/" + userID + "/shoppingHistoryList");

                    shoppingHistoryListRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int totalSpend = 0;
                            int totalShoppingLists = 0;

                            // Loop through "Shopping History List" under each user
                            for (DataSnapshot shoppingHistoryListSnapShot : dataSnapshot.getChildren()) {
                                ShoppingListData mShoppingListData = shoppingHistoryListSnapShot.getValue(ShoppingListData.class);

                                 totalSpend += mShoppingListData.totalCost;
                                totalShoppingLists ++;

                            }

                            ShoppingHistoryData mShoppingHistoryData =
                                    new ShoppingHistoryData(userName, totalSpend, totalShoppingLists, createdOn);

                            shoppingHistoryList.add(mShoppingHistoryData);
                            // Sort the list based on the total spent and total shopping lists
                            Collections.sort(shoppingHistoryList, new Comparator<ShoppingHistoryData>() {
                                @Override
                                public int compare(ShoppingHistoryData user1, ShoppingHistoryData user2)
                                {
                                    if (user2.totalSpend == user1.totalSpend) {
                                        return user2.totalShoppingLists - user1.totalShoppingLists;
                                    }
                                    return  user2.totalSpend - user1.totalSpend;
                                }
                            });

                            shoppingHistoryAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public class ShoppingHistoryAdapter extends RecyclerView.Adapter<ShoppingHistoryAdapter.ViewHolder> {

        private LayoutInflater inflater;
        private ArrayList<ShoppingHistoryData> shoppingHistoryList;

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtUserName;
            private TextView txtTotalSpend;
            private TextView txtTotalShoppingLists;
            private TextView txtSince;
            private TextView txtTotalSpendFocus;

            public ViewHolder(View itemView) {
                super(itemView);

                // Tell our ViewHolder what kind of views it should contain
                txtUserName = (TextView) itemView.findViewById(R.id.shopping_history_user_name);
                txtTotalSpend = (TextView) itemView.findViewById(R.id.shopping_history_total_spend);
                txtTotalShoppingLists = (TextView) itemView.findViewById(R.id.shopping_history_total_shopping_lists);
                txtSince = (TextView) itemView.findViewById(R.id.shopping_history_since);
                txtTotalSpendFocus = (TextView) itemView.findViewById(R.id.shopping_history_total_spend_focus);
            }
        }

        public ShoppingHistoryAdapter(ArrayList<ShoppingHistoryData> shoppingHistoryList, Context context) {
            this.shoppingHistoryList = shoppingHistoryList;
            this.inflater = LayoutInflater.from(context);
        };

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // P1: which layout template file we want each item to inflate
            View view = inflater.inflate(R.layout.list_item_shopping_history, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // Each item on the list is corresponding to an unique id
            // We can access to each item by using its id
            final ShoppingHistoryData item = shoppingHistoryList.get(position);

            // Bind data in each item with our ViewHolder
            String userName = item.userName;
            String totalSpend = "$" + item.totalSpend;
            String totalShoppingList = "Total Shopping Lists: " + item.totalShoppingLists;
            String since = "Since: " + item.since;

            viewHolder.txtUserName.setText(userName);
            viewHolder.txtTotalSpend.setText("Total Spend: " + totalSpend);
            viewHolder.txtTotalShoppingLists.setText(totalShoppingList);
            viewHolder.txtSince.setText(since);
            viewHolder.txtTotalSpendFocus.setText(totalSpend);
        }

        @Override
        public int getItemCount() {
            // Tell the adapter how many ViewHolder objects it is going to create along the way when user scrolls
            return shoppingHistoryList.size();
        }
    }
}
