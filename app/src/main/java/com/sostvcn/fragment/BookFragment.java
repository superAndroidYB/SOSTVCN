package com.sostvcn.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FragmentViewPagerAdapter;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.model.SosAudioCatesInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 书籍一级目录
 */
public class BookFragment extends BaseFragment {

    @ViewInject(R.id.toolbar_title)
    private TextView toolbarTitle;

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.sliding_tabs)
    private TabLayout tabLayout;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    private List<Fragment> fragments;
    private List<String> tabs;
    private List<SosAudioCatesInfo> catesInfos;

    private FragmentViewPagerAdapter viwePageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        toolbarTitle.setText(R.string.rd_menu_book_title);
    }

    @Override
    protected void onEvent() {

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BookFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BookFragment");
    }


}
