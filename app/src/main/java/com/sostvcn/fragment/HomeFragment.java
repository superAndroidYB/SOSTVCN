package com.sostvcn.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sostvcn.R;
import com.sostvcn.adapter.HomeSlideAdapter;
import com.sostvcn.adapter.SosBookGridViewAdapter;
import com.sostvcn.adapter.SosHomeGridViewAdapter;
import com.sostvcn.adapter.SostvHomeListViewAdapter;
import com.sostvcn.api.HomePageApi;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosAudioRecommend;
import com.sostvcn.model.SosBookRecommend;
import com.sostvcn.model.SosSlideImage;
import com.sostvcn.model.SosVideoRecommend;
import com.sostvcn.ui.VideoViewActivity;
import com.sostvcn.utils.ListViewUtils;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.HomeSlideGallery;
import com.sostvcn.widget.ReboundScrollView;
import com.sostvcn.widget.SostvGridView;
import com.sostvcn.widget.SostvListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeFragment extends BaseFragment implements ReboundScrollView.ReboundScrollListener {

    @ViewInject(R.id.home_toolbar)
    private Toolbar toolbar;

    @ViewInject(R.id.home_slide_view)
    private HomeSlideGallery gallery;
    private int galleryHeight = 0;
    private List<SosSlideImage> list;

    @ViewInject(R.id.imagettitle)
    private TextView titleView;
    @ViewInject(R.id.imagesubttitle)
    private TextView subTitleView;


    @ViewInject(R.id.rebound_scroll_View)
    private ReboundScrollView srcollView;

    @ViewInject(R.id.gridview_tjsp)
    private SostvGridView tjspGridView;
    @ViewInject(R.id.btn_tjsphyh)
    private LinearLayout btnTjsphyh;

    @ViewInject(R.id.gridview_tjyd)
    private SostvGridView tjydGridView;
    @ViewInject(R.id.btn_tjydhyh)
    private LinearLayout btnTjydhyh;

    @ViewInject(R.id.listview_tjyp)
    private SostvListView tjypListView;
    @ViewInject(R.id.btn_tjyphyh)
    private LinearLayout btnTjyphyh;

    private HomePageApi api;
    private List<SosVideoRecommend> videoRecommends;
    private SosHomeGridViewAdapter sosHomeGridViewAdapter;
    private List<SosBookRecommend> bookRecommends;
    private SosBookGridViewAdapter sosBookGridViewAdapter;
    private List<SosAudioRecommend> audioRecommends;
    private SostvHomeListViewAdapter sostvHomeListViewAdapter;

    private int spNextPage;
    private int ydNextPage;
    private int ypNextPage;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        initView();
        loadHomeSildData();
        loadHomeVideoData();
        loadHomeBookData();
        loadHomeAudioData();
    }

    @Override
    protected void onEvent() {
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoViewActivity.start(getActivity(), list.get(i % list.size()).getVideo_id());
            }
        });
        tjspGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SosVideoRecommend video = videoRecommends.get(i);
                VideoViewActivity.start(getActivity(), video.getVideo().getVideo_id());
            }
        });
        tjydGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtils.show(getActivity(), i + "", 0);
            }
        });

        tjypListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SosAudioRecommend audioRecommend = audioRecommends.get(i);
            }
        });

    }

    public void initView() {
        list = new ArrayList<>();
        api = HttpUtils.getInstance(getActivity())
                .getRetofitClinet()
                .builder(HomePageApi.class);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        /**
         * 绘制监听器，当滑动组件绘制成功时触发
         */
        ViewTreeObserver observer = gallery.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gallery.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                galleryHeight = gallery.getHeight();
                srcollView.setScrollViewListener(HomeFragment.this);
            }
        });
    }

    /**
     * 换一换的点击事件
     *
     * @param v
     */
    @OnClick(R.id.btn_tjsphyh)
    public void OnClickTjsphyh(View v) {
        loadHomeVideoData();
    }

    @OnClick(R.id.btn_tjydhyh)
    public void OnClickTjydhyh(View v) {
        loadHomeBookData();
    }

    @OnClick(R.id.btn_tjyphyh)
    public void OnClickTjyphyh(View v) {
        loadHomeAudioData();
    }


    /**
     * 获取轮播图数据
     */
    public void loadHomeSildData() {
        api.loadHomeSlideData()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosSlideImage>>(new SubscriberOnNextListener<BaseListResponse<SosSlideImage>>() {
                    @Override
                    public void onNext(BaseListResponse<SosSlideImage> homeSlideImageBaseResponse) {
                        list = homeSlideImageBaseResponse.getResults();

                        gallery.setAdapter(new HomeSlideAdapter(getActivity(), list));
                        gallery.setFocusable(true);
                        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                i = i % list.size();
                                titleView.setText(list.get(i).getCate_title() + "/" + list.get(i).getTitle());
                                subTitleView.setText(list.get(i).getDate());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }, getActivity()));
    }

    /**
     * 获取推荐初始数据
     */
    public void loadHomeVideoData() {

        api.loadVideoRecommend(spNextPage)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosVideoRecommend>>(new SubscriberOnNextListener<BaseListResponse<SosVideoRecommend>>() {
                    @Override
                    public void onNext(BaseListResponse<SosVideoRecommend> homeVideoRecommendBaseResponse) {
                        spNextPage = homeVideoRecommendBaseResponse.getNextPage();
                        videoRecommends = homeVideoRecommendBaseResponse.getResults();
                        sosHomeGridViewAdapter = new SosHomeGridViewAdapter(mContext, videoRecommends);
                        tjspGridView.setAdapter(sosHomeGridViewAdapter);
                    }
                }, getActivity()));

    }


    /**
     * 获取推荐书籍数据
     */
    public void loadHomeBookData() {
        api.loadBookRecommend(ydNextPage).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosBookRecommend>>(new SubscriberOnNextListener<BaseListResponse<SosBookRecommend>>() {
                    @Override
                    public void onNext(BaseListResponse<SosBookRecommend> homeBookRecommendBaseResponse) {
                        ydNextPage = homeBookRecommendBaseResponse.getNextPage();
                        bookRecommends = homeBookRecommendBaseResponse.getResults();
                        sosBookGridViewAdapter = new SosBookGridViewAdapter(mContext, bookRecommends);
                        tjydGridView.setAdapter(sosBookGridViewAdapter);
                    }
                }, getActivity()));
    }

    /**
     * 获取音频数据
     */
    public void loadHomeAudioData() {
        api.loadAudioRecommend(ypNextPage).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosAudioRecommend>>(new SubscriberOnNextListener<BaseListResponse<SosAudioRecommend>>() {
                    @Override
                    public void onNext(BaseListResponse<SosAudioRecommend> homeAudioRecommendBaseResponse) {
                        ypNextPage = homeAudioRecommendBaseResponse.getNextPage();
                        audioRecommends = homeAudioRecommendBaseResponse.getResults();
                        sostvHomeListViewAdapter = new SostvHomeListViewAdapter(mContext, audioRecommends);
                        tjypListView.setAdapter(sostvHomeListViewAdapter);
                        ListViewUtils.setListViewHeightBasedOnChildren(tjypListView);
                    }
                }, getActivity()));
    }


    @Override
    public void onScrollChanged(ReboundScrollView scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment");
        gallery.startTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        gallery.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");
        gallery.destroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gallery.destroy();
    }
}
