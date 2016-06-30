package com.itheima52.mobilesafe.db.dao;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 归属地查询工具
 * 
 * @author spring
 * 
 */
public class AddressDao {

	private static String location = "未知号码";

	public static String getAddress(String str) {

		File path = new File(
				"data/data/com.itheima52.mobilesafe/files/address.db");

		SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
				path.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		// 正则表达式进行过滤
		// ^1[3-8]\d{9}$
		// 进行详细过滤判断
		if (str.matches("^1[3-8]\\d{9}$")) {

			Cursor rawQuery = sqLiteDatabase
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { str.substring(0, 7) });

			if (rawQuery.moveToNext()) {

				location = rawQuery.getString(0);
			}
		} else if (str.matches("^\\d+$")) {

			switch (str.length()) {
			case 3:

				location = "报警电话";
				break;

			case 5:
				location = "客服电话";
				break;
			case 7:
			case 8:
				location = "本地电话";
				break;
			case 1:
			case 2:
				location = "";
				break;
			case 4:
				location = "模拟器电话";
				break;

			default:
				// 其他情况，长途区号,以0开头，长度大于10
				if (str.startsWith("0") && str.length() > 10) {

					Cursor rawQuery = sqLiteDatabase.rawQuery(
							"select location from data2 where area = ?",
							new String[] { location.substring(1, 4) });
					
					if(rawQuery.moveToNext()){
						
						location = rawQuery.getString(0);
					}

				}
				break;
			}
		}

		return location;
	}
}
