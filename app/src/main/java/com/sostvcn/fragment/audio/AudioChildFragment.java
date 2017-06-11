package com.sostvcn.fragment.audio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.FargmentAudioAdapter;
import com.sostvcn.gateway.base.BaseFragment;
import com.sostvcn.model.SosAudioCatesInfo;
import com.sostvcn.ui.SpecialActivity;

/**
 * Created by Administrator on 2017/5/7.
 */
public class AudioChildFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private SosAudioCatesInfo catesInfo;

    @ViewInject(R.id.audio_layout_v1)
    private LinearLayout v1;
    @ViewInject(R.id.frag_audio_grid_view)
    private GridView audioGridV1;

    @ViewInject(R.id.audio_layout_v2)
    private LinearLayout v2;
    @ViewInject(R.id.title_image_view)
    private ImageView titleImage;
    @ViewInject(R.id.frag_audio_grid_view_v2)
    private GridView audioGridV2;

    private BitmapUtils bitmapUtils;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_audio_child_layout;
    }

    public AudioChildFragment(SosAudioCatesInfo catesInfo) {
        this.catesInfo = catesInfo;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected void onInitView(Bundle bundle) {
        if (catesInfo != null) {
            if (catesInfo.getCate_image() == null || "".equals(catesInfo.getCate_image())) {
                v1.setVisibility(View.VISIBLE);
                audioGridV1.setAdapter(new FargmentAudioAdapter(getActivity(), catesInfo.getSubCates(), R.layout.frag_audio_grid_itme_view));
            } else {
                bitmapUtils = new BitmapUtils(getActivity());
                bitmapUtils.configDiskCacheEnabled(true);
                bitmapUtils.configDefaultLoadingImage(R.mipmap.video_cover);//默认背景图片
                bitmapUtils.configDefaultLoadFailedImage(R.mipmap.video_cover);//加载失败图片
                bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);

                v2.setVisibility(View.VISIBLE);
                bitmapUtils.display(titleImage, catesInfo.getCate_image());
                audioGridV2.setAdapter(new FargmentAudioAdapter(getActivity(), catesInfo.getSubCates(), R.layout.frag_audio_grid_itme_view_v2));
            }
        }
    }

    @Override
    protected void onEvent() {
        audioGridV1.setOnItemClickListener(this);
        audioGridV2.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SosAudioCatesInfo.SubCates subCates = catesInfo.getSubCates().get(i);
        SpecialActivity.start(getActivity(), subCates.getCate_id(), subCates.getPlaylist_id());
    }
}
