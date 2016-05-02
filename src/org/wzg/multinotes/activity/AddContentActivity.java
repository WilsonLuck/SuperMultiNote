package org.wzg.multinotes.activity;

import java.io.File;
import java.io.IOException;

import org.wzg.multifuncationnotes.R;
import org.wzg.multinotes.db.NotesDBHelper;
import org.wzg.multinotes.db.OperateNotesDataBase;
import org.wzg.multinotes.entity.NoteEntity;
import org.wzg.multinotes.utils.BitmapUtil;
import org.wzg.multinotes.utils.Config;
import org.wzg.multinotes.utils.SoundPlay;
import org.wzg.multinotes.utils.SoundPlay.OnPlayCompletionListener;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class AddContentActivity extends Activity implements OnClickListener,
		OnPlayCompletionListener {

	private OperateNotesDataBase mDBWriter;
	private TextView mTextSave;
	private TextView mTextCancle;
	/** 文字内容 */
	private EditText mEditText;
	/** 文字标题 */
	private EditText mEditTextTitle;
	/** 盛放暂存图片 */
	private ImageView mImgSaved;
	/** 盛放暂存video */
	private VideoView mVideoViewSaved;
	/** 盛放暂存voice */
	private TextView mTextVoiceSaved;
	/** 添加照片通过本地 */
	private ImageView mImgAddByLocalPhoto;
	/** 添加照片通过相机 */
	private ImageView mImgSaveByTake;
	/** 添加视频 */
	private ImageView mImgSaveVideo;
	/** 添加录音 */
	private ImageView mImgSaveRecord;
	/** 初始的添加页面底部布局 */
	private LinearLayout mLayoutPrimaryBottom;
	/** 点击录音后出现的布局 */
	private RelativeLayout mLayoutBottomRecord;
	/** 判断录音状态，待完善… */
	//private TextView mTextState;
	/** 停止录音 */
	private ImageView mImgStopRecord;
	/** 录音的显示 */
	private RelativeLayout mLayoutRecord;
	/** 使用相机拍照的相片路径 */
	private File mCameraPhoneFile;
	/** 通过相册使用照片的路径 */
	private String mLocalPhotoPath;
	/** 视频的保存路径 */
	private String mLocalVideoPath;
	/** 语音文件保存路径 */
	private String voicePath;

	private SoundPlay mSoundPlay;
	/** 请求标识 */
	private int mRequestCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addcontent);
		// 初始化数据库操作类
		mDBWriter = new OperateNotesDataBase(this);
		mSoundPlay = new SoundPlay();
		initViews();

	}

	private void initViews() {

		mTextCancle = (TextView) findViewById(R.id.tv_cancle);
		mTextSave = (TextView) findViewById(R.id.tv_save);

		mEditTextTitle = (EditText) findViewById(R.id.et_title);
		mEditText = (EditText) findViewById(R.id.et_text);
		mImgSaved = (ImageView) findViewById(R.id.img_saved);
		mVideoViewSaved = (VideoView) findViewById(R.id.video_saved);
		mLayoutPrimaryBottom = (LinearLayout) findViewById(R.id.linear_primarybottom);
		mLayoutBottomRecord = (RelativeLayout) findViewById(R.id.relative_replacebottom);
		mImgStopRecord = (ImageView) findViewById(R.id.img_stoprecord);
		mLayoutRecord = (RelativeLayout) findViewById(R.id.relative_containrecord);
		mTextVoiceSaved = (TextView) findViewById(R.id.tv_voicesaved_name);

		mImgAddByLocalPhoto = (ImageView) findViewById(R.id.iv_add_photo);
		mImgSaveByTake = (ImageView) findViewById(R.id.iv_add_takephoto);
		mImgSaveVideo = (ImageView) findViewById(R.id.iv_add_video);
		mImgSaveRecord = (ImageView) findViewById(R.id.iv_add_voice);

		mTextCancle.setOnClickListener(this);
		mTextSave.setOnClickListener(this);
		mImgAddByLocalPhoto.setOnClickListener(this);
		mImgSaveByTake.setOnClickListener(this);
		mImgSaveVideo.setOnClickListener(this);
		mImgSaveRecord.setOnClickListener(this);
		mImgStopRecord.setOnClickListener(this);
		mLayoutRecord.setOnClickListener(this);
		mSoundPlay.setOnPlayCompletionListener(this);

		mEditTextTitle.setTextSize(22.0f);
		mEditText.setTextColor(Color.DKGRAY);
		mEditText.setTextSize(17.0f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// 取消
		case R.id.tv_cancle:
			finish();
			break;

		// 调用数据库操作类添加方法，存入添加的文字、照片和视频
		case R.id.tv_save:
			toSave();
			break;

		// 通过相册添加照片
		case R.id.iv_add_photo:
			mImgSaved.setVisibility(View.VISIBLE);
			mVideoViewSaved.setVisibility(View.GONE);
			mLayoutRecord.setVisibility(View.GONE);

			addPictureByLocal();
			break;

		// 通过相机拍照添加相片
		case R.id.iv_add_takephoto:
			mImgSaved.setVisibility(View.VISIBLE);
			mVideoViewSaved.setVisibility(View.GONE);
			mLayoutRecord.setVisibility(View.GONE);

			addPictureBytake();
			break;

		// 添加视频
		case R.id.iv_add_video:
			mImgSaved.setVisibility(View.GONE);
			mVideoViewSaved.setVisibility(View.VISIBLE);
			mLayoutRecord.setVisibility(View.GONE);

			addVideoBytake();
			break;

		// 添加录音
		case R.id.iv_add_voice:
			mImgSaved.setVisibility(View.GONE);
			mVideoViewSaved.setVisibility(View.GONE);
			mLayoutPrimaryBottom.setVisibility(View.GONE);
			mLayoutBottomRecord.setVisibility(View.VISIBLE);

			addVoiceBySystem();
			break;

		// 保存录音
		case R.id.img_stoprecord:
			mImgSaved.setVisibility(View.GONE);
			mVideoViewSaved.setVisibility(View.GONE);
			mLayoutBottomRecord.setVisibility(View.GONE);
			mLayoutRecord.setVisibility(View.VISIBLE);

			mSoundPlay.stopRecord();
			break;

		// 播放录音
		case R.id.relative_containrecord:
			
			mSoundPlay.startPlay(voicePath);
			mTextVoiceSaved.setText("正在播放录音");
			break;
		}

	}

	/**
	 * 保存到数据库
	 */
	private void toSave() {

		if (mRequestCode == 1) {
			addToDB(mCameraPhoneFile + "", null);
		} else if (mRequestCode == 2) {
			addToDB(null, mLocalVideoPath);
		} else if (mRequestCode == 3) {
			addToDB(mLocalPhotoPath, null);
		} else {
			addToDB(null, null);
		}

		finish();
	}

	private void addToDB(String photo, String video) {
		mDBWriter.addDB(new NoteEntity(NotesDBHelper.ID, mEditTextTitle
				.getText() + "", mEditText.getText() + "", mDBWriter.getTime(),
				photo, mLocalVideoPath, voicePath));
	}

	// /**
	// * 通过intent开启，保存录像在绝对路径
	// */
	// private void choiceVideoBytake() {
	// Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//
	// 获取系统的摄像头进行录像
	// mVideoFile = new File(Environment.getExternalStorageDirectory()//
	// 建立一个存放video的路径，为在SD卡目录下保存此视频
	// .getAbsoluteFile() + "/" + System.currentTimeMillis() + ".mp4");
	//
	// video.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mVideoFile));//
	// 把视频存到SD卡目录下
	// startActivityForResult(video, 2);
	// }
	/**
	 * 得到照片通过照相
	 */
	public void addPictureBytake() {
		//这里调用了系统的相机，但是没有使用系统默认的路径，自己建了文件夹
		Intent img = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 得到sd卡的目录文件
		File fPath = Environment.getExternalStorageDirectory()
				.getAbsoluteFile();
		// FileUtils.createFiles(Config.PICTURES_PATH);创建文件夹
		// 图片路径
		mCameraPhoneFile = new File(fPath, (System.currentTimeMillis())
				+ ".jpg");
		img.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraPhoneFile));
		startActivityForResult(img, 1);
	}

	/**
	 * 添加录像
	 */
	public void addVideoBytake() {
		Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(video, 2);
	}

	/**
	 * 添加图片通过本地相册
	 */
	public void addPictureByLocal() {

		Intent album = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(album, 3);

	}

	/**
	 * 录音
	 */
	public void addVoiceBySystem() {

		//因为这个是自己写的路径，所以不知道是否存在，要创建文件夹以及文件
		//同时也正因为是自己写的录音机，所以也不需要回调数据
		//初始化文件
		File file = new File(Config.SOUND_PATH);
		if(!file.exists()){
			//这里要记得当自己定义了全局的静态变量时，还没有创建真的文件夹，一定要创建文件夹！！
			file.mkdirs();
		}
		//这里只是写出了要存的文件的名字
		voicePath =  file.getAbsolutePath() + mDBWriter.getTime() + ".mp3";
		//初始化要存的文件
		File voiceFile = new File(voicePath);
		//创建文件
		if(!voiceFile.exists()){
			try {
				voiceFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mTextVoiceSaved.setText(mDBWriter.getTime());
		mSoundPlay.startRecord(voicePath);
		Toast.makeText(this, "您已经开始录音", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 相机反馈给添加页面的信息
	 */
	@Override
	protected void onActivityResult(int mRequestCode, int resultCode,
			Intent data) {
		super.onActivityResult(mRequestCode, resultCode, data);

		AddContentActivity.this.mRequestCode = mRequestCode;// 使得这个mRequestCode和全局的是同一个
		if (resultCode == RESULT_OK) {
			if (mRequestCode == 1) {
				Bitmap bitmap = BitmapFactory.decodeFile(mCameraPhoneFile // ͨ解析存在内存卡里的File为bitmap
						.getAbsolutePath());

				Bitmap newBitmap = BitmapUtil.scaleImage(bitmap, 250, 250); // ͨ显示出来
				mImgSaved.setImageBitmap(newBitmap);
			}

			else if (mRequestCode == 2) {

				//存在系统默认的路径下
				Uri uri = data.getData();
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					mLocalVideoPath = cursor.getString(cursor
							.getColumnIndex(VideoColumns.DATA));
					if (mLocalVideoPath != null) {
						mVideoViewSaved.setVisibility(View.VISIBLE);
						mVideoViewSaved.setVideoPath(mLocalVideoPath);
						mVideoViewSaved.start();
					}
				}
				cursor.close();
			}

			else if (mRequestCode == 3) {
				//这里使用了系统的数据共享，通过查询系统的“数据库”得到照片的路劲，所以这时路径所在的文件夹都是真实存在的，不用创建文件夹了
				Uri selectedImageUri = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImageUri,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				mLocalPhotoPath = cursor.getString(columnIndex);
				Bitmap bitmap = BitmapFactory.decodeFile(mLocalPhotoPath);
				Bitmap newLocalBitmap = BitmapUtil.scaleImage(bitmap, 250, 250); // ͨ显示出来

				mImgSaved.setImageBitmap(newLocalBitmap);
				cursor.close();
			}

		}
	}

	/**
	 * 重写接口中的方法
	 */
	@Override
	public void PlayCompletion() {
		mTextVoiceSaved.setText("播放结束");
	}

}
