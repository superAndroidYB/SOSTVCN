package com.sostvcn.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SostvListView extends ListView {

	public SostvListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SostvListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SostvListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
