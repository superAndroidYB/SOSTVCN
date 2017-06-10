package com.sostvcn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sostvcn.R;
import com.sostvcn.model.SosAudioList;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
public class AudioListViewAdapter extends BaseAdapter {

    private Context context;
    private List<SosAudioList.Voice_list> audioList;

    public AudioListViewAdapter(Context context, List<SosAudioList.Voice_list> audioList) {
        this.audioList = audioList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return audioList.size();
    }

    @Override
    public Object getItem(int i) {
        return audioList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.audio_list_view_item, null);
            viewHolder.orderText = (TextView) view.findViewById(R.id.audio_order);
            viewHolder.titleText = (TextView) view.findViewById(R.id.audio_title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        /*
        TextView textView = (TextView)findViewById(R.id.tv_finish);
Drawable drawable= getResources().getDrawable(R.drawable.ic_finish);
drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//这句一定要加
textView.setCompoundDrawables(drawable,null,null,null);//setCompoundDrawables用来设置图片显示在文本的哪一端
         */
        SosAudioList.Voice_list audio = audioList.get(i);
        viewHolder.orderText.setText((i+1)+"");
        viewHolder.titleText.setText(audio.getTitle());
        return view;
    }

    static class ViewHolder {
        TextView orderText;
        TextView titleText;
    }
}
