package com.itheima52.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.itheima52.mobilesafe.R;

public class ContactActivity extends Activity {
	private ContentResolver contentResolver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		ListView listview = (ListView) findViewById(R.id.lv_contact);
		// 数据源
		final ArrayList<HashMap<String, String>> readContact = readContact();
		// 适配器

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, readContact,
				R.layout.contact_listview_adaptor,
				new String[] { "name", "num" }, new int[] { R.id.tv_name,
						R.id.tv_num });

		listview.setAdapter(simpleAdapter);
		// 监听
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 将点击的item传回去
				String num = readContact.get(position).get("num");

				Intent intent = new Intent();
				intent.putExtra("num", num);
				//设置结果码为ok
				setResult(Activity.RESULT_OK, intent);

				finish();

			}
		});

	}

	/**
	 * 读取系统联系人信息 查询raw_comtacts 的 contact_id列.在根据contact_id查询view表
	 * （其实是view_date视图）的 data1列和miniType列。
	 * 
	 * 
	 */
	private ArrayList<HashMap<String, String>> readContact() {
		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

		// 拿到内容解析者

		Uri parse1 = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri parse2 = Uri.parse("content://com.android.contacts/data");

		contentResolver = getContentResolver();

		Cursor query1 = contentResolver.query(parse1,
				new String[] { "contact_id" }, null, null, null);

		if (query1 != null) {
			while (query1.moveToNext()) {
				// 拿到account_id
				String contact_id = query1.getString(0);

				// System.out.println(contact_id);

				// 根据contact_id去data表查询 data1列和miniType列
				Cursor query2 = contentResolver.query(parse2, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);

				if (query2 != null) {
					HashMap<String, String> hashMap = new HashMap<String, String>();
					while (query2.moveToNext()) {

						String data1 = query2.getString(0);
						String mimetype = query2.getString(1);

						System.out.println("--------------" + data1 + mimetype);
						// 判断mimeType类型，将数据存入hashMap中
						if ("vnd.android.cursor.item/name".equals(mimetype)) {
							hashMap.put("name", data1);

						} else if ("vnd.android.cursor.item/phone_v2"
								.equals(mimetype)) {

							hashMap.put("num", data1);

						}
					}
					arrayList.add(hashMap);
				}
			}
		}
		return arrayList;

	}
}
