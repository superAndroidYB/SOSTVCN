package com.sostvcn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sostvcn.R;
import com.sostvcn.model.SosAudioList;

/**
 * Created by Administrator on 2017/6/25.
 */
public class MultiselectListAdapter extends BaseAdapter {
    private Context context;
    private SosAudioList audioList;

    public MultiselectListAdapter(Context context, SosAudioList audioList) {
        this.audioList = audioList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return audioList.getVoice_list().size();
    }

    @Override
    public Object getItem(int i) {
        return audioList.getVoice_list().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.multiselect_list_item, null);
            holder.selectCheckBox = (CheckBox) view.findViewById(R.id.selected_checkbox);
            holder.titleText = (TextView) view.findViewById(R.id.audio_titile);
            holder.downloadCheckBox = (CheckBox) view.findViewById(R.id.download_checkbox);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        SosAudioList.Voice_list item = audioList.getVoice_list().get(i);
        holder.titleText.setText(item.getTitle());
        return view;
    }

    static class ViewHolder {
        CheckBox selectCheckBox;
        TextView titleText;
        CheckBox downloadCheckBox;
    }
}
