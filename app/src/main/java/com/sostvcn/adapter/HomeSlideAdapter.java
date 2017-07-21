package com.sostvcn.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;
import com.sostvcn.R;
import com.sostvcn.model.SosSlideImage;

import java.util.List;

public class HomeSlideAdapter extends BaseAdapter {

	private BitmapUtils bitmapUtils;
	private Context _context;
	private List<SosSlideImage> imgList;

	public HomeSlideAdapter(Context context, List<SosSlideImage> imgList ) {
		_context = context;
		this.imgList=imgList;

		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDiskCacheEnabled(true);
		bitmapUtils.configDefaultLoadingImage(R.mipmap.home_video_cover);//默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.home_video_cover);//加载失败图片
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		position = position % imgList.size();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			bitmapUtils.display(imageView,imgList.get(position).getImage());
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//viewHolder.imageView.setImageResource(imgList.get(position % imgList.size()));
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}
}
