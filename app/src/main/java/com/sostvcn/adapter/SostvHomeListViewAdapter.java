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
import com.sostvcn.model.SosAudioRecommend;

import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */
public class SostvHomeListViewAdapter extends BaseAdapter {
    private Context context;
    private List<SosAudioRecommend> datas;

    private BitmapUtils bitmapUtils;

    public SostvHomeListViewAdapter(Context context, List<SosAudioRecommend> datas) {
        this.context = context;
        this.datas = datas;

        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.home_voice_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_voice_cover);//加载失败图片
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
                    R.layout.tjyp_list_view_item, null, false);
            holder = new ViewHolder();
            holder.itemImageView = (ImageView) convertView.findViewById(R.id.tjyp_item_imageview);
            holder.textView1 = (TextView) convertView.findViewById(R.id.tyjp_textview1);
            holder.textView2 = (TextView) convertView.findViewById(R.id.tyjp_textview2);
            holder.textView3 = (TextView) convertView.findViewById(R.id.tyjp_textview3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(datas != null){
            SosAudioRecommend audio = datas.get(i);
            bitmapUtils.display(holder.itemImageView, audio.getVoice().getCover());
            holder.textView1.setText(audio.getAlbum_title()+" / ");
            holder.textView2.setText(audio.getVoice().getTitle());
            holder.textView3.setText(audio.getAlbum_title());
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView itemImageView;
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}
