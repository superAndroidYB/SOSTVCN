package com.sostvcn.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.sostvcn.R;
import com.sostvcn.adapter.MultiselectListAdapter;
import com.sostvcn.model.SosAudioList;

public class MultiselectActivity extends BaseActivity implements AdapterView.OnClickListener {


    @ViewInject(R.id.downaudio_btn)
    private TextView downaudio_btn;
    @ViewInject(R.id.deleteaudio_btn)
    private TextView deleteaudio_btn;

    @ViewInject(R.id.quanxuan_btn)
    private TextView quanxuan_btn;
    @ViewInject(R.id.title_text_view)
    private TextView title_text_view;
    @ViewInject(R.id.cancel_btn)
    private TextView cancel_btn;

    @ViewInject(R.id.multi_list_view)
    private ListView multi_list_view;


    private SosAudioList audioList;
    private MultiselectListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_multiselect;
    }

    @Override
    protected void onInitView(Bundle bundle) {
        audioList = (SosAudioList) this.getIntent().getSerializableExtra("audioList");

        downaudio_btn.setOnClickListener(this);
        deleteaudio_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);

        adapter = new MultiselectListAdapter(this, audioList);
        multi_list_view.setAdapter(adapter);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.episode_activity_silde_in, R.anim.episode_activity_silde_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downaudio_btn:

                break;
            case R.id.deleteaudio_btn:
                break;
            case R.id.cancel_btn:
                this.finish();
                break;
        }
    }
}
