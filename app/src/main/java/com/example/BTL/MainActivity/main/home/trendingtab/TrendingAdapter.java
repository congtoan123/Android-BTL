package com.example.BTL.MainActivity.main.home.trendingtab;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.BTL.R;

import java.util.ArrayList;

public class TrendingAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private ArrayList<Fragment> mData = new ArrayList<>();
    private void initData() {
        mData.add(HomeFragment.newInstance());
        mData.add(NowShowingFragment.newInstance());
        mData.add(SearchFragment.newInstance());
    }

    public TrendingAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        mContext = context;
        initData();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mData.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
       return mData.get(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return mContext.getResources().getString(R.string.home);
            case 1: return mContext.getResources().getString(R.string.now_showing);
            case 2: return "Search";
            default: return null;
        }

    }
}
