package com.itheima52.mobilesafe.db.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.itheima52.mobilesafe.db.BlackNumOpenHelper;
import com.itheima52.mobilesafe.db.bean.BlackNumInfo;

/**
 * 数据库查询工具
 * 
 * @author spring
 * 
 */
public class BlackNumDao {

	private BlackNumOpenHelper blackNumOpenHelper;

	// 为了方便同意管理，设计成单利模式（懒汉模式）

	// [1.私有化构造函数]
	private BlackNumDao(Context context) {
		blackNumOpenHelper = new BlackNumOpenHelper(context);
	}

	// [2.判断对象是否为空]
	static BlackNumDao backNumDao = null;

	// [3.提供环获取方法]
	public static BlackNumDao getInstance(Context context) {

		if (backNumDao == null) {
			backNumDao = new BlackNumDao(context);

		}

		return backNumDao;
	}

	/**
	 * 增加一个条目
	 * 
	 * @param num
	 *            （拦截的号码）
	 * @param mode
	 *            （拦截的类型 1.短信 2.电话 3.拦截所有 ）
	 */
	public void insert(String phone, String mode) {

		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put("phone", phone);
		contentValues.put("mode", mode);

		writableDatabase.insert("blacknumber", null, contentValues);

		writableDatabase.close();
	}

	/**
	 * 删除一个条目
	 * 
	 * @param num
	 */
	public void delete(String phone) {
		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		writableDatabase.delete("blacknumber", "phone = ? ",
				new String[] { phone });

		writableDatabase.close();

	}

	/**
	 * 更改数据
	 * 
	 * @param num
	 * @param mode
	 *            （拦截的类型 1.短信 2.电话 3.拦截所有 ）
	 */
	public void update(String phone, String mode) {

		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		writableDatabase.update("blacknumber", contentValues, "phone = ?",
				new String[] { phone });

		writableDatabase.close();

	}

	/**
	 *查询所有条目
	 * @return
	 */
	public ArrayList<BlackNumInfo> query() {

		ArrayList<BlackNumInfo> arrayList = new ArrayList<BlackNumInfo>();
		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		Cursor query = writableDatabase.query("blacknumber", new String[] {
				"phone", "mode" }, null, null, null, null, "_id desc");

		while (query.moveToNext()) {
			String phone = query.getString(0);
			String mode = query.getString(1);
			// 将数据存入java bean中
			BlackNumInfo blackNumInfo = new BlackNumInfo();

			blackNumInfo.phone = phone;
			blackNumInfo.mode = mode;
			
			// 将数据bean存放发到集合中
			arrayList.add(blackNumInfo);
		}

		//关闭
		query.close();
		writableDatabase.close();

		return arrayList;

	}
	
	/**
	 * 一次查询20个条目
	 * @param index
	 * @return
	 */
	public ArrayList<BlackNumInfo> find(int index ) {

		ArrayList<BlackNumInfo> arrayList = new ArrayList<BlackNumInfo>();
		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		Cursor query = writableDatabase.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20", new String[]{index+""});

		while (query.moveToNext()) {
			String phone = query.getString(0);
			String mode = query.getString(1);
			// 将数据存入java bean中
			BlackNumInfo blackNumInfo = new BlackNumInfo();

			blackNumInfo.phone = phone;
			blackNumInfo.mode = mode;
			
			// 将数据bean存放发到集合中
			arrayList.add(blackNumInfo);
		}

		//关闭
		query.close();
		writableDatabase.close();

		return arrayList;

	}
	/**
	 * 获取数据库条目总数
	 * @return
	 */
	public int getCount (){
		
		int mCount = 0 ;
		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		Cursor query = writableDatabase.rawQuery("select phone from blacknumber", null);
		
		
		if(query.moveToNext()){
			 mCount = query.getCount();
//			 mCount = query.getInt(0);
		}

		//关闭
		query.close();
		writableDatabase.close();

		return mCount;
		
	}
	/**
	 * 根据所给的号码，查询数据库返回所拦截的状态
	 * @param num
	 * @return
	 */
	public int findNum(String num ){
		
		int  mode = 0;
		SQLiteDatabase writableDatabase = blackNumOpenHelper
				.getWritableDatabase();

		Cursor query = writableDatabase.query("blacknumber", new String[]{"mode"}, "phone= ?", new String[]{num}, null, null, null);
		
		
		if(query.moveToNext()){
			mode = query.getInt(0);
		}

		//关闭
		query.close();
		writableDatabase.close();
		
		return mode;
		
	}
}
