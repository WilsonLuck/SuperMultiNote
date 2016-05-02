package org.wzg.multinotes.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wzg.multinotes.entity.NoteEntity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperateNotesDataBase {

	private Context mcontext;
	private NotesDBHelper mDBHelper;
	private SQLiteDatabase mDatabase;

	public OperateNotesDataBase(Context mcontext) {

		this.mcontext = mcontext;
		mDBHelper = new NotesDBHelper(mcontext);
		mDatabase = mDBHelper.getWritableDatabase();
	}

	/**
	 * 添加数据
	 */
	public void addDB(NoteEntity noteEntity) {

		ContentValues contentValues = new ContentValues();//类似hashmap，在对数据库做增加操作时使用，键值是数据库字段，值通过实体类得到具体属性值得到
		contentValues.put(NotesDBHelper.TITLE_CONTEXT,
				noteEntity.getTitleContent());
		contentValues.put(NotesDBHelper.CONTEXT, noteEntity.getContent());
		contentValues.put(NotesDBHelper.TIME, noteEntity.getTime());
		contentValues.put(NotesDBHelper.PHOTO, noteEntity.getPhoto());
		contentValues.put(NotesDBHelper.VIDEO, noteEntity.getVideo());
		contentValues.put(NotesDBHelper.VOICE, noteEntity.getVoice());

		mDatabase.insert(NotesDBHelper.TABLE_NAME, null, contentValues);

	}

	/**
	 * 删除数据
	 */
	public void deleteDB(NoteEntity noteEntity) {

		mDatabase.delete(NotesDBHelper.TABLE_NAME, "_id=" + noteEntity.getID(),
				null);

	}

	/**
	 * 编辑数据
	 */
	public void editDB(NoteEntity noteEntity) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(NotesDBHelper.TITLE_CONTEXT,
				noteEntity.getTitleContent());
		contentValues.put(NotesDBHelper.CONTEXT, noteEntity.getContent());
		mDatabase.update(NotesDBHelper.TABLE_NAME, contentValues,
				NotesDBHelper.ID + "=?", new String[] { noteEntity.getID() });
	}

	/**
	 * 查询数据
	 */
	public List<NoteEntity> queryDB() {
		List<NoteEntity> list = new ArrayList<NoteEntity>();
		Cursor cursor = mDatabase.query(NotesDBHelper.TABLE_NAME, null, null,
				null, null, null, "_id DESC");

		if (cursor != null) {
			while (cursor.moveToNext()) {
				String queryID = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.ID));
				String queryTitleContext = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.TITLE_CONTEXT));
				String queryContext = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.CONTEXT));
				String queryTime = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.TIME));
				String queryPhoto = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.PHOTO));
				String queryVideo = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.VIDEO));
				String queryVoice = cursor.getString(cursor
						.getColumnIndex(NotesDBHelper.VOICE));
				list.add(new NoteEntity(queryID, queryTitleContext,
						queryContext, queryTime, queryPhoto, queryVideo,
						queryVoice));
			}
		}
		cursor.close();
		return list;

	}

	/**
	 * 得到系统时间并规定格式
	 */
	public String getTime() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String gettime = dateFormat.format(date);
		return gettime;
	}
}
