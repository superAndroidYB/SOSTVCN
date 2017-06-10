package com.sostvcn.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by Administrator on 2017/3/12.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

//    @ViewInject(R.id.toolbar)
//    private Toolbar toolbar;

    //    初始化数据
    protected abstract void onInitView(Bundle bundle);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ViewUtils.inject(this);
//        setSupportActionBar(toolbar);

        onInitView(savedInstanceState);
    }

    protected abstract int getLayoutId();

}
