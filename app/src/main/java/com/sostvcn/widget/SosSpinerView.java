package com.sostvcn.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sostvcn.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/13.
 */
public class SosSpinerView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context context;
    private List<String> datas;
    private TextView yearTextView;
    private ImageView spinerSpread;
    private boolean isSpread;
    private Activity activity;
    private PopupWindow popupWindow;
    private OnSelectedClikcListener listener;
    private int layout;

    public SosSpinerView(Context context) {
        super(context);
        this.context = context;
        initView(null);
    }

    public SosSpinerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(attrs);
    }

    public SosSpinerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }


    public void setConfig(Activity activity, List<String> datas, OnSelectedClikcListener listener, int layout) {
        this.activity = activity;
        this.datas = datas;
        this.listener = listener;
        this.layout = layout;
    }

    public void setYearTextView(String year) {
        yearTextView.setText(year);
    }

    private void initView(AttributeSet attrs) {
        View.inflate(context, R.layout.spiner_window_layout, this);
        yearTextView = (TextView) findViewById(R.id.year_text_view);
        spinerSpread = (ImageView) findViewById(R.id.spiner_spread);
        this.setOnClickListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SosSpinerView);
        float textSize = a.getDimension(R.styleable.SosSpinerView_textSize, getResources().getDimension(R.dimen.x30));
        yearTextView.setTextSize(textSize);
        int textColor = a.getColor(R.styleable.SosSpinerView_textColor, getResources().getColor(R.color.spiner_text_default));
        yearTextView.setTextColor(textColor);
    }

    @Override
    public void onClick(View view) {
        if (isSpread) {
            isSpread = false;
            spinerSpread.setImageResource(R.mipmap.video_intro_arrow_down);
        } else {
            isSpread = true;
            spinerSpread.setImageResource(R.mipmap.video_intro_arrow_up);
        }
        showOrHidePopupWindow();
    }

    private void showOrHidePopupWindow() {
        if (activity != null) {
            View popupWindowView = activity.getLayoutInflater().inflate(R.layout.spiner_popup_layout, null);
            if (popupWindow == null) {
                //popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow = new PopupWindow(popupWindowView, (int) getResources().getDimension(R.dimen.x500), ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                ColorDrawable drawable = new ColorDrawable(85000000);
                popupWindow.setBackgroundDrawable(drawable);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                        return false;
                    }
                });
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        isSpread = false;
                        spinerSpread.setImageResource(R.mipmap.video_intro_arrow_down);
                    }
                });
                ListView yearListView = (ListView) popupWindowView.findViewById(R.id.spiner_list_view);
                yearListView.setAdapter(new ListAdapter());
                yearListView.setOnItemClickListener(this);
            }
            int[] location = new int[2];
            yearTextView.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1] + yearTextView.getHeight();
            popupWindow.showAtLocation(activity.getLayoutInflater().inflate(layout, null), Gravity.TOP, x, y);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        isSpread = false;
        spinerSpread.setImageResource(R.mipmap.video_intro_arrow_down);
        popupWindow.dismiss();
        listener.onSelectedEvent(datas.get(i));
    }


    class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.spiner_popup_item_view, null, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.ytextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (datas != null) {
                holder.textView.setText(datas.get(i));
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView textView;
    }

    public interface OnSelectedClikcListener {

        void onSelectedEvent(String value);
    }
}
