package com.sostvcn.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yubin on 2017\9\12 0012.
 */

public class SosBookViewPagerAdapter extends PagerAdapter{

    private List<View> listViews;
    private HashMap<View,Boolean> loadTag;
    private Context context;
    private int mIndex;

    public SosBookViewPagerAdapter(List<View> listViews, Context context){
        this.listViews = listViews;
        this.context = context;
        loadTag = new HashMap<>();
    }

    /**
     * 设置加载标志
     *
     * @param tag
     */
    public void setLoadTag(boolean tag) {
        if (listViews != null && listViews.size() > 0) {
            int size = listViews.size();
            for (View views : listViews) {
                loadTag.put(views, tag);
            }
        }
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
