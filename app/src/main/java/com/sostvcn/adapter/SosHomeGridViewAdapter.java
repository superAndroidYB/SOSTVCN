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
import com.sostvcn.model.SosVideoRecommend;

import java.util.List;

/**
 * Created by Administrator on 2017/4/22.
 */
public class SosHomeGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<SosVideoRecommend> datas;
    private BitmapUtils bitmapUtils;

    public SosHomeGridViewAdapter(Context mContext, List<SosVideoRecommend> datas) {
        this.mContext = mContext;
        this.datas = datas;

        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.video_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.video_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        if (datas == null) {
            return null;
        }
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
            convertView = LayoutInflater.from(this.mContext).inflate(
                    R.layout.tjsp_grid_view_item, null, false);
            holder = new ViewHolder();
            holder.itemImageView = (ImageView) convertView.findViewById(R.id.tjsp_item_imageview);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.tjsp_item_datetext);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.tjsp_item_title);
            holder.subTitleTextView = (TextView) convertView.findViewById(R.id.tjsp_item_subtitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(datas != null){
            SosVideoRecommend video = datas.get(i);
            bitmapUtils.display(holder.itemImageView, video.getVideo().getImage());
            holder.dateTextView.setText(video.getVideo().getDate());
            holder.titleTextView.setText(video.getCate_title());
            holder.subTitleTextView.setText(video.getVideo().getTitle());
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView itemImageView;
        TextView dateTextView;
        TextView titleTextView;
        TextView subTitleTextView;
    }
}
