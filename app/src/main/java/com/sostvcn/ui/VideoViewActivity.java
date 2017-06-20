package com.sostvcn.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.RecentlyVideoListViewAdapter;
import com.sostvcn.adapter.SostvCacheListAdapter;
import com.sostvcn.api.VideoPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.helper.CollectHelper;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.RecentlyVideo;
import com.sostvcn.model.SosCateVideo;
import com.sostvcn.model.SosCollectEntity;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.DateUtils;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.SosSpinerView;
import com.sostvcn.widget.SostvListView;
import com.sostvcn.widget.SostvVideoView;
import com.sostvcn.widget.VideoMediaController;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.widget.VideoView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VideoViewActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.sostv_video_view)
    private SostvVideoView videoView;

    private VideoPageApi api;
    private SosCateVideo cateVideo;

    @ViewInject(R.id.video_title_view)
    private TextView videoTitleTextView;
    @ViewInject(R.id.video_subtitle_view)
    private TextView videoSubTitleTextView;

    @ViewInject(R.id.video_cattitle_view)
    private TextView videoCatTitleTextView;
    @ViewInject(R.id.video_moretext_view)
    private TextView videoMoretextView;

    @ViewInject(R.id.hot_text_view)
    private TextView videoHotTextView;

    @ViewInject(R.id.more_btn)
    private ImageView mortTitleBtn;
    @ViewInject(R.id.video_download_btn)
    private ImageView videoDownloadBtn;
    @ViewInject(R.id.video_collection_btn)
    private ImageView videoColltionBtn;
    @ViewInject(R.id.video_share_btn)
    private ImageView videoShareBtn;
    @ViewInject(R.id.show_episode_btn)
    private LinearLayout showEpisodeBtn;
    @ViewInject(R.id.gxsm_text_view)
    private TextView gxsmTextView;

    @ViewInject(R.id.curtain_btn)
    private ImageView curtain_btn;

    @ViewInject(R.id.tjsp_listview)
    private SostvListView listView;
    @ViewInject(R.id.info_span1)
    private View info_span1;
    @ViewInject(R.id.info_span2)
    private View info_span2;

    private List<RecentlyVideo> recentlyVideoList;
    private RecentlyVideoListViewAdapter recentlyVideoListViewAdapter;

    private PopupWindow popupWindow;
    private CollectHelper collectHelper;
    private SosCollectEntity collectEntity;

    private int id;
    private boolean isCate;

    //记录是否全屏
    private boolean isFullScreen = false;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_small_video_view;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        videoView.setVideoPlayCallback(new SostvVideoPlayCallback());
        api = HttpUtils.getInstance(this)
                .getRetofitClinet()
                .builder(VideoPageApi.class);
        mortTitleBtn.setOnClickListener(this);
        videoDownloadBtn.setOnClickListener(this);
        videoColltionBtn.setOnClickListener(this);
        videoShareBtn.setOnClickListener(this);
        showEpisodeBtn.setOnClickListener(this);
        curtain_btn.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (recentlyVideoList != null) {
                    RecentlyVideo video = recentlyVideoList.get(i);
                    loadVideoInfo(video.getVideo_id(), false);
                }
            }
        });

        collectHelper = new CollectHelper(this);
        id = getIntent().getIntExtra("id", 0);
        isCate = getIntent().getBooleanExtra("isCate", false);
        if (isCate) {
            videoView.showCurtain();
            info_span1.setVisibility(View.GONE);
            info_span2.setVisibility(View.GONE);
            loadRecentlyVideo(id);
        } else {
            loadVideoInfo(id, true);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            id = data.getIntExtra("id", 0);
            loadVideoInfo(id, false);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_btn:
                if (videoCatTitleTextView.getVisibility() == View.GONE) {
                    mortTitleBtn.setImageResource(R.mipmap.video_intro_arrow_up);
                    videoCatTitleTextView.setVisibility(View.VISIBLE);
                    videoMoretextView.setVisibility(View.VISIBLE);
                } else {
                    mortTitleBtn.setImageResource(R.mipmap.video_intro_arrow_down);
                    videoCatTitleTextView.setVisibility(View.GONE);
                    videoMoretextView.setVisibility(View.GONE);
                }
                break;

            /**
             * 弹出框按钮
             */
            case R.id.video_download_btn:
                initDownloadPopWindow();
                break;
            case R.id.video_share_btn:
                intiSharePopWindow();
                break;

            /**
             * 选集按钮
             */
            case R.id.show_episode_btn:
                if (!recentlyVideoList.isEmpty()) {
                    videoView.pausePlay();
                    Intent intent = new Intent(this, EpisodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", recentlyVideoList.iterator().next().getCate_id());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                    this.overridePendingTransition(R.anim.episode_activity_silde_in,R.anim.episode_activity_silde_out);
                }
                break;
            /**
             * 收藏按钮
             */
            case R.id.video_collection_btn:
                actionCollectVideo();
                break;
            /**
             * 选择要下载的视频类型
             */
            case R.id.download_hd:
                popupWindow.dismiss();
                ToastUtils.show(this, "高清", 0);
                break;
            case R.id.download_sd:
                popupWindow.dismiss();
                ToastUtils.show(this, "标清", 0);
                break;

            /**
             * 分享按钮
             */
            case R.id.share_qq_btn:
                popupWindow.dismiss();
                UMVideo umVideoQQ = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoQQ.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoQQ.setTitle(cateVideo.getVideo().getTitle());
                umVideoQQ.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.QQ)
                        .withMedia(umVideoQQ)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_qq_zone_btn:
                popupWindow.dismiss();
                UMVideo umVideoQZONE = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoQZONE.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoQZONE.setTitle(cateVideo.getVideo().getTitle());
                umVideoQZONE.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.QZONE)
                        .withMedia(umVideoQZONE)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_weixin_btn:
                popupWindow.dismiss();
                UMVideo umVideoWEIXIN = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoWEIXIN.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoWEIXIN.setTitle(cateVideo.getVideo().getTitle());
                umVideoWEIXIN.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN)
                        .withMedia(umVideoWEIXIN)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_weixin_quan_btn:
                popupWindow.dismiss();
                UMVideo umVideoWEIXIN_CIRCLE = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoWEIXIN_CIRCLE.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoWEIXIN_CIRCLE.setTitle(cateVideo.getVideo().getTitle());
                umVideoWEIXIN_CIRCLE.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(umVideoWEIXIN_CIRCLE)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_weibo_btn:
                popupWindow.dismiss();
                UMVideo umVideoSINA = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoSINA.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoSINA.setTitle(cateVideo.getVideo().getTitle());
                umVideoSINA.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.SINA)
                        .withMedia(umVideoSINA)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_alipay_btn:
                popupWindow.dismiss();
                UMVideo umVideoALIPAY = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                umVideoALIPAY.setH5Url(cateVideo.getVideo().getShare_url());
                umVideoALIPAY.setTitle(cateVideo.getVideo().getTitle());
                umVideoALIPAY.setThumb(new UMImage(this,cateVideo.getVideo().getImage()));
                new ShareAction(this).setPlatform(SHARE_MEDIA.SINA)
                        .withMedia(umVideoALIPAY)
                        .setCallback(umShareListener).share();
                break;
            case R.id.share_copy_link_btn :
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(cateVideo.getVideo().getTitle() + "\n" + cateVideo.getVideo().getShare_url());
                Toast.makeText(this, "复制成功，可以发给朋友们啦！", Toast.LENGTH_LONG).show();
                break;
            case R.id.curtain_btn:
                if(!recentlyVideoList.isEmpty()){
                    loadVideoInfo(recentlyVideoList.iterator().next().getVideo_id(), false);
                }
                break;
        }
    }


    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtils.show(VideoViewActivity.this, "成功", 0);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.show(VideoViewActivity.this, "失败", 0);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtils.show(VideoViewActivity.this, "取消", 0);
        }
    };

    private void actionCollectVideo() {
        if (collectEntity == null) {
            collectEntity = new SosCollectEntity();
            collectEntity.setObjId(cateVideo.getVideo().getVideo_id());
            collectEntity.setObjName(cateVideo.getVideo().getTitle());
            collectEntity.setObjDesc(cateVideo.getCate().getCate_title());
            collectEntity.setObjImage(cateVideo.getVideo().getImage());
            collectEntity.setType(Constants.TYPE_VIDEO);
            collectHelper.saveCollect(collectEntity);
        } else {
            collectHelper.deleteCollect(collectEntity);
            collectEntity = null;
        }
        showCollectStyle();
    }

    private void showCollectStyle() {
        if (collectEntity == null) {
            videoColltionBtn.setImageResource(R.mipmap.video_collection);
        } else {
            videoColltionBtn.setImageResource(R.mipmap.video_collection_sel);
        }
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

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_small_video_view, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 弹出下载框
     */
    private void initDownloadPopWindow() {
        View popupWindowView = getLayoutInflater().inflate(R.layout.dowload_menu_layout, null);
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

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_small_video_view, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        final TextView hd = (TextView) popupWindowView.findViewById(R.id.download_hd);
        TextView sd = (TextView) popupWindowView.findViewById(R.id.download_sd);
        hd.setOnClickListener(this);
        sd.setOnClickListener(this);
    }


    /**
     * 全屏播放时弹出分享界面
     */
    public void showShareListPopWindeow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.video_full_share_view, null);
        final PopupWindow popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

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
        popupWindow.setAnimationStyle(R.style.VideoPopViewRight);
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.sostv_video_view, null), Gravity.RIGHT, 0, 500);

        final int[] icons = new int[]{R.mipmap.videofull_share_qq,R.mipmap.videofull_share_qq_zone,R.mipmap.videofull_share_weixin,
        R.mipmap.videofull_share_weixin_quan,R.mipmap.videofull_share_weibo,R.mipmap.share_alipay,R.mipmap.videofull_share_copy_link};
        final String[] titles = new String[]{"QQ","QQ空间","微信","微信朋友圈","微博","支付宝好友","复制链接"};
        GridView shareList = (GridView) popupWindowView.findViewById(R.id.videofull_share_list);
        shareList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return icons.length;
            }

            @Override
            public Object getItem(int i) {
                return icons[i];
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view == null){
                    view = getLayoutInflater().inflate(R.layout.video_full_share_list_item,null);
                }
                ImageView imageView = (ImageView)view.findViewById(R.id.icon);
                TextView textView = (TextView)view.findViewById(R.id.title);
                imageView.setImageResource(icons[i]);
                textView.setText(titles[i]);
                return view;
            }
        });

        shareList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        popupWindow.dismiss();
                        UMVideo umVideoQQ = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoQQ.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoQQ.setTitle(cateVideo.getVideo().getTitle());
                        umVideoQQ.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.QQ)
                                .withMedia(umVideoQQ)
                                .setCallback(umShareListener).share();
                        break;
                    case 1:
                        popupWindow.dismiss();
                        UMVideo umVideoQZONE = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoQZONE.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoQZONE.setTitle(cateVideo.getVideo().getTitle());
                        umVideoQZONE.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.QZONE)
                                .withMedia(umVideoQZONE)
                                .setCallback(umShareListener).share();
                        break;
                    case 2:
                        popupWindow.dismiss();
                        UMVideo umVideoWEIXIN = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoWEIXIN.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoWEIXIN.setTitle(cateVideo.getVideo().getTitle());
                        umVideoWEIXIN.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN)
                                .withMedia(umVideoWEIXIN)
                                .setCallback(umShareListener).share();
                        break;
                    case 3:
                        popupWindow.dismiss();
                        UMVideo umVideoWEIXIN_CIRCLE = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoWEIXIN_CIRCLE.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoWEIXIN_CIRCLE.setTitle(cateVideo.getVideo().getTitle());
                        umVideoWEIXIN_CIRCLE.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                .withMedia(umVideoWEIXIN_CIRCLE)
                                .setCallback(umShareListener).share();
                        break;
                    case 4:
                        popupWindow.dismiss();
                        UMVideo umVideoSINA = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoSINA.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoSINA.setTitle(cateVideo.getVideo().getTitle());
                        umVideoSINA.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.SINA)
                                .withMedia(umVideoSINA)
                                .setCallback(umShareListener).share();
                        break;
                    case 5:
                        popupWindow.dismiss();
                        UMVideo umVideoALIPAY = new UMVideo(cateVideo.getVideo().getVideo_url_sd());
                        umVideoALIPAY.setH5Url(cateVideo.getVideo().getShare_url());
                        umVideoALIPAY.setTitle(cateVideo.getVideo().getTitle());
                        umVideoALIPAY.setThumb(new UMImage(VideoViewActivity.this,cateVideo.getVideo().getImage()));
                        new ShareAction(VideoViewActivity.this).setPlatform(SHARE_MEDIA.SINA)
                                .withMedia(umVideoALIPAY)
                                .setCallback(umShareListener).share();
                        break;
                    case 6:
                        // 从API11开始android推荐使用android.content.ClipboardManager
                        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        // 将文本内容放到系统剪贴板里。
                        cm.setText(cateVideo.getVideo().getTitle() + "\n" + cateVideo.getVideo().getShare_url());
                        Toast.makeText(VideoViewActivity.this, "复制成功，可以发给朋友们啦！", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    /**
     * 弹出操作列表
     */
    public void showCacheListPopWindow(int id) {
        TextView all_cache_btn;

        View popupWindowView = getLayoutInflater().inflate(R.layout.video_optionlist_view, null);
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
        popupWindow.setAnimationStyle(R.style.VideoPopViewRight);

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.sostv_video_view, null), Gravity.RIGHT, 0, 500);
        final ListView cacheListView = (ListView) popupWindowView.findViewById(R.id.video_cache_list_view);
        final SosSpinerView qxdSelector = (SosSpinerView) popupWindowView.findViewById(R.id.shdh_selector);

        final SosSpinerView yearSelector = (SosSpinerView) popupWindowView.findViewById(R.id.year_selector);
        all_cache_btn = (TextView) popupWindowView.findViewById(R.id.all_cache_btn);

        List<String> qxdSelectorDatas = new ArrayList<>();
        qxdSelectorDatas.add("高清");
        qxdSelectorDatas.add("标清");
        qxdSelector.setYearTextView("高清");
        qxdSelector.setConfig(this, qxdSelectorDatas, new SosSpinerView.OnSelectedClikcListener() {
            @Override
            public void onSelectedEvent(String value) {
                qxdSelector.setYearTextView(value);
            }
        }, getLayoutId());


        api.loadAllVideo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<SosCateVideo.Video>>(new SubscriberOnNextListener<BaseListResponse<SosCateVideo.Video>>() {
                    @Override
                    public void onNext(BaseListResponse<SosCateVideo.Video> videoBaseListResponse) {
                        final Map<String, List<SosCateVideo.Video>> map = new HashMap<>();
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

                            SostvCacheListAdapter adapter = new SostvCacheListAdapter(VideoViewActivity.this, map.get(datas.iterator().next()));
                            cacheListView.setAdapter(adapter);

                            yearSelector.setConfig(VideoViewActivity.this, datas, new SosSpinerView.OnSelectedClikcListener() {
                                @Override
                                public void onSelectedEvent(String value) {
                                    yearSelector.setYearTextView(value);
                                    SostvCacheListAdapter adapter = new SostvCacheListAdapter(VideoViewActivity.this, map.get(value));
                                    cacheListView.setAdapter(adapter);
                                }
                            },getLayoutId());
                        } else {
                            yearSelector.setVisibility(View.GONE);
                            SostvCacheListAdapter adapter = new SostvCacheListAdapter(VideoViewActivity.this, videoBaseListResponse.getResults());
                            cacheListView.setAdapter(adapter);
                        }
                    }
                }, this));
    }

    /**
     * 解析intent传过来的videoid并加载详细数据
     */
    private void loadVideoInfo(int id, final boolean loadRecnet) {
        collectEntity = collectHelper.findCollect(id);
        showCollectStyle();
        api.loadVideoInfo(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosCateVideo>>(new SubscriberOnNextListener<BaseObjectResponse<SosCateVideo>>() {
                    @Override
                    public void onNext(BaseObjectResponse<SosCateVideo> sosCateVideoBaseObjectResponse) {
                        cateVideo = sosCateVideoBaseObjectResponse.getResults();
                        if (cateVideo == null) {
                            ToastUtils.show(VideoViewActivity.this, "没有找到数据", Toast.LENGTH_SHORT);
                            VideoViewActivity.this.finish();
                        }
                        info_span1.setVisibility(View.VISIBLE);
                        info_span2.setVisibility(View.VISIBLE);

                        videoView.startPlay(cateVideo.getVideo().getVideo_url_sd());
                        videoView.setSosCateVideo(cateVideo);
                        videoTitleTextView.setText(cateVideo.getVideo().getTitle());
                        videoSubTitleTextView.setText("更新：第周五播出1期");

                        videoCatTitleTextView.setText("节目：" + cateVideo.getCate().getCate_title());
                        videoMoretextView.setText("简介：" + cateVideo.getCate().getIntro());
                        videoHotTextView.setText(cateVideo.getVideo().getHits() + "次播放");
                        gxsmTextView.setText("更新至" + cateVideo.getCate().getUpdateTo() + "期 | 全部");
                        if (loadRecnet) {
                            loadRecentlyVideo(cateVideo.getCate().getCate_id());
                        }


                    }
                }, this));
    }

    private void loadRecentlyVideo(int cateId) {
        api.loadRecentlyVideo(cateId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseListResponse<RecentlyVideo>>(new SubscriberOnNextListener<BaseListResponse<RecentlyVideo>>() {
                    @Override
                    public void onNext(BaseListResponse<RecentlyVideo> recentlyVideoBaseListResponse) {
                        recentlyVideoList = recentlyVideoBaseListResponse.getResults();
                        recentlyVideoListViewAdapter = new RecentlyVideoListViewAdapter(VideoViewActivity.this, recentlyVideoList);
                        listView.setAdapter(recentlyVideoListViewAdapter);
                    }
                }, this));
    }

    class SostvVideoPlayCallback implements SostvVideoView.VideoPlayCallbackImpl {

        @Override
        public void onCloseVideo() {
            exit();
        }

        @Override
        public void onSwitchPageType() {
            if (isFullScreen) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        @Override
        public void onPlayFinish() {
            VideoViewActivity.this.finish();
        }

        @Override
        public void showCacheListView() {
            showCacheListPopWindow(cateVideo.getCate().getCate_id());
        }

        @Override
        public void showShareMenuView() {
            showShareListPopWindeow();
        }
    }

    private void exit() {
        if (isFullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            VideoViewActivity.this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && isFullScreen) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isFullScreen = true;
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //调整mFlVideoGroup布局参数
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            videoView.setLayoutParams(params);
            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
            videoView.setPageType(VideoMediaController.PageType.EXPAND);
        } else {
            isFullScreen = false;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.y420));
            videoView.setLayoutParams(params);
            videoView.setPageType(VideoMediaController.PageType.SHRINK);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        UMShareAPI.get(this).release();
    }

    public static void start(Context context, int id) {
        Intent intent = new Intent(context, VideoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void start(Context context, int id, boolean isCate) {
        Intent intent = new Intent(context, VideoViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putBoolean("isCate", isCate);
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
        videoView.pausePlay();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoView.startPlayVideo();
    }
}
