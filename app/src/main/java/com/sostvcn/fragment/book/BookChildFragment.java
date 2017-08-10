package com.sostvcn.fragment.book;

import android.os.Bundle;

import com.sostvcn.R;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.model.SosBookCates;

/**
 * Created by Administrator on 2017/7/23.
 */
public class BookChildFragment extends BaseFragment {

    public SosBookCates cates;
    //是否是棕树期刊页面
    public boolean isZh = false;

    public BookChildFragment(SosBookCates cates){
        this.cates = cates;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_child_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {

    }

    @Override
    protected void onEvent() {

    }
}
