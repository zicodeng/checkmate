package edu.uw.dengz6.checkmate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment {

    public static final String TAG = "Shopping_Fragment";

    // Pager
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private android.app.ActionBar actionBar;

    // Fragments

    // Top navigation tab title
    private String[] tabs = {"Shopping List", "History"};

    public ShoppingFragment() {
        // Required empty public constructor
    }

    public static ShoppingFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingFragment fragment = new ShoppingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_shopping, container, false);

        // Initialize ViewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.pager_shopping);
        // We have to use "Child Fragment Manager" because we are targeting nested fragments
        pagerAdapter = new ShoppingPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);


        // Inflate the layout for this fragment
        return rootView;
    }

    private class ShoppingPagerAdapter extends FragmentPagerAdapter {

        public ShoppingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // Define the page order
            if (position == 0) {
                return ShoppingListFragment.newInstance();
            }

            if (position == 1) {
                return ShoppingHistoryFragment.newInstance();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
