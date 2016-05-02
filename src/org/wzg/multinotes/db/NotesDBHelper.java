package org.wzg.multinotes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDBHelper extends SQLiteOpenHelper {
	// 静态成员变量
	public static final String TABLE_NAME = "multinotes";
	public static final String ID = "_id";
	public static final String TITLE_CONTEXT = "title";
	public static final String CONTEXT = "context";
	public static final String PHOTO = "photo";
	public static final String VIDEO = "video";
	public static final String VOICE = "voice";
	public static final String TIME = "time";
	
	public NotesDBHelper(Context context) {
		super(context, "multinotes.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建表
		db.execSQL("create table " + TABLE_NAME + "(" + ID
				+ " integer primary key autoincrement," + TITLE_CONTEXT + " text," +CONTEXT
				+ " text," + PHOTO + " text," + VIDEO
				+ " text," + VOICE + " text," + TIME + " text not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
