package org.wzg.multinotes.utils;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.util.Log;

public class SoundPlay {
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private OnPlayCompletionListener onPlayCompletionListener;
	
	public SoundPlay() {

	}
	
	/**
	 * 开始录音
	 */
	public void startRecord(String filename) {
		
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(filename);
		try {
			mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mRecorder.start();   

	}

	/**
	 * 停止录音
	 */
	public void stopRecord() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	/**
	 * 播放录音
	 */
	public void startPlay(String filename) {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(filename);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					onPlayCompletionListener.PlayCompletion();
				}
			});
		} catch (IOException e) {
			Log.e("Soundplay", "播放失败");
		}
	}

	/**
	 * 停止播放
	 */
	public void stopPlay() {
		mPlayer.release();
		mPlayer = null;
	}

	/**
	 * 这里使用接口，在监听播放完成的方法中调用接口方法，外部类实现接口的具体方法
	 * 
	 * @ClassName: OnPlayCompletionListener
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author wzg0816 466959515@qq.com
	 * @date 2016-3-27 下午3:48:07
	 */
	public interface OnPlayCompletionListener {
		void PlayCompletion();
	}

	public void setOnPlayCompletionListener(
			OnPlayCompletionListener onPlayCompletionListener) {
		this.onPlayCompletionListener = onPlayCompletionListener;
	}

}
