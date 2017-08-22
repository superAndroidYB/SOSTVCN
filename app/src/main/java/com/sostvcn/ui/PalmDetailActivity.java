package com.sostvcn.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.model.SosMagazines;

/**
 * 棕树详情页面
 *
 * @author yubin
 * @since 2017-8-20
 */
public class PalmDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.back_btn)
    private ImageView back_btn;
    @ViewInject(R.id.book_image)
    private ImageView book_image;
    @ViewInject(R.id.book_title)
    private TextView book_title;
    @ViewInject(R.id.book_theme)
    private TextView book_theme;
    @ViewInject(R.id.book_desc)
    private TextView book_desc;

    @ViewInject(R.id.read_btn)
    private TextView read_btn;
    @ViewInject(R.id.book_collect_btn)
    private TextView book_collect_btn;
    @ViewInject(R.id.book_share_btn)
    private TextView book_share_btn;
    @ViewInject(R.id.book_catalogue_btn)
    private TextView book_catalogue_btn;

    private SosMagazines magazines;
    private BitmapUtils bitmapUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_palm_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        magazines = (SosMagazines) getIntent().getSerializableExtra("magazines");
        bitmapUtils = new BitmapUtils(this);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        if (magazines != null) {
            bitmapUtils.display(book_image, magazines.getCover_image());
            book_title.setText(magazines.getCate_title().replace("棕树月刊",""));
        }


        back_btn.setOnClickListener(this);
        read_btn.setOnClickListener(this);
        book_collect_btn.setOnClickListener(this);
        book_share_btn.setOnClickListener(this);
        book_catalogue_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.read_btn:
                break;
            case R.id.book_collect_btn:
                break;
            case R.id.book_share_btn:
                break;
            case R.id.book_catalogue_btn:
                break;
        }
    }

    public static void start(Context context, SosMagazines magazines) {
        Intent intent = new Intent(context, PalmDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("magazines", magazines);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
