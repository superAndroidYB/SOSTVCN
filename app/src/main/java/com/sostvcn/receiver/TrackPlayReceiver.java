package com.sostvcn.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sostvcn.utils.Constants;
/**
 * 
 * ֪ͨ�����ţ��㲥������
 * */
public class TrackPlayReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent newIntent = new Intent();
        newIntent.setAction(Constants.ACTION_PLAY);
        context.sendBroadcast(newIntent);
	}

}
