package com.sostvcn.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FragmentViewPagerAdapter;
import com.sostvcn.api.AudioPageApi;
import com.sostvcn.fragment.audio.AudioChildFragment;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosAudioCatesInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AudioFragment extends BaseFragment {

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

    private AudioPageApi api;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_audio;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        toolbarTitle.setText("音频");
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        loadData();
    }

    private void loadData() {
        api = HttpUtils.getInstance(getActivity())
                .getRetofitClinet()
                .builder(AudioPageApi.class);
        api.loadCatesinfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosAudioCatesInfo>>(new SubscriberOnNextListener<BaseListResponse<SosAudioCatesInfo>>() {
                    @Override
                    public void onNext(BaseListResponse<SosAudioCatesInfo> sosAudioCatesInfoBaseListResponse) {
                        catesInfos = sosAudioCatesInfoBaseListResponse.getResults();
                        fragments = new ArrayList<>();
                        tabs = new ArrayList<>();
                        if (catesInfos.size() <= 3) {
                            tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        } else {
                            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }

                        for (SosAudioCatesInfo cateInfo : catesInfos) {
                            AudioChildFragment fragment = new AudioChildFragment(cateInfo);
                            tabs.add(cateInfo.getCate_title());
                            fragments.add(fragment);
                            tabLayout.addTab(tabLayout.newTab().setText(cateInfo.getCate_title()));
                        }

                        viwePageAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, tabs);
                        viewPager.setAdapter(viwePageAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                }, getActivity()));
    }

    @Override
    protected void onEvent() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AudioFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AudioFragment");
    }
}
