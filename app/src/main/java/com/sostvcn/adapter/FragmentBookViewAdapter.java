package com.sostvcn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.sostvcn.R;
import com.sostvcn.api.BookPageApi;
import com.sostvcn.gateway.callback.ProgressSubscriber;
import com.sostvcn.gateway.callback.SubscriberOnNextListener;
import com.sostvcn.gateway.http.HttpUtils;
import com.sostvcn.model.BaseListResponse;
import com.sostvcn.model.SosBookCates;
import com.sostvcn.model.SosBookItems;
import com.sostvcn.model.SosMagazines;
import com.sostvcn.widget.SostvGridView;

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
    private Map<Integer,List<SosMagazines>> childList;
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
            final List<SosMagazines> list = new ArrayList<>();
            api.loadMagazines(item.getCate_id()).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber<BaseListResponse<SosMagazines>>(new SubscriberOnNextListener<BaseListResponse<SosMagazines>>() {
                        @Override
                        public void onNext(BaseListResponse<SosMagazines> sosPalmsBaseListResponse) {
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
        return cates.getSubCates().get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childList.get(cates.getSubCates().get(i).getCate_id()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_zslist_group,null);
        }
        RelativeLayout year_btn = (RelativeLayout)view.findViewById(R.id.year_btn);
        TextView year_btn_text = (TextView)view.findViewById(R.id.year_btn_text);
        year_btn_text.setText(cates.getSubCates().get(i).getCate_title());
        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fragment_zslist_item,null);
        }
        SostvGridView zs_grid = (SostvGridView)view.findViewById(R.id.zs_grid);
        zs_grid.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return childList.get(cates.getSubCates().get(i).getCate_id()).size();
            }

            @Override
            public Object getItem(int position) {
                return childList.get(cates.getSubCates().get(i).getCate_id()).get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.fragment_zsgrid_item,null);
                }
                ImageView zs_grid_image = (ImageView) convertView.findViewById(R.id.zs_grid_image);
                TextView zs_grid_title = (TextView) convertView.findViewById(R.id.zs_grid_title);

                SosMagazines magazines = childList.get(cates.getSubCates().get(i).getCate_id()).get(position);
                bitmapUtils.display(zs_grid_image, magazines.getCover_image());
                zs_grid_title.setText(magazines.getCate_title());
                return convertView;
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
