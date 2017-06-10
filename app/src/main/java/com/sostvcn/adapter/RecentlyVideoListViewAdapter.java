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
import com.sostvcn.model.RecentlyVideo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
public class RecentlyVideoListViewAdapter extends BaseAdapter {

    private Context context;
    private List<RecentlyVideo> datas;

    private BitmapUtils bitmapUtils;

    public RecentlyVideoListViewAdapter(Context context, List<RecentlyVideo> datas) {
        this.context = context;
        this.datas = datas;

        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_video_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_video_cover);//加载失败图片
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
                    R.layout.recently_video_listview_item, null, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.recently_item_imageview);
            holder.timeView = (TextView) convertView.findViewById(R.id.recentlytime_item_textview);
            holder.titleView = (TextView) convertView.findViewById(R.id.recentlytitle_item_textview);
            holder.dateView = (TextView) convertView.findViewById(R.id.recentlydate_item_textview);
            holder.hotView = (TextView) convertView.findViewById(R.id.recentlyhot_item_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (datas != null) {
            RecentlyVideo video = datas.get(i);
            double durationH = ((double) video.getVideo_duration() / 60.0d);
            double durationM = (((double) video.getVideo_duration() / 60.0d) - (int) durationH) * 60;
            String durationMStr;
            if ((int) durationM < 10) {
                durationMStr = "0" + (int) durationM;
            } else {
                durationMStr = Integer.toString((int) durationM);
            }
            String duration = Integer.toString((int) durationH) + ":" + durationMStr;
            bitmapUtils.display(holder.imageView, video.getImage());
            holder.timeView.setText(duration);
            holder.titleView.setText(video.getTitle());
            holder.dateView.setText(video.getDate());
            holder.hotView.setText(video.getHits()+"次播放");
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView timeView;
        TextView titleView;
        TextView dateView;
        TextView hotView;
    }
}
