package com.sostvcn.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */
public class FragmentViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabs) {
        super(fm);
        this.fragments = fragments;
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position % tabs.size());
    }
}
