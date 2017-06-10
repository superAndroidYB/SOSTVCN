package com.sostvcn.helper;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.sostvcn.R;
import com.sostvcn.adapter.SostvCacheListAdapter;
import com.sostvcn.api.VideoPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosCateVideo;
import com.sostvcn.utils.DateUtils;
import com.sostvcn.widget.SosSpinerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yubin on 2017/5/6.
 * 视频下载的帮助类
 */
public class DownLoadHelper {

    private static ListView cacheListView;
    private static SosSpinerView qxdSelector;
    private static SosSpinerView yearSelector;
    private static Map<String, List<SosCateVideo.Video>> map;
    /**
     * 弹出下载框
     */
    public static void showCacheListPopWindow(final Activity activity, VideoPageApi api, int id) {
        View popupWindowView = activity.getLayoutInflater().inflate(R.layout.video_optionlist_view, null);
        PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        popupWindow.showAtLocation(activity.getLayoutInflater().inflate(R.layout.sostv_video_view, null), Gravity.RIGHT, 0, 500);
        cacheListView = (ListView) popupWindowView.findViewById(R.id.video_cache_list_view);
        qxdSelector = (SosSpinerView) popupWindowView.findViewById(R.id.shdh_selector);
        yearSelector = (SosSpinerView) popupWindowView.findViewById(R.id.year_selector);
        List<String> qxdSelectorDatas = new ArrayList<>();
        qxdSelectorDatas.add("高清");
        qxdSelectorDatas.add("标清");
        qxdSelector.setYearTextView("高清");
        qxdSelector.setConfig(activity, qxdSelectorDatas, new SosSpinerView.OnSelectedClikcListener() {
            @Override
            public void onSelectedEvent(String value) {
                qxdSelector.setYearTextView(value);
            }
        });


        api.loadAllVideo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosCateVideo.Video>>(new SubscriberOnNextListener<BaseListResponse<SosCateVideo.Video>>() {
                    @Override
                    public void onNext(BaseListResponse<SosCateVideo.Video> videoBaseListResponse) {
                        map = new HashMap<>();
                        boolean isGroup = true;
                        for (SosCateVideo.Video video : videoBaseListResponse.getResults()) {
                            String year;
                            if (!DateUtils.isValidDate(video.getDate()) || videoBaseListResponse.getResults().size() <= 50) {
                                isGroup = false;
                                break;
                            }
                            if (video.getDate().length() > 4) {
                                year = video.getDate().substring(0, 4) + "年";
                            } else {
                                year = video.getTitle() + "-" + video.getDate();
                            }
                            if (map.get(year) != null) {
                                map.get(year).add(video);
                                map.put(year, map.get(year));
                            } else {
                                List<SosCateVideo.Video> videos = new ArrayList<SosCateVideo.Video>();
                                videos.add(video);
                                map.put(year, videos);
                            }
                        }

                        List<String> temp = new ArrayList<>();
                        temp.addAll(map.keySet());
                        Collections.sort(temp);
                        List<String> datas = new ArrayList<>();
                        for (int i = temp.size() - 1; i >= 0; i--) {
                            datas.add(temp.get(i));
                        }


                        if (isGroup) {
                            yearSelector.setVisibility(View.VISIBLE);
                            yearSelector.setYearTextView(datas.iterator().next());

                            yearSelector.setConfig(activity, datas, new SosSpinerView.OnSelectedClikcListener() {
                                @Override
                                public void onSelectedEvent(String value) {
                                    yearSelector.setYearTextView(value);
                                    SostvCacheListAdapter adapter = new SostvCacheListAdapter(activity, map.get(value));
                                    cacheListView.setAdapter(adapter);
                                }
                            });
                        } else {
                            yearSelector.setVisibility(View.GONE);
                            SostvCacheListAdapter adapter = new SostvCacheListAdapter(activity,videoBaseListResponse.getResults());
                            cacheListView.setAdapter(adapter);
                        }
                    }
                }, activity));
    }
}
