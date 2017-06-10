package com.sostvcn.apshare;

import android.os.Bundle;

import com.sostvcn.R;
import com.umeng.socialize.media.ShareCallbackActivity;

public class ShareEntryActivity extends ShareCallbackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_entry);
    }
}
