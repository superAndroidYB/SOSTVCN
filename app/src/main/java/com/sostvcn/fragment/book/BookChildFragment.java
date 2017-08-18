package com.sostvcn.fragment.book;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.widget.SostvGridView;

/**
 * Created by Administrator on 2017/7/23.
 */
public class BookChildFragment extends BaseFragment {

    public SosBookCates cates;
    //是否是棕树期刊页面
    public boolean isZh = false;

    @ViewInject(R.id.zs_listview)
    private ExpandableListView zs_listview;
    @ViewInject(R.id.qt_gridview)
    private SostvGridView qt_gridview;

    private BookPageApi api;

    public BookChildFragment(SosBookCates cates){
        this.cates = cates;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_child_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        api = HttpUtils.getInstance(this.getActivity()).getRetofitClinet().builder(BookPageApi.class);
        if(isZh){
            zs_listview.setVisibility(View.VISIBLE);
            qt_gridview.setVisibility(View.GONE);


        }else{
            zs_listview.setVisibility(View.GONE);
            qt_gridview.setVisibility(View.VISIBLE);


        }
    }

    @Override
    protected void onEvent() {

    }
}
