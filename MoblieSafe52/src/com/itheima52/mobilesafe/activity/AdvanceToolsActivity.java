package com.itheima52.mobilesafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.SmsBackup;
import com.itheima52.mobilesafe.utils.SmsBackup.CallBack;

public class AdvanceToolsActivity extends Activity {

	private TextView location_query;
	private TextView sms_backup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_tool);

		initUI();

		numLoaction();

		smsBackup();

	}

	/**
	 * 短信备份
	 */
	private void smsBackup() {
		sms_backup.setOnClickListener(new OnClickListener() {
			private ProgressDialog progressDialog;

			// @Override
			public void onClick(View v) {
				progressDialog = new ProgressDialog(AdvanceToolsActivity.this);
				progressDialog.setTitle("短信备份");
				progressDialog.setIcon(R.drawable.main_sms_icon_pressed);
				progressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();

				// 调用备份短信的方法
				// uri content://sms/
				// 耗时操作开启子线程
				new Thread() {
					@Override
					public void run() {
						super.run();

						Uri uri = Uri.parse("content://sms/");
						String path = Environment.getExternalStorageDirectory()
								.getAbsolutePath()+File.separator
								+ "smsbackup74.xml";

						// 调用备份方法
						SmsBackup.backup(AdvanceToolsActivity.this, uri, path,
								new CallBack() {

									@Override
									public void setProgress(int prog) {
										progressDialog.setProgress(prog);
									}

									@Override
									public void setMax(int max) {
										progressDialog.setMax(max);

									}
								});
						//关闭对话框
						progressDialog.dismiss();
					}
				}.start();
			}
		});
	}

	/**
	 * 归属地查询
	 */
	private void numLoaction() {
		location_query.setOnClickListener(new OnClickListener() {
			// @Override
			public void onClick(View v) {
				// 点击跳转 AdvToolAddressActivity
				Intent intent = new Intent(AdvanceToolsActivity.this,
						AdvNumAddressActivity.class);
				startActivity(intent);

			}
		});
	}

	// 初始化ui
	private void initUI() {
		location_query = (TextView) findViewById(R.id.tv_location_query);
		sms_backup = (TextView) findViewById(R.id.tv_adv_sms_backup);
	}

}
