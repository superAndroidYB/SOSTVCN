package com.sostvcn.ui;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

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

    private SpecialViewBroadcastReceiver broadcastReceiver;// 广播
    private IntentFilter filter;// 广播过滤器

    private PopupWindow popupWindow;

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

        // 创建一个广播
        broadcastReceiver = new SpecialViewBroadcastReceiver();

        // 创建广播过滤器
        filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PlAYING_STATE);
        filter.addAction(Constants.ACTION_SERVICR_PUASE);
        filter.addAction(Constants.ACTION_MUSIC_PLAN);
        filter.addAction(Constants.ACTION_PLAY);
        registerReceiver(broadcastReceiver, filter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                postion = i - 1;
                listViewAdapter.setCurrentMp3(audioList.getVoice_list().get(postion).getMp3());
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
                AudioViewActivity.start(SpecialActivity.this, audioList, 0);
                break;
            case R.id.audio_collect_btn:
                actionCollectAudio();
                break;
            case R.id.audio_share_btn:
                intiSharePopWindow();
                break;


            case R.id.audio_multiselect_btn:
                startMultiselectActivity();
                break;
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_player:
                AudioViewActivity.start(SpecialActivity.this, audioList, postion);
                break;


            /**
             * 分享按钮
             */
            case R.id.share_qq_btn:
                actionShare(SHARE_MEDIA.QQ);
                break;
            case R.id.share_qq_zone_btn:
                actionShare(SHARE_MEDIA.QZONE);
                break;
            case R.id.share_weixin_btn:
                actionShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_weixin_quan_btn:
                actionShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.share_weibo_btn:
                actionShare(SHARE_MEDIA.SINA);
                break;
            case R.id.share_alipay_btn:
                popupWindow.dismiss();
                actionShare(SHARE_MEDIA.ALIPAY);
                break;
            case R.id.share_copy_link_btn:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(audioList.getAlbum_title() + "\n" + audioList.getShare_url());
                Toast.makeText(this, "复制成功，可以发给朋友们啦！", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                break;
        }
    }

    private void startMultiselectActivity() {
        Intent intent = new Intent(this, MultiselectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("audioList", audioList);
        intent.putExtras(bundle);
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.episode_activity_silde_in, R.anim.episode_activity_silde_out);
    }

    private void actionShare(SHARE_MEDIA type) {
        popupWindow.dismiss();
        UMWeb web = new UMWeb(audioList.getShare_url());
        web.setTitle(audioList.getAlbum_title());//标题
        web.setThumb(new UMImage(this, audioList.getAlbum_cover()));  //缩略图
        web.setDescription(audioList.getAlbum_title());//描述

        new ShareAction(this).setPlatform(type)
                .withMedia(web)
                .setCallback(umShareListener).share();
    }

    /**
     * 弹出分享框
     */
    private void intiSharePopWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.share_menu_layout, null);
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        LinearLayout share_qq_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_qq_btn);
        LinearLayout share_qq_zone_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_qq_zone_btn);
        LinearLayout share_weixin_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_weixin_btn);
        LinearLayout share_weixin_quan_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_weixin_quan_btn);
        LinearLayout share_weibo_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_weibo_btn);
        LinearLayout share_alipay_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_alipay_btn);
        LinearLayout share_copy_link_btn = (LinearLayout) popupWindowView.findViewById(R.id.share_copy_link_btn);

        share_qq_btn.setOnClickListener(this);
        share_qq_zone_btn.setOnClickListener(this);
        share_weixin_btn.setOnClickListener(this);
        share_weixin_quan_btn.setOnClickListener(this);
        share_weibo_btn.setOnClickListener(this);
        share_alipay_btn.setOnClickListener(this);
        share_copy_link_btn.setOnClickListener(this);

        popupWindow.showAtLocation(getLayoutInflater().inflate(getLayoutId(), null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    public class SpecialViewBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_PlAYING_STATE.equals(intent.getAction())) {
                int current = intent.getIntExtra("media", 0);
                listViewAdapter.setCurrentMp3(audioList.getVoice_list().get(current).getMp3());
            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtils.show(SpecialActivity.this, "成功", 0);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.show(SpecialActivity.this, "失败", 0);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtils.show(SpecialActivity.this, "取消", 0);
        }
    };
}
