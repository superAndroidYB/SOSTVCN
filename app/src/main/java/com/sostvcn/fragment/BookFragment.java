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
import com.sostvcn.api.BookPageApi;
import com.sostvcn.api.NetWorkApi;
import com.sostvcn.fragment.book.BookChildFragment;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private List<SosBookCates> catesInfos;

    private FragmentViewPagerAdapter viwePageAdapter;
    private BookPageApi api;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        toolbarTitle.setText(R.string.rd_menu_book_title);

        api = HttpUtils.getInstance(this.getActivity()).getRetofitClinet().setBaseUrl(NetWorkApi.BASE_UR2).builder(BookPageApi.class);
        loadData();
    }

    @Override
    protected void onEvent() {

    }

    public void loadData(){
        api.loadCatesinfo()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosBookCates>>(new SubscriberOnNextListener<BaseListResponse<SosBookCates>>() {
                    @Override
                    public void onNext(BaseListResponse<SosBookCates> sosBookCatesBaseListResponse) {
                        catesInfos = sosBookCatesBaseListResponse.getResults();
                        fragments = new ArrayList<>();
                        tabs = new ArrayList<>();
                        if (catesInfos.size() <= 3) {
                            tabLayout.setTabMode(TabLayout.MODE_FIXED);
                        } else {
                            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                        }

                        for(SosBookCates cateInfo : catesInfos){
                            BookChildFragment fragment = new BookChildFragment(cateInfo);
                            tabs.add(cateInfo.getCate_title());
                            fragments.add(fragment);
                            tabLayout.addTab(tabLayout.newTab().setText(cateInfo.getCate_title()));
                        }

                        viwePageAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), fragments, tabs);
                        viewPager.setAdapter(viwePageAdapter);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                },getActivity()));
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
