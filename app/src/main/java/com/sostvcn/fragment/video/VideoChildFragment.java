package com.sostvcn.fragment.video;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FargmentGridViewAdapter;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.model.SosSubCates;
import com.sostvcn.ui.VideoViewActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */
public class VideoChildFragment extends BaseFragment {

    @ViewInject(R.id.frag_video_grid_view)
    private GridView gridView;
    private List<SosSubCates> subCates;
    private FargmentGridViewAdapter adapter;

    public VideoChildFragment(List<SosSubCates> subCates) {
        this.subCates = subCates;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_child_layout;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        adapter = new FargmentGridViewAdapter(getActivity(), subCates);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected void onEvent() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                VideoViewActivity.start(getActivity(), subCates.get(i).getCate_id(), true);
            }
        });
    }

}
