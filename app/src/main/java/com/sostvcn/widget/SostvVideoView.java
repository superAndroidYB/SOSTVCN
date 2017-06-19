package com.sostvcn.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sostvcn.R;
import com.sostvcn.model.SosCateVideo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Administrator on 2016/8/28.
 */
public class SostvVideoView extends RelativeLayout implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private final int TIME_SHOW_CONTROLLER = 5000;
    private final int TIME_UPDATE_PLAY_TIME = 1000;
    private final int MSG_HIDE_CONTROLLER = 10;
    private final int MSG_UPDATE_PLAY_TIME = 11;
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;

    private DateFormat formatter = new SimpleDateFormat("mm:ss");
    private final long mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    private VideoMediaController.PageType mCurrPageType = VideoMediaController.PageType.SHRINK;// 当前是横屏还是竖屏

    private Context mContext;
    private VideoView videoView;
    private Uri uri;
    private VideoMediaController mMediaController;
    private View mProgressBarView;
    private View progressDialog;
    private Timer mUpdateTimer;
    private TimerTask mTimerTask;
    private int progressSec = 0;
    private TextView percentTextview;
    private TextView extraTextview;
    private ImageView kjkvImageView;

    private GestureDetector gestureDetector; //手势监听
    private TextView progSeekTime;
    private TextView progTotalTime;
    private FrameLayout curtain;

    private VideoPlayCallbackImpl mVideoPlayCallback;

    public SostvVideoView(Context context) {
        super(context);
        initView(context);
    }

    public SostvVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SostvVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setSosCateVideo(SosCateVideo cateVideo) {
        mMediaController.setSosCateVideo(cateVideo);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_PLAY_TIME:
                    updatePlayTime();
                    updatePlayProgress();
                    break;
                case MSG_HIDE_CONTROLLER:
                    showOrHideController();
                    break;
            }
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    private OnTouchListener mOnTouchVideoListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    };


    public void setVideoPlayCallback(VideoPlayCallbackImpl mVideoPlayCallback) {
        this.mVideoPlayCallback = mVideoPlayCallback;
    }

    public int getCurrentPosition() {
        return (int) videoView.getCurrentPosition();
    }

    public void close() {
        mMediaController.playFinish(0);
        videoView.pause();
        videoView.setVisibility(GONE);
    }

    public void setPageType(VideoMediaController.PageType type) {
        mCurrPageType = type;
        mMediaController.setPageType(type);
    }

    private void showOrHideController() {
        if (mMediaController.getVisibility() == View.VISIBLE) {
            mMediaController.animate()
                    .alpha(0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mMediaController.setVisibility(View.GONE);
                        }
                    });

        } else {
            resetHideTimer();
            mMediaController.animate()
                    .alpha(1f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mMediaController.setVisibility(View.VISIBLE);
                        }
                    });

        }
    }

    private void resetHideTimer() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER,
                TIME_SHOW_CONTROLLER);
    }

    private void updatePlayProgress() {
        if (videoView == null) {
            return;
        }
        try {
            int allTime = (int) videoView.getDuration();
            int playTime = (int) videoView.getCurrentPosition();
            int progress = playTime * 100 / allTime;
            mMediaController.setProgressBar(progress, progressSec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePlayTime() {
        if (videoView == null) {
            return;
        }
        try {
            int allTime = (int) videoView.getDuration();
            int playTime = (int) videoView.getCurrentPosition();
            mMediaController.setPlayProgressTxt(playTime, allTime);
            progTotalTime.setText(" / " + formatter.format(new Date(allTime)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(Context context) {
        this.mContext = context;
        View.inflate(context, R.layout.sostv_video_view, this);
        mProgressBarView = findViewById(R.id.video_probar);
        progressDialog = findViewById(R.id.progress_dialog);
        videoView = (VideoView) findViewById(R.id.video_view);
        mMediaController = (VideoMediaController) findViewById(R.id.controller);
        progSeekTime = (TextView) findViewById(R.id.prog_seek_time);
        progTotalTime = (TextView) findViewById(R.id.prog_total_time);
        percentTextview = (TextView) findViewById(R.id.percent_textview);
        extraTextview = (TextView) findViewById(R.id.extra_textview);
        kjkvImageView = (ImageView) findViewById(R.id.kjkt_image_view);
        curtain = (FrameLayout)findViewById(R.id.curtain);
        mMediaController.setMediaControl(mMediaControl);
        videoView.setBufferSize(512 * 1024);
        videoView.setOnInfoListener(this);
        videoView.setOnTouchListener(mOnTouchVideoListener);
        gestureDetector = new GestureDetector(mContext, new SosGestureListener());
        videoView.setOnBufferingUpdateListener(this);

    }

    public void showCurtain(){
        curtain.setVisibility(VISIBLE);
        mProgressBarView.setVisibility(GONE);
        percentTextview.setVisibility(GONE);
        extraTextview.setVisibility(GONE);
    }

    public void startPlay(String videoUrl) {
        //videoUrl = SostvApplication.getProxy().getProxyUrl(videoUrl);
        uri = Uri.parse(videoUrl);
        videoView.setVideoURI(uri);
        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);// 高画质
        videoView.requestFocus();

        mProgressBarView.setVisibility(VISIBLE);
        percentTextview.setVisibility(View.VISIBLE);
        extraTextview.setVisibility(View.VISIBLE);
        curtain.setVisibility(GONE);

        resetHideTimer();
        resetUpdateTimer();
        mMediaController.setPlayState(VideoMediaController.PlayState.PLAY);
        requestLayout();
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            // 媒体播放器内部暂时暂停播放缓冲区更多的数据。
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    if (mProgressBarView.getVisibility() == View.GONE) {
                        mProgressBarView.setVisibility(View.VISIBLE);
                        percentTextview.setVisibility(View.VISIBLE);
                        extraTextview.setVisibility(View.VISIBLE);
                    }
                }
                break;
            // 媒体播放器后恢复回放填充缓冲区
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                videoView.start();
                if (mProgressBarView.getVisibility() == View.VISIBLE) {
                    mProgressBarView.setVisibility(View.GONE);
                    percentTextview.setVisibility(View.GONE);
                    extraTextview.setVisibility(View.GONE);
                }
                break;
            // 媒体播放器下载到缓冲区
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                extraTextview.setText(extra + "kb/s");
                break;
        }
        return true;
    }

    public VideoMediaController.MediaControlImpl mMediaControl = new VideoMediaController.MediaControlImpl() {

        @Override
        public void onPlayTurn() {
            if (videoView.isPlaying()) {
                pausePlay();
            } else {
                startPlayVideo();
            }
        }

        @Override
        public void onPageTurn() {
            mVideoPlayCallback.onSwitchPageType();
        }

        @Override
        public void onProgressTurn(VideoMediaController.ProgressState state, int progress) {
            if (state.equals(VideoMediaController.ProgressState.START)) {
                mHandler.removeMessages(MSG_HIDE_CONTROLLER);
            } else if (state.equals(VideoMediaController.ProgressState.STOP)) {
                resetHideTimer();
            } else {
                int time = progress * (int) videoView.getDuration() / 100;
                videoView.seekTo(time);
                updatePlayTime();
            }
        }

        @Override
        public void alwaysShowController() {
            SostvVideoView.this.alwaysShowController();
        }

        @Override
        public void backOnEvent() {
            mVideoPlayCallback.onCloseVideo();
        }

        @Override
        public void showCacheListView() {
            mVideoPlayCallback.showCacheListView();
        }

        @Override
        public void showShareMenuView() {
            mVideoPlayCallback.showShareMenuView();
        }
    };

    private void alwaysShowController() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.setVisibility(View.VISIBLE);
    }

    public void startPlayVideo() {
        videoView.start();
        resetHideTimer();
        resetUpdateTimer();
        mMediaController.setPlayState(VideoMediaController.PlayState.PLAY);
        requestLayout();
        invalidate();
    }

    private void resetUpdateTimer() {
        stopUpdateTimer();
        mUpdateTimer = new Timer();
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_TIME);
            }
        };
        mUpdateTimer.schedule(mTimerTask, 0, TIME_UPDATE_PLAY_TIME);
    }

    private void stopUpdateTimer() {
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public void setVideoLayout(int layout, float aspectRatio) {
        //最优选择，由于比例问题还是会离屏幕边缘有一点间距，所以最好把父View的背景设置为黑色会好一点
        videoView.setVideoLayout(layout, aspectRatio);
    }

    public void pausePlay() {
        videoView.pause();
        mMediaController.setPlayState(VideoMediaController.PlayState.PAUSE);
        stopHideTimer();
    }

    private void stopHideTimer() {
        mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        mMediaController.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mMediaController.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void stopPlayback(){
        videoView.stopPlayback();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        percentTextview.setText(percent + "%");
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public interface VideoPlayCallbackImpl {
        void onCloseVideo();

        void onSwitchPageType();

        void onPlayFinish();

        void showCacheListView();

        void showShareMenuView();
    }

    class SosGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                showOrHideController();
            }
            //return mCurrPageType == VideoMediaController.PageType.EXPAND ? true : false;
            return true;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                //向左滑动
                kjkvImageView.setImageResource(R.mipmap.videofull_backward);
                progressDialog.setAlpha(1);
                progressDialog.setVisibility(VISIBLE);
                long seekTime = (long) (videoView.getCurrentPosition() - Math.abs(velocityX) * 10);
                progSeekTime.setText(formatter.format(seekTime));
                videoView.seekTo(seekTime);
            } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
                //向右滑动
                kjkvImageView.setImageResource(R.mipmap.videofull_forward);
                progressDialog.setAlpha(1);
                progressDialog.setVisibility(VISIBLE);
                long seekTime = (long) (videoView.getCurrentPosition() + Math.abs(velocityX) * 10);
                progSeekTime.setText(formatter.format(seekTime));
                videoView.seekTo(seekTime);
            }
            progressDialog.animate().alpha(0f)
                    .setDuration(mShortAnimationDuration * 5)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressDialog.setVisibility(View.GONE);
                        }
                    });
            return false;
        }
    }
}
