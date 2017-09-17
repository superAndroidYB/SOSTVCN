package com.sostvcn.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.BookPageFactory;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosBookContent;
import com.sostvcn.utils.HtmlRegexpUtil;
import com.sostvcn.utils.ScreenUtils;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.SostvBookTextView;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.utils.Log;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookContentActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private int contentId;
    private SosBookContent content;
    private BookPageApi api;
    private BookViewPagerAdapter adapter;

    @ViewInject(R.id.book_view_pager)
    private ViewPager book_view_pager;
    @ViewInject(R.id.back_btn)
    private ImageView back_btn;
    @ViewInject(R.id.share_view)
    private ImageView share_view;
    @ViewInject(R.id.book_flag_view)
    private ImageView book_flag_view;

    @ViewInject(R.id.catal_view)
    private TextView catal_view;
    @ViewInject(R.id.process_view)
    private TextView process_view;
    @ViewInject(R.id.setting_view)
    private TextView setting_view;
    @ViewInject(R.id.model_view)
    private TextView model_view;
    private List<View> viewList;

    @ViewInject(R.id.topToolBar)
    private RelativeLayout topToolBar;
    @ViewInject(R.id.bottomToolBar)
    private RelativeLayout bottomToolBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_content;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        contentId = getIntent().getIntExtra("contentId", 0);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);
        adapter = new BookViewPagerAdapter();
        viewList = new ArrayList<>();

        back_btn.setOnClickListener(this);
        share_view.setOnClickListener(this);
        book_flag_view.setOnClickListener(this);

        catal_view.setOnClickListener(this);
        process_view.setOnClickListener(this);
        setting_view.setOnClickListener(this);
        model_view.setOnClickListener(this);
        book_view_pager.setOnTouchListener(this);

        book_view_pager.setAdapter(adapter);

        initOperBtn();
        loadBookContent();
    }


    private void initOperBtn() {
        Drawable a = getResources().getDrawable(R.mipmap.read_text_catalog);
        a.setBounds(0, 0, (int) getResources().getDimension(R.dimen.y37), (int) getResources().getDimension(R.dimen.y37));
        catal_view.setCompoundDrawables(null, a, null, null);

        Drawable b = getResources().getDrawable(R.mipmap.read_text_schedule);
        b.setBounds(0, 0, (int) getResources().getDimension(R.dimen.y37), (int) getResources().getDimension(R.dimen.y37));
        process_view.setCompoundDrawables(null, b, null, null);

        Drawable c = getResources().getDrawable(R.mipmap.read_text_set);
        c.setBounds(0, 0, (int) getResources().getDimension(R.dimen.y45), (int) getResources().getDimension(R.dimen.y37));
        setting_view.setCompoundDrawables(null, c, null, null);

        Drawable d = getResources().getDrawable(R.mipmap.read_text_night);
        d.setBounds(0, 0, (int) getResources().getDimension(R.dimen.y37), (int) getResources().getDimension(R.dimen.y37));
        model_view.setCompoundDrawables(null, d, null, null);

    }

    private void loadBookContent() {
        api.loadBookContent(contentId).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosBookContent>>(new SubscriberOnNextListener<BaseObjectResponse<SosBookContent>>() {
                    @Override
                    public void onNext(BaseObjectResponse<SosBookContent> sosBookContentBaseObjectResponse) {
                        content = sosBookContentBaseObjectResponse.getResults();

                        try {
                            Parser parser = new Parser(content.getContent_text());
                            NodeList nodeList = parser.parse(new NodeFilter() {
                                @Override
                                public boolean accept(Node node) {
                                    return true;
                                }
                            });

                            StringBuffer sb = new StringBuffer();
                            for (Node node : nodeList.toNodeArray()) {
                                int s = node.getText().indexOf(">");
                                int e = node.getText().indexOf("<");
                                sb.append(node.getText());
                                break;
                            }

                            View view = LayoutInflater.from(BookContentActivity.this).inflate(R.layout.book_viewpager_item, null);
                            LinearLayout contentView = (LinearLayout) view.findViewById(R.id.text_content_view);
                            TextView title_text_view = (TextView) view.findViewById(R.id.title_text_view);
                            title_text_view.setText(content.getTitle());
                            SostvBookTextView tv = new SostvBookTextView(BookContentActivity.this);
                            tv.setText(HtmlRegexpUtil.filterHtml(content.getContent_text()));
                            //tv.setTextSize(getResources().getDimension(R.dimen.x36));
                            tv.setTextIsSelectable(true);
                            tv.setLineSpacing(getResources().getDimension(R.dimen.x64), 0);
                            tv.setTextColor(getResources().getColor(R.color.fragment_home_tj));
                            tv.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    ToastUtils.showCenter(BookContentActivity.this, "aaaa");
                                    return false;
                                }
                            });
                            contentView.addView(tv);
                            viewList.add(view);

                            adapter.notifyDataSetChanged();

                            showOrHideToolBar();

                        } catch (ParserException e) {
                            e.printStackTrace();
                        }
                    }
                }, this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int height = ScreenUtils.getScreenHeight(this);
        int width = ScreenUtils.getScreenWidth(this);
        if (motionEvent.getAction() == MotionEvent.ACTION_UP && x > (width / 3) && x < (width / 3 + width / 3)) {
            showOrHideToolBar();
        }

        return false;
    }

    private void showOrHideToolBar() {
        if (topToolBar.getVisibility() == View.VISIBLE) {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            topToolBar.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    topToolBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_SELF, -1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            topToolBar.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    topToolBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        if (bottomToolBar.getVisibility() == View.VISIBLE) {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            bottomToolBar.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomToolBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        } else {
            Animation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_SELF, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());

            bottomToolBar.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomToolBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }


    class BookViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
    }

    public static void start(Context context, int contentId) {
        Intent intent = new Intent(context, BookContentActivity.class);
        intent.putExtra("contentId", contentId);
        context.startActivity(intent);
    }


}
