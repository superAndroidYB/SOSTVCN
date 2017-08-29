package com.sostvcn.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.BaseObjectResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookInfo;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosMagazines;
import com.sostvcn.widget.SostvListView;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BookDetailActivity extends BaseActivity {

    @ViewInject(R.id.back_btn)
    private ImageView back_btn;
    @ViewInject(R.id.book_image)
    private ImageView book_image;
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

    private SosBookItems item;
    private BitmapUtils bitmapUtils;
    private BookPageApi api;
    private SosBookInfo info;
    private CatalogueAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        item = (SosBookItems) getIntent().getSerializableExtra("item");
        adapter = new CatalogueAdapter();

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
        api = HttpUtils.getInstance(this).getRetofitClinet().builder(BookPageApi.class);

        if (item != null) {
            bitmapUtils.display(book_image, item.getCate_image());
            book_title.setText(item.getCate_title());
            book_catal_lv.setAdapter(adapter);
            loadBookCatalogue(item.getCate_id());
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
                        adapter.notifyDataSetChanged();
                    }
                }, this));
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
            if(item != null){
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
