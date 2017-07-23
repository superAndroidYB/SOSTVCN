package com.sostvcn.ui;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.helper.CollectHelper;
import com.sostvcn.model.SosAudioList;
import com.sostvcn.model.SosCollectEntity;
import com.sostvcn.model.SosDownloadBean;
import com.sostvcn.service.DownloadService;
import com.sostvcn.service.MusicService;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.ImageUtils;
import com.sostvcn.utils.SPUtils;
import com.sostvcn.utils.ServiceUtils;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.SpecialImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMusic;

public class AudioViewActivity extends BaseActivity implements View.OnClickListener {


    @ViewInject(R.id.back_btn)
    private ImageView back_btn;
    @ViewInject(R.id.title_text_view)
    private TextView title_text_view;
    @ViewInject(R.id.subtitle_text_view)
    private TextView subtitle_text_view;
    @ViewInject(R.id.specima_image_view)
    private SpecialImageView specima_image_view;

    @ViewInject(R.id.sb_audio_view)
    private SeekBar sb_audio_view;
    @ViewInject(R.id.current_time)
    private TextView current_time;
    @ViewInject(R.id.total_time)
    private TextView total_time;

    @ViewInject(R.id.audio_collect_btn)
    private ImageView audio_collect_btn;
    @ViewInject(R.id.audio_share_btn)
    private ImageView audio_share_btn;
    @ViewInject(R.id.audio_download_btn)
    private ImageView audio_download_btn;
    @ViewInject(R.id.audio_delete_btn)
    private ImageView audio_delete_btn;

    @ViewInject(R.id.player_way_btn)
    private ImageView player_way_btn;
    @ViewInject(R.id.player_previous_btn)
    private ImageView player_previous_btn;
    @ViewInject(R.id.player_pause_btn)
    private ImageView player_pause_btn;
    @ViewInject(R.id.player_next_btn)
    private ImageView player_next_btn;
    @ViewInject(R.id.player_list_btn)
    private ImageView player_list_btn;
    @ViewInject(R.id.backgroud_view)
    private RelativeLayout backgroudView;

    private int[] playWay = new int[]{R.drawable.audio_play_list_selector,
            R.drawable.audio_play_single_selector, R.drawable.audio_playlist_randow_selector};
    private int playWayIndex;

    private SosAudioList medias;
    private int postion;
    private BitmapUtils bitmapUtils;
    private AudioViewBroadcastReceiver broadcastReceiver;// 广播
    private IntentFilter filter;// 广播过滤器

    private CollectHelper collectHelper;
    private SosCollectEntity collectEntity;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio_view;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        medias = (SosAudioList) getIntent().getSerializableExtra("medias");
        postion = getIntent().getIntExtra("postion", 0);

        if (medias.getAlbum_cover() != null || !"".equals(medias.getAlbum_cover())) {
            blurBackViewBlur();
        }

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        playWayIndex = (int) SPUtils.get(this, "playWayIndex", 0);

        initMusicView();

        // 创建一个广播
        broadcastReceiver = new AudioViewBroadcastReceiver();

        // 创建广播过滤器
        filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PlAYING_STATE);
        filter.addAction(Constants.ACTION_SERVICR_PUASE);
        filter.addAction(Constants.ACTION_MUSIC_PLAN);
        filter.addAction(Constants.ACTION_PLAY);
        registerReceiver(broadcastReceiver, filter);

        //如果音乐服务不在运行中就启动
        //如果在运行中就发送下标
        if (!ServiceUtils.isServiceRunning(AudioViewActivity.this, "com.sostvcn.service.MusicService")) {
            ToastUtils.showShort(this,"no");
            MusicService.start(AudioViewActivity.this, medias, postion);
        }
        Intent broadcast = new Intent();
        broadcast.setAction(Constants.ACTION_LIST);
        broadcast.putExtra("index", postion);
        sendBroadcast(broadcast);


        initCollectData();

        back_btn.setOnClickListener(this);
        audio_collect_btn.setOnClickListener(this);
        audio_share_btn.setOnClickListener(this);
        audio_download_btn.setOnClickListener(this);
        audio_delete_btn.setOnClickListener(this);

        player_way_btn.setOnClickListener(this);
        player_previous_btn.setOnClickListener(this);
        player_pause_btn.setOnClickListener(this);
        player_next_btn.setOnClickListener(this);
        player_list_btn.setOnClickListener(this);

        sb_audio_view.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarChange(progress);
            }
        });
    }

    private void initMusicView() {
        title_text_view.setText(medias.getVoice_list().get(postion).getTitle());
        subtitle_text_view.setText("-" + medias.getAlbum_title() + "-");
        bitmapUtils.display(specima_image_view, medias.getAlbum_cover());
        player_way_btn.setImageResource(playWay[playWayIndex]);
    }

    private void seekBarChange(int progress) {
        Intent intent = new Intent();
        intent.putExtra("index", progress);
        intent.setAction(Constants.ACTION_PLAN_CURRENT);
        sendBroadcast(intent);
    }

    private void initCollectData() {
        collectHelper = new CollectHelper(this);
        collectEntity = collectHelper.findCollect(medias.getVoice_list().get(postion).getMp3());
        showCollectStyle();
    }

    private void showCollectStyle() {
        if (collectEntity == null) {
            audio_collect_btn.setImageResource(R.mipmap.audio_play_collection);
        } else {
            audio_collect_btn.setImageResource(R.mipmap.audio_play_collection_sel);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.audio_collect_btn:
                if (collectEntity == null) {
                    collectEntity = new SosCollectEntity();
                    collectEntity.setObjId(medias.getVoice_list().get(postion).getMp3());
                    collectEntity.setObjName(medias.getVoice_list().get(postion).getTitle());
                    collectEntity.setObjDesc(medias.getAlbum_title());
                    collectEntity.setObjImage(medias.getVoice_list().get(postion).getCover());
                    collectEntity.setType(Constants.TYPE_AUDIO);
                    collectHelper.saveCollect(collectEntity);
                } else {
                    collectHelper.deleteCollect(collectEntity);
                    collectEntity = null;
                }
                showCollectStyle();
                break;
            case R.id.audio_share_btn:
                intiSharePopWindow();
                break;
            case R.id.audio_download_btn:
                SosAudioList.Voice_list audio = medias.getVoice_list().get(postion);
                SosDownloadBean bean = new SosDownloadBean(audio.getTitle(), medias.getAlbum_title(), audio.getDownload(), Constants.TYPE_AUDIO);
                DownloadService.startService(this, bean);
                break;
            case R.id.audio_delete_btn:

                break;
            case R.id.player_way_btn:
                if (playWayIndex == playWay.length - 1) {
                    playWayIndex = 0;
                } else {
                    playWayIndex++;
                }
                player_way_btn.setImageResource(playWay[playWayIndex]);
                SPUtils.put(this, "playWayIndex", playWayIndex);
                break;
            case R.id.player_previous_btn:
                if (postion == 0) {
                    postion = medias.getVoice_list().size() - 1;
                } else {
                    postion--;
                }
                intent.putExtra("index", postion);
                intent.setAction(Constants.ACTION_LAST);
                sendBroadcast(intent);
                break;
            case R.id.player_pause_btn:
                if ((int) player_pause_btn.getTag() == R.mipmap.audio_suspend) {
                    intent.putExtra("index", postion);
                    intent.setAction(Constants.ACTION_PLAY);
                    sendBroadcast(intent);

                    player_pause_btn.setImageResource(R.mipmap.audio_play);
                    player_pause_btn.setTag(R.mipmap.audio_play);
                } else {
                    intent.putExtra("index", postion);
                    intent.setAction(Constants.ACTION_PAUSE);
                    sendBroadcast(intent);

                    player_pause_btn.setImageResource(R.mipmap.audio_suspend);
                    player_pause_btn.setTag(R.mipmap.audio_suspend);
                }
                break;
            case R.id.player_next_btn:
                if (postion == medias.getVoice_list().size() - 1) {
                    postion = 0;
                } else {
                    postion++;
                }
                intent.putExtra("index", postion);
                intent.setAction(Constants.ACTION_NEXT);
                sendBroadcast(intent);
                break;
            case R.id.player_list_btn:
                showListPopWindow();
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
                actionShare(SHARE_MEDIA.ALIPAY);
                break;
            case R.id.share_copy_link_btn:
                // 从API11开始android推荐使用android.content.ClipboardManager
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                SosAudioList.Voice_list voice = medias.getVoice_list().get(postion);
                cm.setText(voice.getTitle() + "\n" + medias.getShare_url());
                Toast.makeText(this, "复制成功，可以发给朋友们啦！", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                break;
        }
    }


    public void blurBackViewBlur() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //下面的这个方法必须在子线程中执行
                final Bitmap blurBitmap2 = ImageUtils.GetUrlBitmap(medias.getAlbum_cover(), 10);

                //刷新ui必须在主线程中执行
                AudioViewActivity.this.runOnUiThread(new Runnable() {//这个是我自己封装的在主线程中刷新ui的方法。
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

    private void actionShare(SHARE_MEDIA type) {
        popupWindow.dismiss();
        UMusic wxmusic = new UMusic(medias.getVoice_list().get(postion).getMp3());
        wxmusic.setTitle(medias.getVoice_list().get(postion).getTitle());//音乐的标题
        wxmusic.setH5Url(medias.getShare_url());
        wxmusic.setThumb(new UMImage(this, medias.getVoice_list().get(postion).getCover()));//音乐的缩略图
        wxmusic.setDescription(medias.getAlbum_title());//音乐的描述

        new ShareAction(this).setPlatform(type)
                .withMedia(wxmusic)
                .setCallback(umShareListener).share();
    }

    /**
     * 弹出列表框
     */
    private void showListPopWindow() {

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

    private UMShareListener umShareListener = new UMShareListener() {

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtils.show(AudioViewActivity.this, "成功", 0);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            ToastUtils.show(AudioViewActivity.this, "失败", 0);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtils.show(AudioViewActivity.this, "取消", 0);
        }
    };

    public class AudioViewBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.ACTION_PlAYING_STATE.equals(intent.getAction())) {

                postion = intent.getIntExtra("media", 0);

                initMusicView();

                player_pause_btn.setImageResource(R.mipmap.audio_play);
                player_pause_btn.setTag(R.mipmap.audio_play);

                int totalTime = (int) intent.getLongExtra("totalTime", 0);
                total_time.setText(timeconvert(totalTime));
                sb_audio_view.setMax(totalTime);

                // 当服务开始播放音乐后，将专辑图片添加旋转动画效果
                Animation operatingAnim = AnimationUtils.loadAnimation(AudioViewActivity.this, R.anim.tip);
                LinearInterpolator lin = new LinearInterpolator();
                operatingAnim.setInterpolator(lin);
                specima_image_view.startAnimation(operatingAnim);

            } else if (Constants.ACTION_SERVICR_PUASE.equals(intent.getAction())) {
                specima_image_view.clearAnimation();
                player_pause_btn.setImageResource(R.mipmap.audio_suspend);
                player_pause_btn.setTag(R.mipmap.audio_suspend);

            } else if (Constants.ACTION_PLAY.equals(intent.getAction())) {

            } else if (Constants.ACTION_MUSIC_PLAN.equals(intent.getAction())) {
                int playerPosition = (int) intent.getLongExtra("playerPosition", 0);
                String playerTime = timeconvert(playerPosition);
                sb_audio_view.setProgress(playerPosition);
                current_time.setText(playerTime);
                sb_audio_view.invalidate();
            }
        }
    }


    /*************************************************************
     * 歌曲时间格式转换
     */
    public String timeconvert(int time) {
        int min = 0, hour = 0;
        time /= 1000;
        min = time / 60;
        time %= 60;
        return (min < 10 ? "0" + min : min) + ":" + (time < 10 ? "0" + time : time);
    }


    public static void start(Context context, SosAudioList medias, int postion) {
        Intent intent = new Intent(context, AudioViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("medias", medias);
        bundle.putInt("postion", postion);
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

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
