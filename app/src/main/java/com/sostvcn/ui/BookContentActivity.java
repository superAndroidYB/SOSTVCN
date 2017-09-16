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
import android.view.View;
import android.widget.ImageView;
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

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookContentActivity extends BaseActivity implements View.OnClickListener {

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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_content;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        contentId = getIntent().getIntExtra("contentId", 0);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);
        adapter = new BookViewPagerAdapter();

        back_btn.setOnClickListener(this);
        share_view.setOnClickListener(this);
        book_flag_view.setOnClickListener(this);

        catal_view.setOnClickListener(this);
        process_view.setOnClickListener(this);
        setting_view.setOnClickListener(this);
        model_view.setOnClickListener(this);

        initOperBtn();
    }

    private void initOperBtn() {
        Drawable a = getResources().getDrawable(R.mipmap.read_text_catalog);
        a.setBounds(0, 0, (int)getResources().getDimension(R.dimen.y37), (int)getResources().getDimension(R.dimen.y37));
        catal_view.setCompoundDrawables(null, a, null, null);

        Drawable b = getResources().getDrawable(R.mipmap.read_text_schedule);
        b.setBounds(0, 0, (int)getResources().getDimension(R.dimen.y37), (int)getResources().getDimension(R.dimen.y37));
        process_view.setCompoundDrawables(null, b, null, null);

        Drawable c = getResources().getDrawable(R.mipmap.read_text_set);
        c.setBounds(0, 0, (int)getResources().getDimension(R.dimen.y37), (int)getResources().getDimension(R.dimen.y37));
        setting_view.setCompoundDrawables(null, c, null, null);

        Drawable d = getResources().getDrawable(R.mipmap.read_text_night);
        d.setBounds(0, 0, (int)getResources().getDimension(R.dimen.y37), (int)getResources().getDimension(R.dimen.y37));
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
                                sb.append(node.getText());
                            }

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


    class BookViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

    public static void start(Context context, int contentId) {
        Intent intent = new Intent(context, BookContentActivity.class);
        intent.putExtra("contentId", contentId);
        context.startActivity(intent);
    }


}
