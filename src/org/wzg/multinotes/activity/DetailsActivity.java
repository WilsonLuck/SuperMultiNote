package org.wzg.multinotes.activity;

import org.wzg.multifuncationnotes.R;
import org.wzg.multinotes.db.OperateNotesDataBase;
import org.wzg.multinotes.entity.NoteEntity;
import org.wzg.multinotes.utils.BitmapUtil;
import org.wzg.multinotes.utils.SoundPlay;
import org.wzg.multinotes.utils.SoundPlay.OnPlayCompletionListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailsActivity extends Activity implements OnClickListener,
		OnPlayCompletionListener {

	private EditText mTitleEt;

	/** 盛放照片 */
	private ImageView mImgDetailSaved;
	/** 盛放视频 */
	private VideoView mVideoDetailSaved;
	/** 盛放我内容 */
	private EditText mEditTextDetailSaved;
	/** 保存 */
	private TextView mTextDetailSave;
	/** 返回 */
	private TextView mTextDetailBack;
	/** 录音详情页 */
	private RelativeLayout mLayoutRecord;
	/** 录音状态 */
	private TextView mTextRecordState;
	private SoundPlay mSoundPlay;

	// 下部栏，待实现…
	private ImageView mImgSaveByPhoto;
	private ImageView mImgSaveByVideo;
	private ImageView mSaveByTakeImageView;
	private ImageView mSaveVideoImageView;
	private TextView mShowTextPopupwindow;

	private PopupWindow mPopWindow;
	private OperateNotesDataBase mDBWriter;
	// 笔记本列表实体类对象
	private NoteEntity item;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_details);

		mDBWriter = new OperateNotesDataBase(this);
		mSoundPlay = new SoundPlay();

		initViews();
		initData();

	}

	private void initViews() {

		mTextDetailBack = (TextView) findViewById(R.id.tv_back);
		mTextDetailSave = (TextView) findViewById(R.id.tv_dt_save);

		mTitleEt = (EditText) findViewById(R.id.et_title);
		mEditTextDetailSaved = (EditText) findViewById(R.id.et_detail);
		mImgDetailSaved = (ImageView) findViewById(R.id.img_detail);
		mVideoDetailSaved = (VideoView) findViewById(R.id.video_detail);
		mLayoutRecord = (RelativeLayout) findViewById(R.id.relative_detail_containrecord);
		mTextRecordState = (TextView) findViewById(R.id.tv_detail_voicesaved_name);
		mShowTextPopupwindow = (TextView) findViewById(R.id.tv_more);

		mSaveByTakeImageView = (ImageView) findViewById(R.id.iv_takephoto);
		mSaveVideoImageView = (ImageView) findViewById(R.id.iv_video);

		mTextDetailSave.setOnClickListener(this);
		mTextDetailBack.setOnClickListener(this);
		mShowTextPopupwindow.setOnClickListener(this);
		mLayoutRecord.setOnClickListener(this);
		mSoundPlay.setOnPlayCompletionListener(this);

	}

	private void initData() {
		item = (NoteEntity) getIntent().getSerializableExtra("item");
		// 得到传来的标题
		mTitleEt.setText(item.getTitleContent());
		mTitleEt.setTextSize(22.0f);
		// 得到传来的内容
		mEditTextDetailSaved.setText(item.getContent());
		mEditTextDetailSaved.setTextColor(Color.DKGRAY);
		mEditTextDetailSaved.setTextSize(17.0f);
		// 判断是否传来图片
		if (item.getPhoto() == null) {
			mImgDetailSaved.setVisibility(View.GONE);
		} else {
			mImgDetailSaved.setVisibility(View.VISIBLE);
			// 解析照片并显示
			Bitmap bitmap = BitmapFactory.decodeFile(item.getPhoto());
			// 图片压缩
			Bitmap newMap = BitmapUtil.scaleImage(bitmap, 300, 300);
			mImgDetailSaved.setImageBitmap(newMap);
		}
		// 判断是否传来视频
		if (item.getVideo() == null || item.getVideo().equals("null")) {
			mVideoDetailSaved.setVisibility(View.GONE);
		} else {
			mVideoDetailSaved.setVisibility(View.VISIBLE);
			// 解析视频并显示
			mVideoDetailSaved.setVideoPath(item.getVideo());
			mVideoDetailSaved.start();
		}
		// 判断是否传来录音
		if (item.getVoice() == null || item.getVoice().equals("null")) {
			mLayoutRecord.setVisibility(View.GONE);
		} else {
			mLayoutRecord.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_dt_save:
			mDBWriter.editDB(new NoteEntity(item.getID(), mTitleEt.getText()
					+ "", mEditTextDetailSaved.getText() + "", mDBWriter
					.getTime(), null, null, null));
			finish();
			break;

		case R.id.tv_delete:
			mDBWriter.deleteDB(item);
			finish();
			break;

		case R.id.tv_back:
			finish();
			break;

		case R.id.iv_takephoto:

			break;
			
		case R.id.iv_video:

			break;
			
		case R.id.relative_detail_containrecord:
			mSoundPlay.startPlay(item.getVoice());
			mTextRecordState.setText("正在播放");
			break;
			
		// 弹出popupwindow
		case R.id.tv_more:
			showPopupWindow(v);
			break;
			
		case R.id.tv_share:
			showShare();
			Toast.makeText(this, "去分享", Toast.LENGTH_SHORT).show();
			mPopWindow.dismiss();
			break;
		}
	}

	public void showPopupWindow(View v) {

		// 绑定popupwindow布局
		View contentView = View.inflate(this, R.layout.popupwindow, null);
		mPopWindow = new PopupWindow(contentView, 200, 150, true);

		TextView popTextShare = (TextView) contentView
				.findViewById(R.id.tv_share);
		TextView popTextDelect = (TextView) contentView
				.findViewById(R.id.tv_delete);

		popTextShare.setOnClickListener(this);
		popTextDelect.setOnClickListener(this);

		// 设置popupwindow区域外点击消失
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.popupwindow_bg));
		mPopWindow.showAsDropDown(v, 10, 10);
	}

	/**
	 * 分享
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();
//		oks.setTitle(item.getTitleContent());
//		oks.setText(item.getTitleContent());
		oks.setImagePath(item.getPhoto());
		oks.show(this);
	}

	/**
	 * 实现接口中的方法
	 */
	@Override
	public void PlayCompletion() {
		mTextRecordState.setText("播放结束");
	}

}
