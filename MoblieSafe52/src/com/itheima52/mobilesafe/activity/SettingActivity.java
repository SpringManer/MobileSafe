package com.itheima52.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.service.NumAddressService;
import com.itheima52.mobilesafe.view.SettingItemView;
import com.itheima52.mobilesafe.view.SettingSelectView;

public class SettingActivity extends Activity {

	private SettingItemView sivUpdate;
	private SharedPreferences sharedPreferences;
	private SettingItemView sivNumAddress;
	private SharedPreferences sharedPreferences2;
	private SettingSelectView styleSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sharedPreferences2 = getSharedPreferences("config", MODE_PRIVATE);

		initUpdate();
		initNumAddress();
		initAddressStyle();
		intiAddressLocation();

	}

	/**
	 * 初始化自动升级开关
	 */
	private void initUpdate() {

		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		sivUpdate.setTitle("设置自动更新");
		sivUpdate.setDesc("设置自动已开启");
		// shareprefer
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

		// 下次进入设置页面先读取一下上次的记录,进行设置
		boolean autoupdate = sharedPreferences.getBoolean("auto_update", true);
		if (autoupdate) {
			sivUpdate.setChecked(true);
			// sivUpdate.setDesc("设置自动已开启");
		} else {
			sivUpdate.setChecked(false);
			// sivUpdate.setDesc("设置自动已关闭");
		}

		// 设置监听，并将结果保存在share文件中
		sivUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断，点击的时候，反向勾选
				if (sivUpdate.isChecked()) {
					sivUpdate.setChecked(false);
					// 已经在自定义view中动态设置
					// sivUpdate.setDesc("设置自动已关闭");
					sharedPreferences.edit().putBoolean("auto_update", false)
							.commit();
				} else {
					sivUpdate.setChecked(true);
					// sivUpdate.setDesc("设置自动已开启");
					sharedPreferences.edit().putBoolean("auto_update", true)
							.commit();
				}

			}
		});

	}

	/**
	 * 初始化归属地开关
	 */
	private void initNumAddress() {

		sivNumAddress = (SettingItemView) findViewById(R.id.siv_num_address);

		sivNumAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断开关改变状态
				if (sivNumAddress.isChecked()) {
					sivNumAddress.setChecked(false);

					// 关闭号码归属地服务
					stopService(new Intent(SettingActivity.this,
							NumAddressService.class));

				} else {
					sivNumAddress.setChecked(true);

					// 开启号码归属地服务
					startService(new Intent(SettingActivity.this,
							NumAddressService.class));
					// 开启去电receiver

				}

			}
		});

	}

	/**
	 * 初始化归属地显示风格
	 */
	
	
	final String[] style = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
	
	private void initAddressStyle() {
		styleSelect = (SettingSelectView) findViewById(R.id.siv_style_select);
		
		int style1 = sharedPreferences2.getInt("address_style", 0);

		styleSelect.setTitle("归属地提示框风格");

		styleSelect.setDesc(style[style1]);
		// 点击弹出单选对话框
		styleSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 单选对话框
				showSingleDialog();

			}
		});

	}
	/**
	 * 初始化
	 */
	private void intiAddressLocation() {
		
		SettingSelectView addressLocation = (SettingSelectView) findViewById(R.id.siv_address_location);
		
		addressLocation.setTitle("归属地提示框位置");
		addressLocation.setDesc("设置归属地提示框位置");
		//设置点击事件
		addressLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 调转进调整归属地显示框页面
				
			startActivity(new Intent(SettingActivity.this,AdjustLocatonActivity.class));
				
			}
		});

	}

	/**
	 * 弹出单选对话框
	 */
	private void showSingleDialog() {
		
		
		int style1 = sharedPreferences2.getInt("address_style", 0);
		AlertDialog.Builder singleChoice = new Builder(SettingActivity.this);

		singleChoice.setTitle("归属地提示框风格");

		
		//默认选项
		singleChoice.setSingleChoiceItems(style, style1,
				new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//点击选项，把选项保存在share preference中。
						sharedPreferences2.edit().putInt("address_style", which).commit();
						
						styleSelect.setDesc(style[which]);
						
						
						dialog.dismiss();

					}

				});

		singleChoice.setNegativeButton("取消", null);
		
		singleChoice.show();

	}

}
