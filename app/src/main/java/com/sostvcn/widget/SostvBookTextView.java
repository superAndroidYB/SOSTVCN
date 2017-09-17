package com.sostvcn.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017\9\17 0017.
 */

public class SostvBookTextView extends android.support.v7.widget.AppCompatTextView {

    public SostvBookTextView(Context context) {
        super(context);
    }

    public SostvBookTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SostvBookTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {

    }
}
