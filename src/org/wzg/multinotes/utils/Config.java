package org.wzg.multinotes.utils;

import android.os.Environment;


public class Config {
	public static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	public static final String PICTURES_PATH = PATH + "/MyNoteBook/pictures/";
	public static final String SOUND_PATH = PATH + "/MyNoteBook/sound";
	public static final String VIDEO_PATH = PATH + "/MyNoteBook/video/";
}
