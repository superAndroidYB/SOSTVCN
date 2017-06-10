package com.sostvcn.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FragmentViewPagerAdapter;
import com.sostvcn.api.VideoPageApi;
import com.sostvcn.fragment.video.VideoChildFragment;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosCatesInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class VideoFragment extends BaseFragment {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.sliding_tabs)
    private TabLayout tabLayout;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    private VideoPageApi api;
    public static List<SosCatesInfo> catesInfos;

    private List<Fragment> fragments;
    private List<String> tabs;

    private FragmentViewPagerAdapter viwePageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        loadData();

    }

    @Override
    protected void onEvent() {

    }

    public void loadData() {
        api = HttpUtils.getInstance(getActivity())
                .getRetofitClinet()
                .builder(VideoPageApi.class);
        api.loadCatesInfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosCatesInfo>>(new SubscriberOnNextListener<BaseListResponse<SosCatesInfo>>() {
                    @Override
                    public void onNext(BaseListResponse<SosCatesInfo> sosCatesInfoBaseListResponse) {
                        catesInfos = sosCatesInfoBaseListResponse.getResults();
                        fragments = new ArrayList<>();
                        tabs = new ArrayList<>();
                        if (catesInfos.size() <= 3) {
                            tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        }else{
                            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }
                        for (SosCatesInfo cateInfo : catesInfos) {
                            VideoChildFragment fragment = new VideoChildFragment(cateInfo.getSubCates());
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideoFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideoFragment");
    }
}
