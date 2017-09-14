package com.sostvcn.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

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

public class BookContentActivity extends BaseActivity {

    private int contentId;
    private SosBookContent content;
    private BookPageApi api;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_content;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        contentId = getIntent().getIntExtra("contentId", 0);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);
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

    public static void start(Context context, int contentId) {
        Intent intent = new Intent(context, BookContentActivity.class);
        intent.putExtra("contentId", contentId);
        context.startActivity(intent);
    }


}
