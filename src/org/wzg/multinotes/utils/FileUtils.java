package org.wzg.multinotes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

	private static final String TAG = "FileUtil";

	/**
	 * 正则表达式判断文件名是否是纯数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 根据目录查找所有文件
	 * 
	 * @param filepath
	 * @return
	 */
	public static List<String> getFiles(String filepath) {
		// 根据上面获取到的路径 读取路径下的所有文件
		File filelist = new File(filepath);
		// list中就是指定文件所在目录下的所有文件的文件名
		List<String> list = Arrays.asList(filelist.list());
		List<String> lists = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			lists.add(filepath + "/" + list.get(i));// 得到绝对路径
		}
		return lists;
	}

	/**
	 * 将bitmap的uri存入内存
	 * 
	 * @param imageUri
	 * @return
	 */
	public static File getCacheFile(String imageUri) {
		File cacheFile = null;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String fileName = getFileName(imageUri);
				File dir = new File(sdCardDir.getCanonicalPath() + "/miaopai");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				cacheFile = new File(dir, fileName);
				Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir
						+ ",file:" + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "getCacheFileError:" + e.getMessage());
		}

		return cacheFile;
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 */
	public static void createFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static void createFiles(String path) {
		File file = new File(path);
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 将bitmap保存为文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static String saveFile(Bitmap bitmap, String path, String fileName)
			throws IOException {
		FileOutputStream b = null;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();// mkdirs创建多层目录
		}
		// 文件命名不可以有特殊字符
		File bitmapFile = new File(file, fileName);
		// if (!bitmapFile.exists()) {
		// bitmapFile.createNewFile();
		// }
		try {
			b = new FileOutputStream(bitmapFile);
			// 把数据写入文件
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmapFile.getAbsolutePath();
	}

}
