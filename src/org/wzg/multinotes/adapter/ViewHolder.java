package org.wzg.multinotes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private final SparseArray<View> mViews;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * æ‹¿åˆ°ä¸?ä¸ªViewHolderå¯¹è±¡
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * ä¸ºTextViewè®¾ç½®å­—ç¬¦ä¸?
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getView(viewId);
		view.setText(text);
		return this;
	}
	/**
	 * ÉèÖÃ×ÖÌåÑÕÉ«
	 */
	public ViewHolder setTextColor(int viewId, int color){
		TextView view = getView(viewId);
		view.setTextColor(color);
		return this;
	}

	/**
	 * ä¸ºImageViewè®¾ç½®å›¾ç‰‡
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * ä¸ºImageViewè®¾ç½®å›¾ç‰‡
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}
	
	public ViewHolder setViewVisibility(int viewId,int visibility){
		View view = getView(viewId);
		view.setVisibility(visibility);
		return this;
	}
	
	public ViewHolder setViewOnCLickListener(int viewId,OnClickListener onClickListener){
		View view= getView(viewId);
		view.setOnClickListener(onClickListener);
		return this;
		
	}

}