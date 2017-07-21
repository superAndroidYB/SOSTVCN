package com.sostvcn.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sostvcn.R;
import com.sostvcn.model.SosCateVideo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/18.
 */
public class VideoMediaController extends FrameLayout implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private Context context;
    private ImageView mPlayImg;// 播放按钮
    private SeekBar mProgressSeekBar;// 播放进度条
    private TextView currentTimeTxt;// 播放时间
    private TextView totalTimeTxt;// 播放时间
    private TextView mExpandImg;// 清晰度切换按钮
    private ImageView mShrinkImg;// 缩放播放按钮
    private ImageView nextImageView; //下一个
    private TextView titleView;//视频标题
    private ImageView backBtn;//退回

    private ImageView downloadBtn;
    private ImageView shareBtn;

    private MediaControlImpl mMediaControl;
    private SosCateVideo cateVideo;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean isFromUser) {
        if (isFromUser)
            mMediaControl.onProgressTurn(ProgressState.DOING, progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.START, 0);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMediaControl.onProgressTurn(ProgressState.STOP, 0);
    }

    public void setSosCateVideo(SosCateVideo cateVideo) {
        this.cateVideo = cateVideo;
        titleView.setText(cateVideo.getVideo().getTitle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pause:
                mMediaControl.onPlayTurn();
                break;
            case R.id.expand:
                mMediaControl.showSharpnessView();
                break;
            case R.id.shrink:
                mMediaControl.onPageTurn();
                break;
            case R.id.vide_back_btn:
                mMediaControl.backOnEvent();
                break;
            case R.id.full_download_btn:
                mMediaControl.showCacheListView();
                break;
            case R.id.full_share_btn:
                mMediaControl.showShareMenuView();
                break;
        }
    }

    public void setProgressBar(int progress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        mProgressSeekBar.setProgress(progress);
    }

    public void setProgressBar(int progress, int secondProgress) {
        if (progress < 0)
            progress = 0;
        if (progress > 100)
            progress = 100;
        if (secondProgress < 0)
            secondProgress = 0;
        if (secondProgress > 100)
            secondProgress = 100;
        mProgressSeekBar.setProgress(progress);
        mProgressSeekBar.setSecondaryProgress(secondProgress);
    }

    public void setPlayState(PlayState playState) {
        mPlayImg.setImageResource(playState.equals(PlayState.PLAY) ? R.mipmap.video_control_pause
                : R.mipmap.video_control_play);
    }

    public void setPageType(PageType pageType) {
        mExpandImg.setVisibility(pageType.equals(PageType.EXPAND) ? VISIBLE : GONE);
        mShrinkImg.setVisibility(pageType.equals(PageType.SHRINK) ? VISIBLE : GONE);
        nextImageView.setVisibility(pageType.equals(PageType.EXPAND) ? VISIBLE : GONE);
        titleView.setVisibility(pageType.equals(PageType.EXPAND) ? VISIBLE : GONE);
        downloadBtn.setVisibility(pageType.equals(PageType.EXPAND) ? VISIBLE : GONE);
        shareBtn.setVisibility(pageType.equals(PageType.EXPAND) ? VISIBLE : GONE);
    }


    public void setPlayProgressTxt(int nowSecond, int allSecond) {
        currentTimeTxt.setText(getPlayTime(nowSecond));
        totalTimeTxt.setText(getPlayTime(allSecond));
    }

    public void playFinish(int allTime) {
        mProgressSeekBar.setProgress(0);
        setPlayProgressTxt(0, allTime);
        setPlayState(PlayState.PAUSE);
    }

    public void setMediaControl(MediaControlImpl mediaControl) {
        mMediaControl = mediaControl;
    }

    public VideoMediaController(Context context) {
        super(context);
        initView(context);
    }

    public VideoMediaController(Context context, AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public VideoMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {
        View.inflate(context, R.layout.small_video_media_controller, this);
        mPlayImg = (ImageView) findViewById(R.id.pause);
        mProgressSeekBar = (SeekBar) findViewById(R.id.media_controller_progress);
        currentTimeTxt = (TextView) findViewById(R.id.current_time);
        totalTimeTxt = (TextView) findViewById(R.id.total_time);
        mExpandImg = (TextView) findViewById(R.id.expand);
        mShrinkImg = (ImageView) findViewById(R.id.shrink);
        nextImageView = (ImageView) findViewById(R.id.next);
        titleView = (TextView) findViewById(R.id.title_text_view);
        backBtn = (ImageView) findViewById(R.id.vide_back_btn);
        downloadBtn = (ImageView) findViewById(R.id.full_download_btn);
        shareBtn = (ImageView) findViewById(R.id.full_share_btn);
        initData();
    }

    private void initData() {
        mProgressSeekBar.setOnSeekBarChangeListener(this);
        mPlayImg.setOnClickListener(this);
        mShrinkImg.setOnClickListener(this);
        mExpandImg.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        setPageType(PageType.SHRINK);
        setPlayState(PlayState.PAUSE);
    }

    @SuppressLint("SimpleDateFormat")
    private String formatPlayTime(long time) {
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(new Date(time));
    }

    private String getPlayTime(int second) {
        String playSecondStr = "00:00";
        if (second > 0) {
            playSecondStr = formatPlayTime(second);
        }

        return playSecondStr;
    }

    /**
     * 播放样式 展开、缩放
     */
    public enum PageType {
        EXPAND, SHRINK
    }

    /**
     * 播放状态 播放 暂停
     */
    public enum PlayState {
        PLAY, PAUSE
    }

    public enum ProgressState {
        START, DOING, STOP
    }

    public interface MediaControlImpl {
        void onPlayTurn();

        void onPageTurn();

        void onProgressTurn(ProgressState state, int progress);

        void alwaysShowController();

        void backOnEvent();

        void showCacheListView();

        void showShareMenuView();

        void showSharpnessView();
    }


}
