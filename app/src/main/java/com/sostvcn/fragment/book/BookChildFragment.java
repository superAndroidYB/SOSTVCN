package com.sostvcn.fragment.book;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FragmentBookViewAdapter;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosMagazines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.utils.Log;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private GridView qt_gridview;

    private Map<Integer, List<SosMagazines>> childList;

    private FragmentBookViewAdapter zsAdapter;

    private BookPageApi api;
    private BitmapUtils bitmapUtils;

    public BookChildFragment(SosBookCates cates) {
        this.cates = cates;
        if ("棕树期刊".equals(cates.getCate_title())) {
            isZh = true;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_book_child_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        bitmapUtils = new BitmapUtils(this.getActivity());
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

        api = HttpUtils.getInstance(this.getActivity()).getRetofitClinet().builder(BookPageApi.class);
        if (isZh) {
            zs_listview.setVisibility(View.VISIBLE);
            qt_gridview.setVisibility(View.GONE);


            loadPalms();
            zsAdapter = new FragmentBookViewAdapter(this.getActivity(), cates, childList);
            zs_listview.setAdapter(zsAdapter);
        } else {
            zs_listview.setVisibility(View.GONE);
            qt_gridview.setVisibility(View.VISIBLE);

            qt_gridview.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return cates.getSubCates().size();
                }

                @Override
                public Object getItem(int i) {
                    return cates.getSubCates().get(i);
                }

                @Override
                public long getItemId(int i) {
                    return i;
                }

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    if (view == null) {
                        LayoutInflater inflater = (LayoutInflater) BookChildFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.fragment_qtsj_grid_item, null);
                    }
                    ImageView qtsj_item_image = (ImageView) view.findViewById(R.id.qtsj_item_image);
                    TextView qtsj_item_text = (TextView) view.findViewById(R.id.qtsj_item_text);
                    bitmapUtils.display(qtsj_item_image, cates.getSubCates().get(i).getCate_image());
                    qtsj_item_text.setText(cates.getSubCates().get(i).getCate_title());
                    return view;
                }
            });
        }
    }

    @Override
    protected void onEvent() {
        if (zs_listview != null) {
            zs_listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                    for (int j = 0; j < cates.getSubCates().size(); j++) {
                        if (i != j) {
                            Log.e("被关闭的有" + j + "i 的值为" + i, j);
                            zs_listview.collapseGroup(j);
                        }
                    }
                    return false;
                }
            });
        }
    }

    private void loadPalms() {
        childList = new HashMap<>();
        for (SosBookItems item : cates.getSubCates()) {
            final List<SosMagazines> list = new ArrayList<>();
            api.loadMagazines(item.getCate_id()).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseListResponse<SosMagazines>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BaseListResponse<SosMagazines> sosMagazinesBaseListResponse) {
                            list.addAll(sosMagazinesBaseListResponse.getResults());
                        }
                    });

            childList.put(item.getCate_id(), list);
        }
    }
}
