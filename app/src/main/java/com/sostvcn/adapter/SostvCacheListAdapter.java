package com.sostvcn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sostvcn.R;
import com.sostvcn.model.SosCateVideo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/18.
 */
public class SostvCacheListAdapter extends BaseAdapter {

    private Context context;
    private List<SosCateVideo.Video> videos;

    public SostvCacheListAdapter(Context context,List<SosCateVideo.Video> videos){
        this.context = context;
        this.videos = videos;
    }
    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int i) {
        return videos.get(i);
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
                    R.layout.video_cache_list_item, null, false);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.video_title_view);
            holder.itemImageView = (ImageView) convertView.findViewById(R.id.cache_flag_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(videos != null){
            SosCateVideo.Video video = videos.get(i);
            holder.titleView.setTextColor(context.getResources().getColor(R.color.white));
            holder.titleView.setText(video.getTitle());
            holder.itemImageView.setImageResource(R.mipmap.videofull_caching);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        ImageView itemImageView;
    }
}
