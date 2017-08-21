package com.sostvcn.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;

/**
 * 棕树详情页面
 * @author yubin
 * @since 2017-8-20
 */
public class PalmDetailActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.back_btn)
    private ImageView back_btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_palm_detail;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        back_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }

    }
}
