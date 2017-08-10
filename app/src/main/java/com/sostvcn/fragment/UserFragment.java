package com.sostvcn.fragment;

import android.os.Bundle;

import com.sostvcn.R;
import com.sostvcn.gateway.base.BaseFragment;
import com.umeng.analytics.MobclickAgent;


public class UserFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void onInitView(Bundle bundle) {

    }

    @Override
    protected void onEvent() {

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("UserFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("UserFragment");
    }


}
