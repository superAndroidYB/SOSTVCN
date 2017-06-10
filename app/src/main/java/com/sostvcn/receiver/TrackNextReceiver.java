package com.sostvcn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sostvcn.service.MusicService;
import com.sostvcn.utils.Constants;


public class TrackNextReceiver extends BroadcastReceiver {

    private int position;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (MusicService.position == MusicService.medias.getVoice_list().size() - 1) {
            MusicService.position = 0;
        } else {
            position = MusicService.position++;
        }

        Intent newIntent = new Intent();
        newIntent.setAction(Constants.ACTION_NEXT);
        newIntent.getIntExtra("index", position);
        context.sendBroadcast(newIntent);
    }

}
