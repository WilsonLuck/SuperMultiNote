package org.wzg.multinotes.adapter;

import java.util.List;

import org.wzg.multifuncationnotes.R;
import org.wzg.multinotes.entity.NoteEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

public class SaveContentAdapter extends CommonAdapter<NoteEntity> {

	public SaveContentAdapter(Context context, List<NoteEntity> mDatas) {
		super(context, mDatas, R.layout.listview_item);// 加载item父布局
	}

	@Override
	public void convert(ViewHolder helper, NoteEntity item, int position) {

		if(item.getTitleContent() == null || item.getTitleContent().equals("")){
			helper.setText(R.id.tv_title, "无标题");
		}else{
			helper.setText(R.id.tv_title, item.getTitleContent());// 设置标题
		}
		helper.setText(R.id.list_content, item.getContent());// 设置内容
		helper.setText(R.id.list_time, item.getTime());// 设置时间

		helper.setImageBitmap(R.id.list_img,
				getImageThumbnail(item.getPhoto(), 200, 200));//显示返回到listview上的照片缩略图

		helper.setImageBitmap(
				R.id.list_video,
				getVideoThumbnail(item.getVideo(), 200, 200,
						MediaStore.Images.Thumbnails.MICRO_KIND));// 显示视频的第一帧
	}

	/**
	 * @param uri为数据库的对应路径
	 * @param width
	 * @param height
	 * @return
	 */
	public Bitmap getImageThumbnail(String uri, int width, int height) {
		Bitmap bitmap = null;
		// ͨ得到缩略图
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(uri, options);
		options.inJustDecodeBounds = false;
		int realWidth = options.outWidth / width;
		int realheight = options.outHeight / height;
		// 防止图片过小依然压缩
		int be = 1;
		if (realWidth < realheight) {
			be = realWidth;
		} else {
			be = realheight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 压缩过程
		bitmap = BitmapFactory.decodeFile(uri, options);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	// 视频第一帧的压缩
	private Bitmap getVideoThumbnail(String uri, int width, int height, int kind) {
		Bitmap bitmap = null;
		bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
