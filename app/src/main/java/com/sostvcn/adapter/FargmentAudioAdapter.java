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
import com.sostvcn.model.SosAudioCatesInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
public class FargmentAudioAdapter extends BaseAdapter {

    private Context context;
    private List<SosAudioCatesInfo.SubCates> subCates;
    private int layoutId;
    private BitmapUtils bitmapUtils;

    public FargmentAudioAdapter(Context context, List<SosAudioCatesInfo.SubCates> subCates, int layoutId) {
        this.context = context;
        this.subCates = subCates;
        this.layoutId = layoutId;

        bitmapUtils = new BitmapUtils(context);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configDefaultLoadingImage(R.mipmap.video_cover);//默认背景图片
        bitmapUtils.configDefaultLoadFailedImage(R.mipmap.video_cover);//加载失败图片
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
    }

    @Override
    public int getCount() {
        return subCates.size();
    }

    @Override
    public Object getItem(int i) {
        return subCates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutId, null, false);
            holder = new ViewHolder();
            holder.audioImage = (ImageView) view.findViewById(R.id.audio_item_image);
            holder.audioTitle = (TextView) view.findViewById(R.id.audio_item_title);
            holder.audioSubTitle = (TextView) view.findViewById(R.id.audio_item_subtitle);
            holder.audioNum = (TextView) view.findViewById(R.id.audio_item_num);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SosAudioCatesInfo.SubCates subCate = subCates.get(i);
        if (subCate != null) {
            bitmapUtils.display(holder.audioImage, subCate.getCate_image());
            holder.audioTitle.setText(subCate.getCate_title());
            holder.audioSubTitle.setText(subCate.getAlias());
            holder.audioNum.setText(subCate.getVoice_count()+"讲");
        }

        return view;
    }

    static class ViewHolder {
        ImageView audioImage;
        TextView audioTitle;
        TextView audioSubTitle;
        TextView audioNum;
    }
}
