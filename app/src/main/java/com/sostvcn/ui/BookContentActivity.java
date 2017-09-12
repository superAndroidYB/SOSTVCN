package com.sostvcn.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.BookPageFactory;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosBookContent;
import com.sostvcn.utils.SDCardUtils;
import com.sostvcn.widget.SostvBookView;
import com.umeng.socialize.media.Base;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookContentActivity extends Activity {

    private int contentId;
    private SosBookContent content;
    private BookPageApi api;


    private SostvBookView mPageWidget;
    Bitmap mCurPageBitmap, mNextPageBitmap;
    Canvas mCurPageCanvas, mNextPageCanvas;
    BookPageFactory pagefactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;


        mPageWidget = new SostvBookView(this, w_screen, h_screen);
        setContentView(mPageWidget);


        mCurPageBitmap = Bitmap.createBitmap(w_screen, h_screen, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap
                .createBitmap(w_screen, h_screen, Bitmap.Config.ARGB_8888);

        mCurPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);
        pagefactory = new BookPageFactory(w_screen, h_screen);

        pagefactory.setBgBitmap(BitmapFactory.decodeResource(
                this.getResources(), R.drawable.button_bg));

        try {
            pagefactory.openbook("/" + SDCardUtils.getSDCardRootDir() + "/test.txt");
            pagefactory.onDraw(mCurPageCanvas);
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "电子书不存在,请将《test.txt》放在SD卡根目录下",
                    Toast.LENGTH_SHORT).show();
        }

        mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);

        mPageWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {

                boolean ret = false;
                if (v == mPageWidget) {
                    if (e.getAction() == MotionEvent.ACTION_DOWN) {
                        mPageWidget.abortAnimation();
                        mPageWidget.calcCornerXY(e.getX(), e.getY());

                        //pagefactory.onDraw(mCurPageCanvas);
                        if (mPageWidget.DragToRight()) {
                            try {
                                pagefactory.prePage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (pagefactory.isfirstPage()) return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        } else {
                            try {
                                pagefactory.nextPage();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            if (pagefactory.islastPage()) return false;
                            pagefactory.onDraw(mNextPageCanvas);
                        }
                        mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
                    }

                    ret = mPageWidget.doTouchEvent(e);
                    return ret;
                }
                return false;
            }

        });
    }
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_book_content;
//    }
//
//    @Override
//    protected void onInitView(Bundle bundle) {
//        contentId = getIntent().getIntExtra("contentId", 0);
//        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);
//        api.loadBookContent(contentId).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosBookContent>>(new SubscriberOnNextListener<BaseObjectResponse<SosBookContent>>() {
//                    @Override
//                    public void onNext(BaseObjectResponse<SosBookContent> sosBookContentBaseObjectResponse) {
//                        content = sosBookContentBaseObjectResponse.getResults();
//                        try {
//                            Parser parser = new Parser(content.getContent_text());
//                            NodeList nodeList = parser.parse(new NodeFilter() {
//                                @Override
//                                public boolean accept(Node node) {
//                                    return true;
//                                }
//                            });
//
//                            StringBuffer sb = new StringBuffer();
//                            for (Node node : nodeList.toNodeArray()) {
//                                sb.append(node.getText());
//                            }
//
//                        } catch (ParserException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, this));
//
//
//    }

    public static void start(Context context, int contentId) {
        Intent intent = new Intent(context, BookContentActivity.class);
        intent.putExtra("contentId", contentId);
        context.startActivity(intent);
    }


}
