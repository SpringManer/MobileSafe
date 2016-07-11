package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.db.bean.BlackNumInfo;
import com.itheima52.mobilesafe.db.dao.BlackNumDao;
import com.itheima52.mobilesafe.utils.MD5Utils;

/**
 * 主页面
 * 
 * @author Kevin
 * 
 */
public class HomeActivity extends Activity {

	private GridView gvHome;

	private SharedPreferences mPref;

	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	private String existPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		setContentView(R.layout.activity_home);

		gvHome = (GridView) findViewById(R.id.gv_home);

		gvHome.setAdapter(new HomeAdapter());

		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 具体判断点击的id
				switch (position) {
				// 进入防盗页面
				case 0:

					showPasswordDialog();

					break;
					//进入通讯卫士页面
				case 1:
					
					//跳页面
					Intent intent1 = new Intent(getApplicationContext(),
							BlackNumAcyivity.class);
					startActivity(intent1);
//					test
//					BlackNumDao.getInstance(HomeActivity.this).insert("15295101238", "1");
//					BlackNumDao.getInstance(HomeActivity.this).update("110", "3");
//					BlackNumDao.getInstance(HomeActivity.this).delete("110");
					
//					System.out.println("-------------插入成功");
					
					
					
					

					break;
					//进入软件页面
				case 2:
					//跳页面
					Intent intent2 = new Intent(getApplicationContext(),
							AppManagerActivity.class);
					startActivity(intent2);

					break;
				case 3:

					break;
				case 4:

					break;
				case 5:

					break;
				case 6:

					break;
				case 7:
					// 高级工具，跳页面
					Intent intent7 = new Intent(getApplicationContext(),
							AdvanceToolsActivity.class);
					startActivity(intent7);
					
					
					

					break;
				// 设置中心，跳转set页面
				case 8:
					Intent intent8 = new Intent(getApplicationContext(),
							SettingActivity.class);
					startActivity(intent8);

					break;
				}

			}

		});
	}

	private void showPasswordDialog() {
		existPass = mPref.getString("password", null);
		// 盘点密码是否为空
		if (!TextUtils.isEmpty(existPass)) {
			// 让用户输入密码
			showPasswordInputDialog();

		} else {

			showPasswordSetDialog();

		}

	}

	/**
	 * 输入密码的界面
	 */
	private void showPasswordInputDialog() {

		AlertDialog.Builder alert = new Builder(this);

		final AlertDialog dialog = alert.create();

		View view = View.inflate(this, R.layout.dialog_input_password, null);
		// dialog.setView(view); 为了兼容2.3版本，用下面的方法、
		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();

		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		Button mBtnok = (Button) view.findViewById(R.id.btn_ok);
		Button mBtncan = (Button) view.findViewById(R.id.btn_cancel);

		mBtnok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 取出输入的密码
				String password = etPassword.getText().toString();

				// 检测密码是否为空
				if (!TextUtils.isEmpty(password)) {
					// 检测转换后的md5密码是否一致
					if (MD5Utils.encode(password).equals(existPass)) {

						Toast.makeText(getApplicationContext(), "登录成功", 0)
								.show();
						dialog.dismiss();
						// 进入丢失页面
						startActivity(new Intent(HomeActivity.this,
								LostFindActivity.class));


					} else {
						Toast.makeText(getApplicationContext(), "密码错误", 0)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
				}

			}
		});
		mBtncan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击取消，关闭对话框、
				dialog.dismiss();

			}
		});
	}

	/**
	 * 设置密码的界面
	 */
	private void showPasswordSetDialog() {
		// 密码设置界面
		AlertDialog.Builder alert = new Builder(this);

		final AlertDialog dialog = alert.create();

		View view = View.inflate(this, R.layout.dialog_set_password, null);
		// dialog.setView(view); 为了兼容2.3版本，用下面的方法、
		dialog.setView(view, 0, 0, 0, 0);

		dialog.show();
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConf = (EditText) view
				.findViewById(R.id.et_password_confirm);

		Button mBtnok = (Button) view.findViewById(R.id.btn_ok);
		Button mBtncan = (Button) view.findViewById(R.id.btn_cancel);

		mBtnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击确定，实现保存密码到shareprefer的逻辑
				// 取出密码
				String password = etPassword.getText().toString();
				String passwordConf = etPasswordConf.getText().toString();
				// 判断两次输入的密码是否相同而且不能为空
				if (!TextUtils.isEmpty(password) && !passwordConf.isEmpty()) {

					// 判断两次输入密码是否一致
					if (password.equals(passwordConf)) {
						// 将密码进行MD5加密在存储
						mPref.edit()
								.putString("password",
										MD5Utils.encode(password)).commit();

						Toast.makeText(getApplicationContext(), "设置成功", 0)
								.show();
						
						dialog.dismiss();

					} else {
						Toast.makeText(getApplicationContext(), "输入的密码不一致", 0)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
				}
			}
		});
		mBtncan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击取消，关闭对话框、
				dialog.dismiss();

			}
		});

	}

	class HomeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return mItems[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.home_list_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

			tvItem.setText(mItems[position]);
			ivItem.setImageResource(mPics[position]);
			return view;
		}

	}
}
