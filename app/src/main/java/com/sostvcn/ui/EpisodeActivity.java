package com.sostvcn.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.api.VideoPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosCateVideo;
import com.sostvcn.utils.DateUtils;
import com.sostvcn.widget.SosSpinerView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EpisodeActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.toolbar)
    private Toolbar toolbar;
    @ViewInject(R.id.back_btn)
    private ImageView backBtn;
    @ViewInject(R.id.spinerView)
    private SosSpinerView spinerView;
    @ViewInject(R.id.video_by_yearview)
    private GridView gridView;
    private List<SosCateVideo.Video> gridData;
    @ViewInject(R.id.toolbar_desc)
    private TextView gxDesc;

    private VideoPageApi api;
    private Map<String, List<SosCateVideo.Video>> map;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_episode;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        setSupportActionBar(toolbar);
        backBtn.setOnClickListener(this);

        api = HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(VideoPageApi.class);

        int id = getIntent().getIntExtra("id", 0);
        loadData(id);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("id", gridData.get(i).getVideo_id());
                setResult(2,intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
        }
    }

    private void loadData(int id) {

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

                        gxDesc.setText("第周五晚更新一期/更新至" + videoBaseListResponse.getResults().iterator().next().getDate() + "期");

                        if (isGroup) {
                            spinerView.setVisibility(View.VISIBLE);
                            setGridViewDataByYear(datas.iterator().next());
                            spinerView.setYearTextView(datas.iterator().next());

                            spinerView.setConfig(EpisodeActivity.this, datas, new SosSpinerView.OnSelectedClikcListener() {
                                @Override
                                public void onSelectedEvent(String value) {
                                    spinerView.setYearTextView(value);
                                    setGridViewDataByYear(value);
                                }
                            });
                        } else {
                            spinerView.setVisibility(View.GONE);
                            gridData = videoBaseListResponse.getResults();
                            gridView.setAdapter(new EpisodeGridAdapter());
                        }
                    }
                }, this));
    }

    private void setGridViewDataByYear(String year) {
        gridData = map.get(year);
        gridView.setAdapter(new EpisodeGridAdapter());
    }

    class EpisodeGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return gridData.size();
        }

        @Override
        public Object getItem(int i) {
            return gridData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(EpisodeActivity.this).inflate(
                        R.layout.episode_grid_item_view, null, false);
                holder.dateTextView = (TextView) view.findViewById(R.id.day_text_view);
                holder.titleTextView = (TextView) view.findViewById(R.id.title_text_view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            SosCateVideo.Video video = gridData.get(i);
            holder.dateTextView.setText(video.getDate());
            holder.titleTextView.setText(video.getTitle());
            return view;
        }
    }

    static class ViewHolder {
        TextView dateTextView;
        TextView titleTextView;
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, EpisodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
