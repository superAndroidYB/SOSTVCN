package com.sostvcn.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosMagazines;
import com.sostvcn.widget.SostvListView;

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
    @ViewInject(R.id.book_catalogue_lv)
    private SostvListView book_catalogue_lv;

    private SosBookItems item;
    private BitmapUtils bitmapUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        item = (SosBookItems)getIntent().getSerializableExtra("item");

        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        if (item != null) {
            bitmapUtils.display(book_image, item.getCate_image());
            book_title.setText(item.getCate_title());
        }
    }

    public static void start(Context context, SosBookItems item){
        Intent intent = new Intent(context, BookDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
