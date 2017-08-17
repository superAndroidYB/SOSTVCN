package com.sostvcn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.lidroid.xutils.BitmapUtils;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosPalms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/8/15.
 */
public class FragmentBookViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private SosBookCates cates;
    private Map<Integer,List<SosPalms>> childList;
    private BitmapUtils bitmapUtils;
    public FragmentBookViewAdapter(Context context, SosBookCates cates){
        this.context = context;
        this.cates = cates;

        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }


    private void loadPalms(){
        childList = new HashMap<>();
        BookPageApi api = HttpUtils.getInstance(this.context).getRetofitClinet().builder(BookPageApi.class);

        for (SosBookItems item : cates.getSubCates()) {
            final List<SosPalms> list = new ArrayList<>();
            api.loadPalms(item.getCate_id()).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<BaseListResponse<SosPalms>>(new SubscriberOnNextListener<BaseListResponse<SosPalms>>() {
                        @Override
                        public void onNext(BaseListResponse<SosPalms> sosPalmsBaseListResponse) {
                            list.addAll(sosPalmsBaseListResponse.getResults());
                        }
                    },context));
            childList.put(item.getCate_id(), list);
        }
    }

    @Override
    public int getGroupCount() {
        return cates.getSubCates().size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childList.get(cates.getSubCates().get(i).getCate_id()).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
