package com.sostvcn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.sostvcn.R;
import com.sostvcn.model.SosBookRecommend;

import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */
public class SosBookGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<SosBookRecommend> datas;
    private BitmapUtils bitmapUtils;

    public SosBookGridViewAdapter(Context context, List<SosBookRecommend> datas){
        this.context = context;
        this.datas = datas;

        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_book_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_book_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(
                    R.layout.tjyd_grid_view_item, null, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.tjyd_item_imageview);
            holder.textView = (TextView) convertView.findViewById(R.id.tjyd_item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(datas != null){
            SosBookRecommend book = datas.get(i);
            bitmapUtils.display(holder.imageView, book.getImage());
            holder.textView.setText(book.getBook_title());
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
