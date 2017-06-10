package com.sostvcn.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.AudioListViewAdapter;
import com.sostvcn.api.AudioPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.helper.CollectHelper;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosAudioList;
import com.sostvcn.model.SosCollectEntity;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.ImageUtils;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.ReboundScrollView;
import com.sostvcn.widget.SostvListView;
import com.umeng.analytics.MobclickAgent;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SpecialActivity extends BaseActivity implements ReboundScrollView.ReboundScrollListener, View.OnClickListener {

    private AudioPageApi api;


    @ViewInject(R.id.toolbar_view)
    private RelativeLayout toolBalView;
    @ViewInject(R.id.toolbar_title)
    private TextView toolBalTitle;
    @ViewInject(R.id.toolbar_player)
    private ImageView toolbarPlayer;
    @ViewInject(R.id.toolbar_back)
    private ImageView toolbarBack;

    @ViewInject(R.id.backgroud_view)
    private RelativeLayout backgroudView;
    @ViewInject(R.id.special_image)
    private ImageView specialImage;
    @ViewInject(R.id.special_title)
    private TextView specialTitle;
    @ViewInject(R.id.special_author)
    private TextView specialAuthor;
    @ViewInject(R.id.special_from)
    private TextView specialFrom;

    @ViewInject(R.id.root_view)
    private ReboundScrollView rootView;
    @ViewInject(R.id.audio_play_btn)
    private TextView audioPlayBtn;
    @ViewInject(R.id.audio_collect_btn)
    private TextView audioCollectBtn;
    @ViewInject(R.id.audio_share_btn)
    private TextView audioShareBtn;
    @ViewInject(R.id.audio_multiselect_btn)
    private TextView audioMultiselectBtn;

    @ViewInject(R.id.count_text_view)
    private TextView countView;
    @ViewInject(R.id.audio_list_view)
    private SostvListView listView;

    private SosAudioList audioList;
    private AudioListViewAdapter listViewAdapter;
    private BitmapUtils bitmapUtils;

    private int cateId;
    private int playlistId;
    private int height;

    private int postion;

    private SosCollectEntity collectEntity;
    private CollectHelper collectHelper;

    private AudioViewBroadcastReceiver broadcastReceiver;// 广播
    private IntentFilter filter;// 广播过滤器

    @Override
    protected int getLayoutId() {
        return R.layout.activity_special;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        rootView.setScrollViewListener(this);
        collectHelper = new CollectHelper(this);

        audioPlayBtn.setOnClickListener(this);
        audioCollectBtn.setOnClickListener(this);
        audioShareBtn.setOnClickListener(this);
        audioMultiselectBtn.setOnClickListener(this);

        toolbarPlayer.setOnClickListener(this);
        toolbarBack.setOnClickListener(this);

        listView.addHeaderView(new ViewStub(this));
        listView.addFooterView(new ViewStub(this));

        api = HttpUtils.getInstance(this).getRetofitClinet().builder(AudioPageApi.class);

        cateId = getIntent().getIntExtra("cateId", 0);
        playlistId = getIntent().getIntExtra("playlistId", 0);

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.video_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.video_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        height = (int) getResources().getDimension(R.dimen.y450);

        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                postion = i - 1;
                AudioViewActivity.start(SpecialActivity.this, audioList, postion);
            }
        });
    }


    private void loadData() {
        api.loadAudioList(cateId, playlistId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosAudioList>>(new SubscriberOnNextListener<BaseObjectResponse<SosAudioList>>() {
                    @Override
                    public void onNext(BaseObjectResponse<SosAudioList> sosAudioListBaseListResponse) {
                        audioList = sosAudioListBaseListResponse.getResults();

                        if (audioList.getAlbum_cover() != null || !"".equals(audioList.getAlbum_cover())) {
                            blurBackViewBlur();
                        }

                        countView.setText("/共" + audioList.getVoice_list().size() + "讲");
                        listViewAdapter = new AudioListViewAdapter(SpecialActivity.this, audioList.getVoice_list());
                        listView.setAdapter(listViewAdapter);
                        rootView.smoothScrollTo(0, 0);

                        bitmapUtils.display(specialImage, audioList.getAlbum_cover());
                        specialTitle.setText(audioList.getAlbum_title());
                        specialAuthor.setText("主讲 " + audioList.getAlbum_artist());
                        specialFrom.setText("来自 " + audioList.getAlbum_source());


                        collectEntity = collectHelper.findCollect(audioList.getAlbum_id());
                        showCollectStyle();
                    }
                }, this));
    }

    public void blurBackViewBlur() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //下面的这个方法必须在子线程中执行
                final Bitmap blurBitmap2 = ImageUtils.GetUrlBitmap(audioList.getAlbum_cover(), 10);

                //刷新ui必须在主线程中执行
                SpecialActivity.this.runOnUiThread(new Runnable() {//这个是我自己封装的在主线程中刷新ui的方法。
                    @Override
                    public void run() {
                        if (blurBitmap2 != null) {
                            backgroudView.setBackgroundDrawable(new BitmapDrawable(blurBitmap2));
                        }
                    }
                });
            }
        }).start();
    }

    private void actionCollectAudio() {
        if (collectEntity == null) {
            collectEntity = new SosCollectEntity();
            collectEntity.setObjId(audioList.getAlbum_id());
            collectEntity.setObjName(audioList.getAlbum_title());
            collectEntity.setObjDesc(audioList.getAlbum_source());
            collectEntity.setObjImage(audioList.getAlbum_cover());
            collectEntity.setType(Constants.TYPE_AUDIO);
            collectHelper.saveCollect(collectEntity);
        } else {
            collectHelper.deleteCollect(collectEntity);
            collectEntity = null;
        }
        showCollectStyle();
    }

    private void showCollectStyle() {
        if (collectEntity == null) {
            Drawable drawable = getResources().getDrawable(R.mipmap.audio_album_collection);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            audioCollectBtn.setCompoundDrawables(null, drawable, null, null);
            audioCollectBtn.setText(R.string.audio_title2);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.audio_album_collection_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            audioCollectBtn.setCompoundDrawables(null, drawable, null, null);
            audioCollectBtn.setText(R.string.audio_title2_2);
        }
    }

    @Override
    public void onScrollChanged(ReboundScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (y <= 0) {   //设置标题的背景颜色
            toolBalView.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
            toolBalTitle.setTextColor(Color.argb((int) 255, 255, 255, 255));
            toolBalTitle.setText("专辑");
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            toolBalTitle.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            toolBalTitle.setText(audioList.getAlbum_title());
            toolBalView.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        } else {    //滑动到banner下面设置普通颜色
            toolBalView.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.audio_play_btn:
                ToastUtils.show(this, "播放", 0);
                break;
            case R.id.audio_collect_btn:
                actionCollectAudio();
                break;
            case R.id.audio_share_btn:
                break;


            case R.id.audio_multiselect_btn:
                break;
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_player:
                AudioViewActivity.start(SpecialActivity.this, audioList, postion);
                break;
        }
    }


    public class AudioViewBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }


    public static void start(Context context, int cateId, int playlistId) {
        Intent intent = new Intent(context, SpecialActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("cateId", cateId);
        bundle.putInt("playlistId", playlistId);
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
