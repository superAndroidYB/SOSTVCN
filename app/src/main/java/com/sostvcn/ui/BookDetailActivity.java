package com.sostvcn.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.helper.CollectHelper;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookInfo;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosCollectEntity;
import com.sostvcn.model.SosMagazines;
import com.sostvcn.utils.Constants;
import com.sostvcn.utils.ToastUtils;
import com.sostvcn.widget.SostvListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.yinglan.shadowimageview.ShadowImageView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.back_btn)
    private ImageView back_btn;
    @ViewInject(R.id.book_image)
    private ShadowImageView book_image;
    @ViewInject(R.id.book_title)
    private TextView book_title;
    @ViewInject(R.id.book_desc)
    private TextView book_desc;
    @ViewInject(R.id.book_collect_btn)
    private TextView book_collect_btn;
    @ViewInject(R.id.book_share_btn)
    private TextView book_share_btn;
    @ViewInject(R.id.book_catal_lv)
    private SostvListView book_catal_lv;
    @ViewInject(R.id.start_read_btn)
    private RelativeLayout start_read_btn;

    private SosBookItems item;
    private BitmapUtils bitmapUtils;
    private BookPageApi api;
    private SosBookInfo info;
    private CatalogueAdapter adapter;

    private PopupWindow popupWindow;
    private CollectHelper collectHelper;
    private SosCollectEntity collectEntity;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        item = (SosBookItems) getIntent().getSerializableExtra("item");
        info = new SosBookInfo();
        collectHelper = new CollectHelper(this);

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);

        if (item != null) {
            bitmapUtils.display(book_image, item.getCate_image());
            book_title.setText(item.getCate_title());
            //book_desc.setText(item.getCate_title());
            loadBookCatalogue(item.getCate_id());

            book_catal_lv.setOnItemClickListener(this);
        }

        back_btn.setOnClickListener(this);
        book_collect_btn.setOnClickListener(this);
        book_share_btn.setOnClickListener(this);
        start_read_btn.setOnClickListener(this);
    }

    private void actionCollectBook() {
        if (collectEntity == null) {
            collectEntity = new SosCollectEntity();
            collectEntity.setObjId(info.getBook_id());
            collectEntity.setObjName(info.getBook_title());
            collectEntity.setObjDesc(info.getBook_title());
            collectEntity.setObjImage(info.getBook_cover());
            collectEntity.setType(Constants.TYPE_BOOK);
            collectHelper.saveCollect(collectEntity);
            ToastUtils.showCenter(this,"您已成功收藏！");
        } else {
            collectHelper.deleteCollect(collectEntity);
            collectEntity = null;
            ToastUtils.showCenter(this,"您已取消收藏！");
        }
        showCollectStyle();
    }

    private void showCollectStyle() {
        if (collectEntity == null) {
            Drawable drawable = getResources().getDrawable(R.mipmap.read_details_collection);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            book_collect_btn.setCompoundDrawables(drawable, null, null, null);
            book_collect_btn.setText(R.string.audio_title2);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.read_details_collection_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            book_collect_btn.setCompoundDrawables(drawable, null, null, null);
            book_collect_btn.setText(R.string.audio_title2_2);

        }
    }

    private void loadBookCatalogue(int cateId) {
        api.loadBookInfo(cateId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<BaseObjectResponse<SosBookInfo>>(new SubscriberOnNextListener<BaseObjectResponse<SosBookInfo>>() {
                    @Override
                    public void onNext(BaseObjectResponse<SosBookInfo> sosBookInfoBaseListResponse) {
                        info = sosBookInfoBaseListResponse.getResults();
                        adapter = new CatalogueAdapter();
                        book_catal_lv.setAdapter(adapter);

                        collectEntity = collectHelper.findCollect(info.getBook_id());
                        showCollectStyle();
                    }
                }, this));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SosBookInfo.BookContent content = info.getContents().get(i);
        BookContentActivity.start(this, content.getContent_id());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.book_collect_btn:
                actionCollectBook();
                break;
            case R.id.book_share_btn:
                intiSharePopWindow();
                break;
            case R.id.start_read_btn:
                if (info != null && info.getContents() != null && info.getContents().size() > 0) {
                    SosBookInfo.BookContent content = info.getContents().iterator().next();
                    BookContentActivity.start(this, content.getContent_id());
                }
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
                //cm.setText(cateVideo.getVideo().getTitle() + "\n" + cateVideo.getVideo().getShare_url());
                Toast.makeText(this, "复制成功，可以发给朋友们啦！", Toast.LENGTH_LONG).show();
                break;
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

        popupWindow.showAtLocation(getLayoutInflater().inflate(getLayoutId(), null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void actionShare(SHARE_MEDIA type) {
        popupWindow.dismiss();
        //TODO
        //UMWeb web = new UMWeb(info.get);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    class CatalogueAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return info.getContents().size();
        }

        @Override
        public Object getItem(int position) {
            return info.getContents().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = BookDetailActivity.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.book_catalogue_list_item, null);
            }
            SosBookInfo.BookContent item = info.getContents().get(position);
            if (item != null) {
                TextView title = (TextView) convertView.findViewById(R.id.book_cata_title);
                title.setText(item.getTitle());
            }
            return convertView;
        }
    }

    public static void start(Context context, SosBookItems item) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
